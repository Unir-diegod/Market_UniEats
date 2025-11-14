package com.remington.unieats.marketplace.config;

import com.remington.unieats.marketplace.model.entity.*;
import com.remington.unieats.marketplace.model.repository.*;
import com.remington.unieats.marketplace.repository.UsuarioComportamientoRepository;
import com.remington.unieats.marketplace.entity.UsuarioComportamiento;
import com.remington.unieats.marketplace.model.enums.EstadoTienda;
import com.remington.unieats.marketplace.model.enums.ClasificacionProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;
import java.util.Random;

@Component
@Order(1) // Se ejecuta PRIMERO para crear datos base (roles, usuarios, tiendas)
@Profile("!test") // No ejecutar en tests
public class DataLoader implements CommandLineRunner {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataLoader.class);

    @Autowired private RolRepository rolRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TiendaRepository tiendaRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private UsuarioComportamientoRepository usuarioComportamientoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        logger.info("DataLoader iniciado...");
        
        try {
            // Verificar si necesitamos datos básicos
            boolean baseDatosVacia = rolRepository.count() == 0 || tiendaRepository.count() == 0;
            
            if (baseDatosVacia) {
                // 1. Crear roles
                crearRoles();
                
                // 2. Crear usuarios administrador y estudiante
                crearUsuariosBase();
                
                // 4. Crear 6 tiendas con sus vendedores
                crearTiendasCompletas();
                
                // 5. Crear productos para cada tienda
                crearProductos();
                
                logger.info("Datos básicos creados exitosamente");
            } else {
                logger.info("Base de datos ya tiene datos básicos");
                
                // ⚠️ VERIFICAR SI PRODUCTOS TIENEN CLASIFICACIONES CORRECTAS (solo la primera vez)
                long productosExistentes = productoRepository.count();
                if (productosExistentes > 0) {
                    // Verificar si al menos 1 producto tiene una clasificación diferente a SIN_CATEGORIA
                    long productosSinClasificar = productoRepository.findAll().stream()
                            .filter(p -> p.getClasificacion() == null || 
                                        p.getClasificacion() == ClasificacionProducto.SIN_CATEGORIA)
                            .count();
                    
                    if (productosSinClasificar == productosExistentes) {
                        logger.info("TODOS los productos tienen SIN_CATEGORIA. Recreando con clasificaciones correctas...");
                        productoRepository.deleteAll();
                        logger.info("Productos eliminados. Recreando...");
                        crearProductos();
                        logger.info("Productos recreados con clasificaciones correctas");
                    } else {
                        logger.info("Los productos ya tienen clasificaciones correctas");
                    }
                }
            }
            
            // SIEMPRE verificar y crear usuarios especializados para ML
            crearUsuariosEspecializados();
            
            // SIEMPRE verificar y crear comportamientos ML para usuarios especializados
            crearComportamientosML();
            
            logger.info("DataLoader completado exitosamente");
            logger.info("Sistema listo para usar con datos de prueba completos");
            logger.info("Credenciales de prueba:");
            logger.info("   Admin: admin@unieats.com / admin123");
            logger.info("   Estudiante: estudiante@unieats.com / estudiante123");
            logger.info("   Bebidas ML: bebidas@unieats.com / bebidas123");
            logger.info("   Almuerzo ML: almuerzo@unieats.com / almuerzo123");
            logger.info("   Vendedores: tienda1@gmail.com a tienda6@gmail.com / vendedor123");
            
        } catch (Exception e) {
            logger.error("Error en DataLoader: {}", e.getMessage(), e);
            logger.warn("La aplicación continuará sin datos iniciales");
        }
    }

    private void crearRoles() {
        logger.info("Creando roles...");
        crearRolSiNoExiste("ESTUDIANTE");
        crearRolSiNoExiste("VENDEDOR");
        crearRolSiNoExiste("ADMIN_PLATAFORMA");
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (rolRepository.findByNombre(nombreRol).isEmpty()) {
            Rol nuevoRol = new Rol();
            nuevoRol.setNombre(nombreRol);
            rolRepository.save(nuevoRol);
            logger.info("Rol creado: {}", nombreRol);
        }
    }

    private void crearUsuariosBase() {
        logger.info("Creando usuarios base...");
        
        // Admin
        if (usuarioRepository.findByCorreo("admin@unieats.com").isEmpty()) {
            Rol adminRol = rolRepository.findByNombre("ADMIN_PLATAFORMA").orElseThrow();
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("UniEats");
            admin.setCorreo("admin@unieats.com");
            admin.setCedula("0000000000");
            admin.setContrasenaHash(passwordEncoder.encode("admin123"));
            admin.setActivo(true);
            admin.setRoles(Set.of(adminRol));
            usuarioRepository.save(admin);
            logger.info("Admin creado: admin@unieats.com / admin123");
        }

        // Estudiante de prueba
        if (usuarioRepository.findByCorreo("estudiante@unieats.com").isEmpty()) {
            Rol estudianteRol = rolRepository.findByNombre("ESTUDIANTE").orElseThrow();
            Usuario estudiante = new Usuario();
            estudiante.setNombre("Juan Carlos");
            estudiante.setApellido("Pérez López");
            estudiante.setCorreo("estudiante@unieats.com");
            estudiante.setCedula("1234567890");
            estudiante.setContrasenaHash(passwordEncoder.encode("estudiante123"));
            estudiante.setActivo(true);
            estudiante.setRoles(Set.of(estudianteRol));
            usuarioRepository.save(estudiante);
            logger.info("Estudiante creado: estudiante@unieats.com / estudiante123");
        }
    }

    private void crearTiendasCompletas() {
        logger.info("Creando tiendas completas con vendedores...");
        
        String[][] tiendasData = {
            {"Burger House Express", "tienda1@gmail.com", "Las mejores hamburguesas artesanales del campus", "/uploads/logos/21885aee-9879-4a96-a149-e658111d1c24.jpg", "Ana María", "González Rodríguez"},
            {"Pizza Palace", "tienda2@gmail.com", "Pizzas tradicionales e innovadoras para todos los gustos", "/uploads/logos/928c3ae6-49ef-40f5-ab4f-3dcead9c597b.png", "Carlos Eduardo", "Martínez Silva"},
            {"Sushi Master", "tienda3@gmail.com", "Sushi fresco y auténtico preparado por expertos", "/uploads/logos/3ef69b42-0d67-4780-9d65-b7273f4ad64b.png", "María José", "Rodríguez López"},
            {"Café Central", "tienda4@gmail.com", "Café de especialidad y postres caseros irresistibles", "/uploads/logos/5eacd37e-c0a3-4632-bfe9-5b9738f27e30.jpg", "Luis Fernando", "López Muñoz"},
            {"Tierra Querida", "tienda5@gmail.com", "Comida casera tradicional como en casa de la abuela", "/uploads/logos/6cbe7698-366b-4947-b67d-d766281822a9.png", "Rosa Elena", "Muñoz García"},
            {"Las Peludas Gourmet", "tienda6@gmail.com", "Arepas y comida tradicional colombiana de calidad", "/uploads/logos/10022911-8478-4935-bb9b-92a780bb6b26.png", "Jorge Andrés", "Hernández Castro"}
        };

        Rol vendedorRol = rolRepository.findByNombre("VENDEDOR").orElseThrow();
        
        for (String[] tiendaData : tiendasData) {
            String nombreTienda = tiendaData[0];
            String emailVendedor = tiendaData[1];
            
            // Crear vendedor si no existe
            if (usuarioRepository.findByCorreo(emailVendedor).isEmpty()) {
                Usuario vendedor = new Usuario();
                vendedor.setNombre(tiendaData[4]);
                vendedor.setApellido(tiendaData[5]);
                vendedor.setCorreo(emailVendedor);
                vendedor.setCedula(generarCedula());
                vendedor.setContrasenaHash(passwordEncoder.encode("vendedor123"));
                vendedor.setActivo(true);
                vendedor.setRoles(Set.of(vendedorRol));
                vendedor = usuarioRepository.save(vendedor);
                
                // Crear tienda
                Tienda tienda = new Tienda();
                tienda.setNombre(nombreTienda);
                tienda.setDescripcion(tiendaData[2]);
                tienda.setNit(generarNit());
                tienda.setLogoUrl(tiendaData[3]);
                tienda.setEstado(EstadoTienda.ACTIVA);
                tienda.setEstaAbierta(true);
                tienda.setVendedor(vendedor);
                tiendaRepository.save(tienda);
                
                logger.info("Tienda creada: {} - Vendedor: {} / vendedor123", nombreTienda, emailVendedor);
            }
        }
    }

    private void crearProductos() {
        logger.info("Creando productos para todas las tiendas...");
        
        List<Tienda> tiendas = tiendaRepository.findAll();
        
        for (Tienda tienda : tiendas) {
            crearProductosParaTienda(tienda);
        }
    }

    private void crearProductosParaTienda(Tienda tienda) {
        String nombreTienda = tienda.getNombre();
        String[][] productosData = {};
        
        switch (nombreTienda) {
            case "Burger House Express":
                productosData = new String[][]{
                    {"Hamburguesa Clásica", "Carne de res, lechuga fresca, tomate, cebolla y salsa especial", "15000", "/uploads/productos/1b0c616e-3083-4735-a910-8aa53fc7d26f.png", "COMIDA_RAPIDA"},
                    {"Hamburguesa BBQ", "Carne de res, salsa BBQ casera, cebolla caramelizada y queso cheddar", "18000", "/uploads/productos/2623a938-1f48-47ce-8e3b-baf601cfa3a9.jpg", "COMIDA_RAPIDA"},
                    {"Papas Fritas Especiales", "Papas doradas y crujientes con sal de mar", "8000", "/uploads/productos/36209086-e3fe-4d65-9283-c0499403ed88.jpeg", "SNACKS"},
                    {"Combo Familiar", "2 hamburguesas clásicas + papas grandes + 2 bebidas", "35000", "/uploads/productos/3c4bbd5a-e2fc-4969-8f9a-6bfaa5d65f50.jpg", "COMIDA_RAPIDA"}
                };
                break;
            case "Pizza Palace":
                productosData = new String[][]{
                    {"Pizza Margherita", "Salsa de tomate, mozzarella fresca y albahaca", "22000", "/uploads/productos/462870a9-60b3-4402-921c-9547c9e3fa92.jpg", "COMIDA_RAPIDA"},
                    {"Pizza Pepperoni", "Pepperoni premium, mozzarella y salsa de tomate", "25000", "/uploads/productos/66875562-639c-4181-8b11-176352987b49.jpg", "COMIDA_RAPIDA"},
                    {"Pizza Hawaiana", "Jamón, piña tropical y mozzarella derretida", "24000", "/uploads/productos/6f62b13c-bb2b-49a5-acac-2806c5beb53f.jpg", "COMIDA_RAPIDA"},
                    {"Lasaña Casera", "Pasta fresca, carne molida, bechamel y queso gratinado", "28000", "/uploads/productos/7adf58cb-67c4-43a1-8766-2b44c8a0ff0d.jpg", "ALMUERZO"}
                };
                break;
            case "Sushi Master":
                productosData = new String[][]{
                    {"Sushi Roll California", "Cangrejo, aguacate, pepino y sésamo", "32000", "/uploads/productos/7db6fa65-27d8-4562-a04e-994d864ca0e9.jpg", "SALUDABLE"},
                    {"Sashimi Salmón", "Cortes frescos de salmón del Atlántico", "35000", "/uploads/productos/8cacdc8c-a1e7-4d94-989c-1ba3a6ae7a14.jpg", "SALUDABLE"},
                    {"Tempura de Vegetales", "Verduras frescas en tempura crujiente", "18000", "/uploads/productos/98a903cc-7d28-40ba-bd40-d3f182a7eb3b.jpg", "SALUDABLE"},
                    {"Combo Sushi Mixto", "Variedad de rolls, sashimi y nigiri", "45000", "/uploads/productos/9dcf555f-8bfc-4b31-9169-3e2924c9b055.jpg", "SALUDABLE"}
                };
                break;
            case "Café Central":
                productosData = new String[][]{
                    {"Café Americano", "Café negro de origen colombiano 100% arábica", "5000", "/uploads/productos/a1cb3084-dd2f-4247-939a-76f46961d574.jpg", "BEBIDAS"},
                    {"Cappuccino Artesanal", "Espresso doble con leche espumada perfectamente", "7000", "/uploads/productos/bc72a8f7-0eb7-40b5-b575-b53ff95b69ca.png", "BEBIDAS"},
                    {"Cheesecake Frutos Rojos", "Postre cremoso con frutos del bosque frescos", "12000", "/uploads/productos/bf3319f2-cf59-4c8c-ad81-8d28287acfad.jpg", "POSTRES"},
                    {"Croissant Jamón y Queso", "Croissant francés relleno, recién horneado", "9000", "/uploads/productos/c4dd0d4b-c88f-4849-b3d0-a9eb31ac24bf.jpg", "DESAYUNO"}
                };
                break;
            case "Tierra Querida":
                productosData = new String[][]{
                    {"Bandeja Paisa Completa", "Fríjoles, arroz, carne molida, chicharrón, huevo y aguacate", "35000", "/uploads/productos/c6dc896b-affa-4193-95a7-f7f25ac7a044.jpeg", "ALMUERZO"},
                    {"Sancocho de Gallina", "Sopa tradicional con gallina criolla y verduras frescas", "25000", "/uploads/productos/cb0d6d92-3c92-4fa1-b87b-012ad4f3639b.jpg", "ALMUERZO"},
                    {"Arepa con Queso", "Arepa maíz blanco dorada con queso campesino", "8000", "/uploads/productos/ed33b3b2-9fbd-4cb0-8f8e-b0f7681957fc.jpg", "DESAYUNO"},
                    {"Jugos Naturales", "Variedad de frutas tropicales frescas del día", "6000", "/uploads/productos/eef71fea-2bf8-43eb-9450-49a59d4648b1.jpg", "BEBIDAS"}
                };
                break;
            case "Las Peludas Gourmet":
                productosData = new String[][]{
                    {"Arepa Peluda Especial", "Arepa con carne desmechada, aguacate y queso", "15000", "/uploads/productos/ef3095c0-d18b-4d75-94ea-645b3d8f7205.jpg", "COMIDA_RAPIDA"},
                    {"Arepa de Pollo", "Pollo desmechado guisado con verduras frescas", "13000", "/uploads/productos/f5a69102-79b1-4086-8829-9dafe3d352e9.jpg", "COMIDA_RAPIDA"},
                    {"Arepa Vegetariana", "Verduras salteadas, queso fresco y aguacate", "12000", "/uploads/productos/03339b00-5e92-4799-b952-ab57805962b7.jpg", "SALUDABLE"},
                    {"Mazamorra con Panela", "Postre tradicional colombiano con leche y canela", "8000", "/uploads/productos/1ecd9d6e-abc9-4cea-af94-ce9942853c83.jpg", "POSTRES"}
                };
                break;
        }
        
        for (String[] productoData : productosData) {
            // Verificar si ya existe este producto en esta tienda
            List<Producto> productosExistentes = productoRepository.findByTienda(tienda);
            boolean existe = productosExistentes.stream()
                .anyMatch(p -> p.getNombre().equals(productoData[0]));
                
            if (!existe) {
                Producto producto = new Producto();
                producto.setNombre(productoData[0]);
                producto.setDescripcion(productoData[1]);
                producto.setPrecio(new BigDecimal(productoData[2]));
                producto.setDisponible(true);
                producto.setImagenUrl(productoData[3]);
                producto.setTienda(tienda);
                
                // Asignar clasificación si está disponible
                if (productoData.length > 4) {
                    try {
                        ClasificacionProducto clasificacion = ClasificacionProducto.valueOf(productoData[4]);
                        producto.setClasificacion(clasificacion);
                    } catch (IllegalArgumentException e) {
                        logger.warn("Clasificación inválida para producto {}: {}", productoData[0], productoData[4]);
                        producto.setClasificacion(ClasificacionProducto.COMIDA_RAPIDA); // Default
                    }
                }
                
                productoRepository.save(producto);
            }
        }
        
        logger.info("Productos creados para: {}", nombreTienda);
    }

    private String generarCedula() {
        return String.valueOf(1000000000L + (long)(Math.random() * 1000000000L));
    }

    private String generarNit() {
        return String.valueOf(900000000L + (long)(Math.random() * 100000000L));
    }
    
    // === NUEVAS FUNCIONES PARA MACHINE LEARNING ===
    
    /**
     * Crea dos usuarios especializados con preferencias muy diferentes:
     * - Usuario "Bebidas": Solo consume bebidas y postres
     * - Usuario "Almuerzo": Solo consume almuerzos y comida fuerte
     */
    private void crearUsuariosEspecializados() {
        logger.info("Creando usuarios especializados para ML...");
        
        Rol estudianteRol = rolRepository.findByNombre("ESTUDIANTE").orElseThrow();
        
        // Usuario especializado en BEBIDAS
        if (usuarioRepository.findByCorreo("bebidas@unieats.com").isEmpty()) {
            Usuario usuarioBebidas = new Usuario();
            usuarioBebidas.setNombre("María Elena");
            usuarioBebidas.setApellido("Café López");
            usuarioBebidas.setCorreo("bebidas@unieats.com");
            usuarioBebidas.setCedula("1111111111");
            usuarioBebidas.setContrasenaHash(passwordEncoder.encode("bebidas123"));
            usuarioBebidas.setActivo(true);
            usuarioBebidas.setRoles(Set.of(estudianteRol));
            usuarioRepository.save(usuarioBebidas);
            logger.info("Usuario especializado en BEBIDAS creado: bebidas@unieats.com / bebidas123");
        }

        // Usuario especializado en ALMUERZOS
        if (usuarioRepository.findByCorreo("almuerzo@unieats.com").isEmpty()) {
            Usuario usuarioAlmuerzo = new Usuario();
            usuarioAlmuerzo.setNombre("Carlos Alberto");
            usuarioAlmuerzo.setApellido("Comida Fuerte");
            usuarioAlmuerzo.setCorreo("almuerzo@unieats.com");
            usuarioAlmuerzo.setCedula("2222222222");
            usuarioAlmuerzo.setContrasenaHash(passwordEncoder.encode("almuerzo123"));
            usuarioAlmuerzo.setActivo(true);
            usuarioAlmuerzo.setRoles(Set.of(estudianteRol));
            usuarioRepository.save(usuarioAlmuerzo);
            logger.info("Usuario especializado en ALMUERZOS creado: almuerzo@unieats.com / almuerzo123");
        }
    }
    
    /**
     * Crea comportamientos ML simulados para entrenar el sistema
     */
    private void crearComportamientosML() {
        logger.info("Creando comportamientos ML especializados...");
        
        try {
            Usuario usuarioBebidas = usuarioRepository.findByCorreo("bebidas@unieats.com").orElse(null);
            Usuario usuarioAlmuerzo = usuarioRepository.findByCorreo("almuerzo@unieats.com").orElse(null);
            
            if (usuarioBebidas == null || usuarioAlmuerzo == null) {
                logger.warn("No se encontraron usuarios especializados, saltando creación de comportamientos ML");
                return;
            }
            
            // Crear comportamientos para usuario BEBIDAS (15-20 interacciones)
            crearComportamientosBebidas(usuarioBebidas);
            
            // Crear comportamientos para usuario ALMUERZOS (15-20 interacciones)
            crearComportamientosAlmuerzos(usuarioAlmuerzo);
            
            logger.info("Comportamientos ML creados exitosamente");
            
        } catch (Exception e) {
            logger.error("❌ Error creando comportamientos ML: {}", e.getMessage());
        }
    }
    
    private void crearComportamientosBebidas(Usuario usuario) {
        logger.info("Creando comportamientos especializados en BEBIDAS para usuario: {}", usuario.getCorreo());
        
        // Obtener productos de bebidas y postres
        List<Producto> productosBebidas = productoRepository.findAll().stream()
            .filter(p -> p.getClasificacion() == ClasificacionProducto.BEBIDAS)
            .toList();
            
        List<Producto> productosPostres = productoRepository.findAll().stream()
            .filter(p -> p.getClasificacion() == ClasificacionProducto.POSTRES)
            .toList();
        
        Random random = new Random();
        
        // Crear 12-15 comportamientos de BEBIDAS (alta frecuencia)
        for (int i = 0; i < 14; i++) {
            if (!productosBebidas.isEmpty()) {
                Producto producto = productosBebidas.get(random.nextInt(productosBebidas.size()));
                crearComportamiento(usuario, producto, random.nextInt(5) + 3); // Frecuencia 3-7
            }
        }
        
        // Crear 3-5 comportamientos de POSTRES (frecuencia media)
        for (int i = 0; i < 4; i++) {
            if (!productosPostres.isEmpty()) {
                Producto producto = productosPostres.get(random.nextInt(productosPostres.size()));
                crearComportamiento(usuario, producto, random.nextInt(3) + 2); // Frecuencia 2-4
            }
        }
        
        logger.info("{} comportamientos de BEBIDAS creados", 18);
    }
    
    private void crearComportamientosAlmuerzos(Usuario usuario) {
        logger.info("Creando comportamientos especializados en ALMUERZOS para usuario: {}", usuario.getCorreo());
        
        // Obtener productos de almuerzo y comida rápida
        List<Producto> productosAlmuerzo = productoRepository.findAll().stream()
            .filter(p -> p.getClasificacion() == ClasificacionProducto.ALMUERZO)
            .toList();
            
        List<Producto> productosComidaRapida = productoRepository.findAll().stream()
            .filter(p -> p.getClasificacion() == ClasificacionProducto.COMIDA_RAPIDA)
            .toList();
            
        List<Producto> productosSaludable = productoRepository.findAll().stream()
            .filter(p -> p.getClasificacion() == ClasificacionProducto.SALUDABLE)
            .toList();
        
        Random random = new Random();
        
        // Crear 10-12 comportamientos de ALMUERZO (alta frecuencia)
        for (int i = 0; i < 11; i++) {
            if (!productosAlmuerzo.isEmpty()) {
                Producto producto = productosAlmuerzo.get(random.nextInt(productosAlmuerzo.size()));
                crearComportamiento(usuario, producto, random.nextInt(4) + 4); // Frecuencia 4-7
            }
        }
        
        // Crear 4-6 comportamientos de COMIDA_RAPIDA (frecuencia media)
        for (int i = 0; i < 5; i++) {
            if (!productosComidaRapida.isEmpty()) {
                Producto producto = productosComidaRapida.get(random.nextInt(productosComidaRapida.size()));
                crearComportamiento(usuario, producto, random.nextInt(3) + 2); // Frecuencia 2-4
            }
        }
        
        // Crear 2-3 comportamientos de SALUDABLE (frecuencia baja)
        for (int i = 0; i < 2; i++) {
            if (!productosSaludable.isEmpty()) {
                Producto producto = productosSaludable.get(random.nextInt(productosSaludable.size()));
                crearComportamiento(usuario, producto, random.nextInt(2) + 1); // Frecuencia 1-2
            }
        }
        
        logger.info("{} comportamientos de ALMUERZOS creados", 18);
    }
    
    private void crearComportamiento(Usuario usuario, Producto producto, int frecuencia) {
        try {
            UsuarioComportamiento comportamiento = new UsuarioComportamiento();
            comportamiento.setUsuarioId(usuario.getId());
            comportamiento.setProductoId(producto.getId());
            comportamiento.setNombreProducto(producto.getNombre());
            comportamiento.setCategoriaProducto(producto.getClasificacion().toString());
            comportamiento.setTiendaId(producto.getTienda().getId());
            comportamiento.setNombreTienda(producto.getTienda().getNombre());
            comportamiento.setFrecuenciaCompra(frecuencia);
            comportamiento.setTotalGastado(producto.getPrecio().multiply(BigDecimal.valueOf(frecuencia)));
            comportamiento.setPromedioCantidad(BigDecimal.valueOf(frecuencia / 2.0));
            comportamiento.setEsFavorito(frecuencia > 5);
            comportamiento.setPuntuacionAfinidad(BigDecimal.valueOf(frecuencia * 10.0));
            comportamiento.setHoraPreferidaCompra(12); // Hora del almuerzo por defecto
            comportamiento.setUltimaCompra(LocalDateTime.now().minusDays(1));
            comportamiento.setFechaCreacion(LocalDateTime.now());
            comportamiento.setFechaActualizacion(LocalDateTime.now());
            
            usuarioComportamientoRepository.save(comportamiento);
            
            logger.debug("Comportamiento creado: Usuario {}, Producto {} ({}), Frecuencia: {}", 
                usuario.getId(), producto.getNombre(), producto.getClasificacion(), frecuencia);
        } catch (Exception e) {
            logger.warn("Error creando comportamiento para producto {}: {}", producto.getNombre(), e.getMessage());
        }
    }}