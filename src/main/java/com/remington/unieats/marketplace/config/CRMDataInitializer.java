package com.remington.unieats.marketplace.config;

import com.remington.unieats.marketplace.model.entity.*;
import com.remington.unieats.marketplace.model.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Inicializador de datos de prueba para el CRM
 * Se ejecuta automáticamente al iniciar la aplicación
 * TEMPORALMENTE DESHABILITADO para permitir que la aplicación inicie
 */
@Component
@Order(2) // Se ejecuta DESPUÉS del DataLoader (que tiene @Order(1) implícito)
@Profile("disabled") // Deshabilitado temporalmente
public class CRMDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CRMDataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private SegmentoRepository segmentoRepository;

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private CampanaRepository campanaRepository;

    @Autowired
    private InteraccionClienteRepository interaccionClienteRepository;

    @Autowired
    private NotificacionMarketingRepository notificacionMarketingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("==========================================");
        logger.info("Verificando estado de datos del CRM...");
        logger.info("==========================================");
        
        // Verificar si ya existen clientes CRM
        long clientesCount = clienteRepository.count();
        long templatesCount = emailTemplateRepository.count();
        long segmentosCount = segmentoRepository.count();
        long campanasCount = campanaRepository.count();
        
        logger.info("Estado actual: {} clientes, {} templates, {} segmentos, {} campañas", 
                    clientesCount, templatesCount, segmentosCount, campanasCount);
        
        // Si ya hay datos completos del CRM, no hacer nada
        if (clientesCount > 0 && templatesCount > 0 && segmentosCount > 0 && campanasCount > 0) {
            logger.info("Ya existen datos completos del CRM en la base de datos. Omitiendo inicialización.");
            return;
        }

        logger.info("==========================================");
        logger.info("Iniciando inserción de datos de prueba del CRM...");
        logger.info("==========================================");

        try {
            // 1. Crear usuarios estudiantes (solo si no existen)
            List<Usuario> usuarios = crearUsuariosEstudiantes();
            logger.info("✓ Procesados {} usuarios estudiantes", usuarios.size());

            // 2. Crear clientes CRM (solo si no existen)
            List<Cliente> clientes = crearClientes(usuarios);
            logger.info("✓ Creados {} clientes CRM", clientes.size());

            // 3. Crear templates de email
            List<EmailTemplate> templates = crearEmailTemplates();
            logger.info("✓ Creados {} templates de email", templates.size());

            // 4. Crear segmentos
            List<Segmento> segmentos = crearSegmentos(clientes);
            logger.info("✓ Creados {} segmentos de clientes", segmentos.size());

            // 5. Crear campañas
            List<Campana> campanas = crearCampanas(segmentos, templates);
            logger.info("✓ Creadas {} campañas de marketing", campanas.size());

            // 6. Crear interacciones
            int interacciones = crearInteracciones(clientes, campanas);
            logger.info("✓ Creadas {} interacciones de clientes", interacciones);

            // 7. Crear notificaciones
            int notificaciones = crearNotificaciones(clientes, campanas);
            logger.info("✓ Creadas {} notificaciones de marketing", notificaciones);

            logger.info("==========================================");
            logger.info("Datos de prueba del CRM insertados exitosamente");
            logger.info("==========================================");

        } catch (Exception e) {
            logger.error("Error al insertar datos de prueba del CRM: {}", e.getMessage());
            logger.warn("La aplicación continuará sin datos del CRM");
        }
    }

    private List<Usuario> crearUsuariosEstudiantes() {
        List<Usuario> usuarios = new ArrayList<>();
        
        // Primero intentar obtener usuarios ESTUDIANTES existentes
        Rol rolEstudiante = rolRepository.findByNombre("ESTUDIANTE")
                .orElse(null);
        
        if (rolEstudiante == null) {
            logger.warn("Rol ESTUDIANTE no encontrado, no se pueden crear usuarios del CRM");
            return usuarios;
        }
        
        // Buscar usuarios existentes con rol ESTUDIANTE
        List<Usuario> usuariosExistentes = usuarioRepository.findAll().stream()
                .filter(u -> u.getRoles().contains(rolEstudiante))
                .toList();
        
        if (!usuariosExistentes.isEmpty()) {
            logger.info("Encontrados {} usuarios ESTUDIANTES existentes. Usando estos usuarios.", usuariosExistentes.size());
            // Usar hasta 10 usuarios existentes
            usuarios.addAll(usuariosExistentes.stream().limit(10).toList());
            return usuarios;
        }
        
        // Si no hay usuarios existentes, crear nuevos
        logger.info("No se encontraron usuarios ESTUDIANTES. Creando nuevos usuarios de prueba...");

        String[] nombres = {
                "María", "Carlos", "Ana",
                "Luis", "Diana", "Jorge",
                "Carolina", "Miguel", "Valentina", "Santiago"
        };

        String[] apellidos = {
                "García López", "Rodríguez", "Martínez Silva",
                "Fernando Torres", "Patricia Gómez", "Andrés Pérez",
                "Sánchez", "Ángel Castro", "Herrera", "Ramírez"
        };

        String[] emails = {
                "maria.garcia@uremington.edu.co", "carlos.rodriguez@uremington.edu.co",
                "ana.martinez@uremington.edu.co", "luis.torres@uremington.edu.co",
                "diana.gomez@uremington.edu.co", "jorge.perez@uremington.edu.co",
                "carolina.sanchez@uremington.edu.co", "miguel.castro@uremington.edu.co",
                "valentina.herrera@uremington.edu.co", "santiago.ramirez@uremington.edu.co"
        };

        String[] cedulas = {
                "1001234567", "1001234568", "1001234569",
                "1001234570", "1001234571", "1001234572",
                "1001234573", "1001234574", "1001234575", "1001234576"
        };

        int[] diasRegistro = {90, 60, 45, 30, 15, 10, 5, 3, 2, 1};

        for (int i = 0; i < nombres.length; i++) {
            // Verificar si el usuario ya existe
            if (usuarioRepository.findByCorreo(emails[i]).isPresent()) {
                usuarios.add(usuarioRepository.findByCorreo(emails[i]).get());
                continue;
            }

            Usuario usuario = new Usuario();
            usuario.setNombre(nombres[i]);
            usuario.setApellido(apellidos[i]);
            usuario.setCorreo(emails[i]);
            usuario.setCedula(cedulas[i]);
            usuario.setTelefono("300123456" + i);
            usuario.setContrasenaHash(passwordEncoder.encode("password123")); // Contraseña de prueba
            usuario.setRoles(Set.of(rolEstudiante));
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now().minusDays(diasRegistro[i]));
            usuarios.add(usuarioRepository.save(usuario));
        }

        return usuarios;
    }

    private List<Cliente> crearClientes(List<Usuario> usuarios) {
        List<Cliente> clientes = new ArrayList<>();

        double[] valoresCompras = {285000, 195000, 340000, 125000, 78000, 95000, 42000, 25000, 18000, 0};
        int[] numeroPedidos = {18, 12, 22, 8, 5, 6, 3, 2, 1, 0};
        String[] niveles = {"PLATINUM", "ORO", "PLATINUM", "PLATA", "BRONCE", "PLATA", "BRONCE", "BRONCE", "BRONCE", "BRONCE"};
        int[] diasUltimaCompra = {3, 5, 2, 7, 10, 4, 3, 2, 1, -1};

        for (int i = 0; i < usuarios.size(); i++) {
            Cliente cliente = new Cliente();
            cliente.setUsuario(usuarios.get(i));
            cliente.setValorTotalCompras(valoresCompras[i]);
            cliente.setNumeroPedidos(numeroPedidos[i]);

            if (numeroPedidos[i] > 0) {
                cliente.setTicketPromedio(valoresCompras[i] / numeroPedidos[i]);
                cliente.setUltimaCompra(LocalDateTime.now().minusDays(diasUltimaCompra[i]));
            }

            if (numeroPedidos[i] >= 15) {
                cliente.setFrecuenciaCompra("ALTA");
            } else if (numeroPedidos[i] >= 8) {
                cliente.setFrecuenciaCompra("MEDIA");
            } else {
                cliente.setFrecuenciaCompra("BAJA");
            }

            cliente.setUltimaInteraccion(LocalDateTime.now().minusDays(1));
            cliente.setPuntosFidelidad((int) (valoresCompras[i] / 1000));
            cliente.setNivelCliente(niveles[i]);
            cliente.setAceptaEmail(true);
            cliente.setAceptaSms(i < 3); // Solo los primeros 3 aceptan SMS
            cliente.setFechaRegistro(LocalDateTime.now().minusDays(diasUltimaCompra[i] + 5));

            clientes.add(clienteRepository.save(cliente));
        }

        return clientes;
    }

    private List<EmailTemplate> crearEmailTemplates() {
        List<EmailTemplate> templates = new ArrayList<>();

        // Template 1: Bienvenida VIP
        EmailTemplate bienvenida = new EmailTemplate();
        bienvenida.setNombre("Bienvenida VIP");
        bienvenida.setAsunto("¡Bienvenido a UniEats, {nombre}!");
        bienvenida.setContenidoHtml("<!DOCTYPE html><html><head><style>body{font-family:Arial,sans-serif;background-color:#f5f5f5;margin:0;padding:20px}.container{max-width:600px;margin:0 auto;background-color:#fff;padding:30px;border-radius:10px;box-shadow:0 2px 10px rgba(0,0,0,0.1)}h1{color:#7c3aed;margin-bottom:20px}p{color:#333;line-height:1.6}.button{display:inline-block;background-color:#7c3aed;color:#fff;padding:12px 30px;text-decoration:none;border-radius:5px;margin-top:20px}.footer{margin-top:30px;padding-top:20px;border-top:1px solid #eee;color:#666;font-size:12px}</style></head><body><div class=\"container\"><h1>¡Hola {nombre}!</h1><p>Bienvenido a <strong>UniEats</strong>, el marketplace de comida favorito de la Universidad Remington.</p><p>Tu nivel actual es: <strong style=\"color:#7c3aed\">{nivel}</strong></p><p>Has acumulado <strong>{puntos} puntos</strong> de fidelidad.</p><a href=\"https://unieats.com/productos\" class=\"button\">Explorar Productos</a><div class=\"footer\">Este es un email automático. UniEats - Universidad Remington</div></div></body></html>");
        bienvenida.setCategoria("BIENVENIDA");
        bienvenida.setDescripcion("Template de bienvenida para nuevos clientes VIP");
        bienvenida.setActivo(true);
        bienvenida.setFechaCreacion(LocalDateTime.now().minusDays(60));
        templates.add(emailTemplateRepository.save(bienvenida));

        // Template 2: Promoción Especial
        EmailTemplate promocion = new EmailTemplate();
        promocion.setNombre("Promoción Especial");
        promocion.setAsunto("🎉 Promoción exclusiva para ti, {nombre}");
        promocion.setContenidoHtml("<!DOCTYPE html><html><head><style>body{font-family:Arial,sans-serif;background-color:#f5f5f5;margin:0;padding:20px}.container{max-width:600px;margin:0 auto;background-color:#fff;padding:30px;border-radius:10px}.promo{background:linear-gradient(135deg,#667eea 0%,#764ba2 100%);color:#fff;padding:30px;border-radius:8px;text-align:center;margin:20px 0}h1{margin:0 0 15px 0;font-size:28px}h2{margin:0;font-size:42px}.discount{font-size:64px;font-weight:bold;margin:10px 0}.button{display:inline-block;background-color:#fff;color:#667eea;padding:15px 40px;text-decoration:none;border-radius:25px;margin-top:20px;font-weight:bold}</style></head><body><div class=\"container\"><div class=\"promo\"><h1>¡OFERTA EXCLUSIVA!</h1><div class=\"discount\">20%</div><h2>DE DESCUENTO</h2><p>En todos nuestros productos</p><a href=\"https://unieats.com/promociones\" class=\"button\">COMPRAR AHORA</a></div><p>Hola {nombre}, como cliente <strong>{nivel}</strong> tienes acceso a esta promoción exclusiva.</p></div></body></html>");
        promocion.setCategoria("PROMOCION");
        promocion.setDescripcion("Template para promociones y ofertas especiales");
        promocion.setActivo(true);
        promocion.setFechaCreacion(LocalDateTime.now().minusDays(45));
        templates.add(emailTemplateRepository.save(promocion));

        // Template 3: Carrito Abandonado
        EmailTemplate carrito = new EmailTemplate();
        carrito.setNombre("Recuperación de Carrito");
        carrito.setAsunto("{nombre}, ¡No te olvides de tu pedido! 🛒");
        carrito.setContenidoHtml("<!DOCTYPE html><html><head><style>body{font-family:Arial,sans-serif;background-color:#f5f5f5;margin:0;padding:20px}.container{max-width:600px;margin:0 auto;background-color:#fff;padding:30px;border-radius:10px}.cart-icon{font-size:64px;text-align:center;margin:20px 0}.total{background-color:#f0f0f0;padding:20px;border-radius:5px;font-size:24px;font-weight:bold;text-align:center;margin:20px 0}.button{display:inline-block;background-color:#10b981;color:#fff;padding:15px 40px;text-decoration:none;border-radius:5px;margin-top:20px;text-align:center;display:block}</style></head><body><div class=\"container\"><div class=\"cart-icon\">🛒</div><h1>¡Tu carrito te está esperando!</h1><p>Hola {nombre},</p><p>Notamos que dejaste algunos productos en tu carrito. ¡No pierdas esta oportunidad!</p><div class=\"total\">Total: ${valorCarrito}</div><a href=\"https://unieats.com/carrito\" class=\"button\">COMPLETAR MI PEDIDO</a></div></body></html>");
        carrito.setCategoria("ABANDONO_CARRITO");
        carrito.setDescripcion("Recordatorio para carritos abandonados");
        carrito.setActivo(true);
        carrito.setFechaCreacion(LocalDateTime.now().minusDays(30));
        templates.add(emailTemplateRepository.save(carrito));

        // Template 4: Fidelidad
        EmailTemplate fidelidad = new EmailTemplate();
        fidelidad.setNombre("Programa de Fidelidad");
        fidelidad.setAsunto("⭐ ¡Felicidades {nombre}! Subiste de nivel");
        fidelidad.setContenidoHtml("<!DOCTYPE html><html><head><style>body{font-family:Arial,sans-serif;background-color:#f5f5f5;margin:0;padding:20px}.container{max-width:600px;margin:0 auto;background-color:#fff;padding:30px;border-radius:10px}.badge{text-align:center;margin:30px 0}.badge-icon{font-size:80px}.nivel{font-size:32px;font-weight:bold;margin:10px 0;color:#7c3aed}</style></head><body><div class=\"container\"><div class=\"badge\"><div class=\"badge-icon\">🏆</div><div class=\"nivel\">{nivelNuevo}</div><p>¡Has alcanzado un nuevo nivel!</p></div><p>Estimado/a {nombre}, has sido promovido al nivel <strong>{nivelNuevo}</strong>.</p></div></body></html>");
        fidelidad.setCategoria("FIDELIDAD");
        fidelidad.setDescripcion("Notificación de cambio de nivel");
        fidelidad.setActivo(true);
        fidelidad.setFechaCreacion(LocalDateTime.now().minusDays(20));
        templates.add(emailTemplateRepository.save(fidelidad));

        return templates;
    }

    private List<Segmento> crearSegmentos(List<Cliente> clientes) {
        List<Segmento> segmentos = new ArrayList<>();

        // Segmento 1: Clientes VIP
        Segmento vip = new Segmento();
        vip.setNombre("Clientes VIP");
        vip.setDescripcion("Clientes con compras superiores a $200,000");
        vip.setCriterios("{\"valor_minimo\": 200000, \"nivel\": [\"PLATINUM\", \"ORO\"]}");
        vip.setActivo(true);
        vip.setFechaCreacion(LocalDateTime.now().minusDays(30));
        List<Cliente> clientesVip = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.getValorTotalCompras() != null && c.getValorTotalCompras() > 200000) {
                clientesVip.add(c);
            }
        }
        vip.setClientes(clientesVip);
        vip.setCantidadClientes(clientesVip.size());
        segmentos.add(segmentoRepository.save(vip));

        // Segmento 2: Compradores Frecuentes
        Segmento frecuentes = new Segmento();
        frecuentes.setNombre("Compradores Frecuentes");
        frecuentes.setDescripcion("Clientes con más de 10 pedidos");
        frecuentes.setCriterios("{\"pedidos_minimos\": 10}");
        frecuentes.setActivo(true);
        frecuentes.setFechaCreacion(LocalDateTime.now().minusDays(25));
        List<Cliente> clientesFrecuentes = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.getNumeroPedidos() != null && c.getNumeroPedidos() >= 10) {
                clientesFrecuentes.add(c);
            }
        }
        frecuentes.setClientes(clientesFrecuentes);
        frecuentes.setCantidadClientes(clientesFrecuentes.size());
        segmentos.add(segmentoRepository.save(frecuentes));

        // Segmento 3: Nuevos Clientes
        Segmento nuevos = new Segmento();
        nuevos.setNombre("Nuevos Clientes");
        nuevos.setDescripcion("Registrados en los últimos 30 días");
        nuevos.setCriterios("{\"dias_registro\": 30}");
        nuevos.setActivo(true);
        nuevos.setFechaCreacion(LocalDateTime.now().minusDays(20));
        List<Cliente> clientesNuevos = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.getFechaRegistro().isAfter(LocalDateTime.now().minusDays(30))) {
                clientesNuevos.add(c);
            }
        }
        nuevos.setClientes(clientesNuevos);
        nuevos.setCantidadClientes(clientesNuevos.size());
        segmentos.add(segmentoRepository.save(nuevos));

        // Segmento 4: Por niveles
        String[] niveles = {"BRONCE", "PLATA", "ORO", "PLATINUM"};
        for (String nivel : niveles) {
            Segmento segNivel = new Segmento();
            segNivel.setNombre("Nivel " + nivel);
            segNivel.setDescripcion("Clientes nivel " + nivel);
            segNivel.setCriterios("{\"nivel\": \"" + nivel + "\"}");
            segNivel.setActivo(true);
            segNivel.setFechaCreacion(LocalDateTime.now().minusDays(15));
            List<Cliente> clientesNivel = new ArrayList<>();
            for (Cliente c : clientes) {
                if (nivel.equals(c.getNivelCliente())) {
                    clientesNivel.add(c);
                }
            }
            segNivel.setClientes(clientesNivel);
            segNivel.setCantidadClientes(clientesNivel.size());
            segmentos.add(segmentoRepository.save(segNivel));
        }

        return segmentos;
    }

    private List<Campana> crearCampanas(List<Segmento> segmentos, List<EmailTemplate> templates) {
        List<Campana> campanas = new ArrayList<>();

        // Buscar segmentos y templates específicos
        Segmento segFrecuentes = segmentos.stream()
                .filter(s -> s.getNombre().equals("Compradores Frecuentes"))
                .findFirst().orElse(segmentos.get(0));

        Segmento segNuevos = segmentos.stream()
                .filter(s -> s.getNombre().equals("Nuevos Clientes"))
                .findFirst().orElse(segmentos.get(0));

        Segmento segVip = segmentos.stream()
                .filter(s -> s.getNombre().equals("Clientes VIP"))
                .findFirst().orElse(segmentos.get(0));

        EmailTemplate tempPromocion = templates.stream()
                .filter(t -> t.getNombre().equals("Promoción Especial"))
                .findFirst().orElse(templates.get(0));

        EmailTemplate tempBienvenida = templates.stream()
                .filter(t -> t.getNombre().equals("Bienvenida VIP"))
                .findFirst().orElse(templates.get(0));

        // Campaña 1: Black Friday (Finalizada)
        Campana blackFriday = new Campana();
        blackFriday.setNombre("Campaña Black Friday 2024");
        blackFriday.setDescripcion("Promoción especial de Black Friday con descuentos de hasta 50%");
        blackFriday.setTipo("EMAIL");
        blackFriday.setEstado("FINALIZADA");
        blackFriday.setSegmento(segFrecuentes);
        blackFriday.setTemplate(tempPromocion);
        blackFriday.setFechaInicio(LocalDateTime.now().minusDays(15));
        blackFriday.setFechaFin(LocalDateTime.now().minusDays(8));
        blackFriday.setFechaCreacion(LocalDateTime.now().minusDays(20));
        blackFriday.setPresupuesto(100000.0);
        blackFriday.setObjetivo("Incrementar ventas en 30% durante Black Friday");
        blackFriday.setEnviosTotales(245);
        blackFriday.setEnviosExitosos(238);
        blackFriday.setEnviosFallidos(7);
        blackFriday.setTasaApertura(68.5);
        blackFriday.setTasaClics(42.3);
        blackFriday.setConversiones(89);
        blackFriday.setIngresosGenerados(4250000.0);
        blackFriday.setRoi(42.5);
        campanas.add(campanaRepository.save(blackFriday));

        // Campaña 2: Bienvenida (Activa)
        Campana bienvenida = new Campana();
        bienvenida.setNombre("Bienvenida Nuevos Estudiantes");
        bienvenida.setDescripcion("Campaña de bienvenida para estudiantes que se registraron este mes");
        bienvenida.setTipo("EMAIL");
        bienvenida.setEstado("ACTIVA");
        bienvenida.setSegmento(segNuevos);
        bienvenida.setTemplate(tempBienvenida);
        bienvenida.setFechaInicio(LocalDateTime.now().minusDays(5));
        bienvenida.setFechaFin(LocalDateTime.now().plusDays(25));
        bienvenida.setFechaCreacion(LocalDateTime.now().minusDays(7));
        bienvenida.setPresupuesto(50000.0);
        bienvenida.setObjetivo("Convertir nuevos registros en primeros compradores");
        bienvenida.setEnviosTotales(156);
        bienvenida.setEnviosExitosos(152);
        bienvenida.setEnviosFallidos(4);
        bienvenida.setTasaApertura(72.8);
        bienvenida.setTasaClics(38.9);
        bienvenida.setConversiones(45);
        bienvenida.setIngresosGenerados(890000.0);
        bienvenida.setRoi(17.8);
        campanas.add(campanaRepository.save(bienvenida));

        // Campaña 3: Reactivación VIP (Programada)
        Campana reactivacion = new Campana();
        reactivacion.setNombre("Reactivación de Clientes VIP");
        reactivacion.setDescripcion("Campaña para reactivar clientes VIP que no han comprado recientemente");
        reactivacion.setTipo("EMAIL");
        reactivacion.setEstado("PROGRAMADA");
        reactivacion.setSegmento(segVip);
        reactivacion.setTemplate(tempPromocion);
        reactivacion.setFechaInicio(LocalDateTime.now().plusDays(3));
        reactivacion.setFechaCreacion(LocalDateTime.now().minusDays(2));
        reactivacion.setPresupuesto(75000.0);
        reactivacion.setObjetivo("Recuperar clientes VIP inactivos con ofertas exclusivas");
        reactivacion.setEnviosTotales(0);
        reactivacion.setEnviosExitosos(0);
        reactivacion.setEnviosFallidos(0);
        reactivacion.setTasaApertura(0.0);
        reactivacion.setTasaClics(0.0);
        reactivacion.setConversiones(0);
        reactivacion.setIngresosGenerados(0.0);
        reactivacion.setRoi(0.0);
        campanas.add(campanaRepository.save(reactivacion));

        // Campaña 4: Navidad (Borrador)
        Campana navidad = new Campana();
        navidad.setNombre("Navidad 2024 - Preparación");
        navidad.setDescripcion("Campaña preparatoria para la temporada navideña");
        navidad.setTipo("EMAIL");
        navidad.setEstado("BORRADOR");
        navidad.setFechaCreacion(LocalDateTime.now().minusDays(1));
        navidad.setPresupuesto(150000.0);
        navidad.setObjetivo("Preparar campaña navideña con promociones especiales");
        navidad.setEnviosTotales(0);
        navidad.setEnviosExitosos(0);
        navidad.setEnviosFallidos(0);
        navidad.setTasaApertura(0.0);
        navidad.setTasaClics(0.0);
        navidad.setConversiones(0);
        navidad.setIngresosGenerados(0.0);
        navidad.setRoi(0.0);
        campanas.add(campanaRepository.save(navidad));

        return campanas;
    }

    private int crearInteracciones(List<Cliente> clientes, List<Campana> campanas) {
        int count = 0;

        Campana blackFriday = campanas.stream()
                .filter(c -> c.getNombre().contains("Black Friday"))
                .findFirst().orElse(null);

        // Crear interacciones de compra
        for (int i = 0; i < Math.min(clientes.size(), 8); i++) {
            Cliente cliente = clientes.get(i);
            if (cliente.getNumeroPedidos() > 0) {
                InteraccionCliente compra = new InteraccionCliente();
                compra.setCliente(cliente);
                compra.setTipo("COMPRA");
                compra.setCanal("WEB");
                compra.setDescripcion("Compra de productos variados");
                compra.setFechaInteraccion(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
                compra.setValorMonetario(15000.0 + (Math.random() * 35000));
                interaccionClienteRepository.save(compra);
                count++;
            }
        }

        // Interacciones de email abierto
        if (blackFriday != null) {
            for (int i = 0; i < Math.min(clientes.size(), 6); i++) {
                Cliente cliente = clientes.get(i);
                if (cliente.getAceptaEmail()) {
                    InteraccionCliente emailAbierto = new InteraccionCliente();
                    emailAbierto.setCliente(cliente);
                    emailAbierto.setTipo("EMAIL_ABIERTO");
                    emailAbierto.setCanal("EMAIL");
                    emailAbierto.setDescripcion("Abrió email de campaña Black Friday");
                    emailAbierto.setFechaInteraccion(LocalDateTime.now().minusDays(12));
                    emailAbierto.setCampana(blackFriday);
                    interaccionClienteRepository.save(emailAbierto);
                    count++;
                }
            }
        }

        // Interacciones adicionales
        if (clientes.size() > 0) {
            InteraccionCliente feedback = new InteraccionCliente();
            feedback.setCliente(clientes.get(1));
            feedback.setTipo("FEEDBACK_POSITIVO");
            feedback.setCanal("WEB");
            feedback.setDescripcion("Calificación 5 estrellas en pedido");
            feedback.setFechaInteraccion(LocalDateTime.now().minusDays(3));
            feedback.setValorMonetario(42500.0);
            interaccionClienteRepository.save(feedback);
            count++;
        }

        return count;
    }

    private int crearNotificaciones(List<Cliente> clientes, List<Campana> campanas) {
        int count = 0;

        Campana blackFriday = campanas.stream()
                .filter(c -> c.getNombre().contains("Black Friday"))
                .findFirst().orElse(null);

        Campana bienvenida = campanas.stream()
                .filter(c -> c.getNombre().contains("Bienvenida"))
                .findFirst().orElse(null);

        // Notificaciones de Black Friday
        if (blackFriday != null) {
            for (int i = 0; i < Math.min(clientes.size(), 7); i++) {
                Cliente cliente = clientes.get(i);
                if (cliente.getAceptaEmail()) {
                    NotificacionMarketing notif = new NotificacionMarketing();
                    notif.setCliente(cliente);
                    notif.setCampana(blackFriday);
                    notif.setTipo("EMAIL");
                    notif.setEstado(Math.random() < 0.7 ? "ABIERTO" : "ENTREGADO");
                    notif.setDestinatario(cliente.getUsuario().getCorreo());
                    notif.setAsunto("Promoción Black Friday - Descuentos exclusivos");
                    notif.setFechaProgramada(LocalDateTime.now().minusDays(14));
                    notif.setFechaEnvio(LocalDateTime.now().minusDays(14));
                    if ("ABIERTO".equals(notif.getEstado())) {
                        notif.setFechaApertura(LocalDateTime.now().minusDays(13));
                    }
                    notif.setIntentosEnvio(1);
                    notificacionMarketingRepository.save(notif);
                    count++;
                }
            }
        }

        // Notificaciones de Bienvenida
        if (bienvenida != null) {
            for (int i = 0; i < Math.min(clientes.size(), 5); i++) {
                Cliente cliente = clientes.get(i);
                if (cliente.getAceptaEmail() && 
                    cliente.getFechaRegistro().isAfter(LocalDateTime.now().minusDays(30))) {
                    NotificacionMarketing notif = new NotificacionMarketing();
                    notif.setCliente(cliente);
                    notif.setCampana(bienvenida);
                    notif.setTipo("EMAIL");
                    notif.setEstado(Math.random() < 0.75 ? "ABIERTO" : "ENTREGADO");
                    notif.setDestinatario(cliente.getUsuario().getCorreo());
                    notif.setAsunto("Bienvenido a UniEats - Tu marketplace universitario");
                    notif.setFechaProgramada(LocalDateTime.now().minusDays(4));
                    notif.setFechaEnvio(LocalDateTime.now().minusDays(4));
                    if ("ABIERTO".equals(notif.getEstado())) {
                        notif.setFechaApertura(LocalDateTime.now().minusDays(3));
                    }
                    notif.setIntentosEnvio(1);
                    notificacionMarketingRepository.save(notif);
                    count++;
                }
            }
        }

        return count;
    }
}
