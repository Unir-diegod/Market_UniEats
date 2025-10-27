package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.dto.AnuncioDTO;
import com.remington.unieats.marketplace.model.entity.Anuncio;
import com.remington.unieats.marketplace.model.entity.AnuncioPersonalizado;
import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.DetallePedido;
import com.remington.unieats.marketplace.model.entity.Pedido;
import com.remington.unieats.marketplace.model.entity.Producto;
import com.remington.unieats.marketplace.model.entity.Segmento;
import com.remington.unieats.marketplace.model.entity.Tienda;
import com.remington.unieats.marketplace.model.entity.TipoAnuncio;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.repository.AnuncioPersonalizadoRepository;
import com.remington.unieats.marketplace.model.repository.AnuncioRepository;
import com.remington.unieats.marketplace.model.repository.ClienteRepository;
import com.remington.unieats.marketplace.model.repository.DetallePedidoRepository;
import com.remington.unieats.marketplace.model.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de personalización de anuncios
 * Implementa el algoritmo de scoring para mostrar anuncios relevantes a cada usuario
 */
@Service
public class AnuncioPersonalizacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(AnuncioPersonalizacionService.class);
    
    @Autowired
    private AnuncioRepository anuncioRepository;
    
    @Autowired
    private AnuncioPersonalizadoRepository anuncioPersonalizadoRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Obtiene anuncios personalizados para un usuario
     * @param usuario Usuario actual
     * @param limite Número máximo de anuncios a retornar
     * @return Lista de anuncios ordenados por relevancia
     */
    @Transactional(readOnly = true)
    public List<AnuncioDTO> obtenerAnunciosPersonalizados(Usuario usuario, int limite) {
        logger.info("Obteniendo anuncios personalizados para usuario: {} {}", usuario.getNombre(), usuario.getApellido());
        
        // 1. Obtener todos los anuncios vigentes
        LocalDateTime ahora = LocalDateTime.now();
        List<Anuncio> anunciosVigentes = anuncioRepository.findAnunciosVigentes(ahora);
        
        if (anunciosVigentes.isEmpty()) {
            logger.warn("No hay anuncios vigentes en este momento");
            return new ArrayList<>();
        }
        
        // 2. Calcular score de relevancia para cada anuncio
        Map<Anuncio, ResultadoScoring> scoreMap = new HashMap<>();
        for (Anuncio anuncio : anunciosVigentes) {
            ResultadoScoring resultado = calcularScoreRelevancia(anuncio, usuario);
            scoreMap.put(anuncio, resultado);
        }
        
        // 3. Ordenar por score (mayor a menor)
        List<Map.Entry<Anuncio, ResultadoScoring>> sortedList = scoreMap.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue().score, e1.getValue().score))
                .limit(limite)
                .collect(Collectors.toList());
        
        // 4. Convertir a DTOs
        List<AnuncioDTO> result = new ArrayList<>();
        for (Map.Entry<Anuncio, ResultadoScoring> entry : sortedList) {
            AnuncioDTO dto = convertirADTO(entry.getKey(), entry.getValue());
            result.add(dto);
        }
        
        logger.info("Se obtuvieron {} anuncios personalizados", result.size());
        return result;
    }
    
    /**
     * Calcula el score de relevancia de un anuncio para un usuario
     * ALGORITMO DE SCORING:
     * - Vendedor favorito: +30 puntos
     * - Categoría comprada antes: +25 puntos
     * - Segmento de campaña: +20 puntos
     * - Producto nuevo: +15 puntos
     * - Es promoción: +10 puntos
     * - Prioridad del anuncio: +5 puntos por cada nivel de prioridad
     */
    private ResultadoScoring calcularScoreRelevancia(Anuncio anuncio, Usuario usuario) {
        double score = 0.0;
        List<String> motivos = new ArrayList<>();
        
        try {
            // 1. Verificar si es del vendedor favorito (+30 puntos)
            if (anuncio.getTienda() != null) {
                List<Pedido> pedidosUsuario = pedidoRepository.findAll().stream()
                        .filter(p -> p.getComprador() != null && 
                                    p.getComprador().getId().equals(usuario.getId()))
                        .collect(Collectors.toList());
                
                // Contar pedidos por tienda
                Map<Integer, Long> pedidosPorTienda = pedidosUsuario.stream()
                        .collect(Collectors.groupingBy(p -> p.getTienda().getId(), Collectors.counting()));
                
                if (!pedidosPorTienda.isEmpty()) {
                    Integer tiendaFavorita = pedidosPorTienda.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .get().getKey();
                    
                    if (anuncio.getTienda().getId().equals(tiendaFavorita)) {
                        score += 30;
                        motivos.add("Tu vendedor favorito");
                    }
                }
            }
            
            // 2. Verificar si la categoría ha sido comprada antes (+25 puntos)
            if (anuncio.getCategoria() != null) {
                List<DetallePedido> detalles = obtenerDetallesPedidosUsuario(usuario);
                Set<String> categoriasCompradas = detalles.stream()
                        .map(d -> d.getProducto().getClasificacion())
                        .filter(Objects::nonNull)
                        .map(clasificacion -> clasificacion.name())
                        .collect(Collectors.toSet());
                
                if (categoriasCompradas.contains(anuncio.getCategoria())) {
                    score += 25;
                    motivos.add("Compraste esto antes");
                }
            }
            
            // 3. Verificar si el usuario está en el segmento de la campaña (+20 puntos)
            if (anuncio.getCampana() != null && anuncio.getCampana().getSegmento() != null) {
                Optional<Cliente> clienteOpt = clienteRepository.findByUsuarioId(usuario.getId());
                if (clienteOpt.isPresent()) {
                    Cliente cliente = clienteOpt.get();
                    // Verificar si el cliente cumple los criterios del segmento
                    if (clientePerteneceASegmento(cliente, anuncio.getCampana().getSegmento())) {
                        score += 20;
                        motivos.add("Especial para ti");
                    }
                }
            }
            
            // 4. Verificar si es un producto que nunca ha visto (+15 puntos)
            if (anuncio.getProducto() != null) {
                List<DetallePedido> detalles = obtenerDetallesPedidosUsuario(usuario);
                boolean yaComproProducto = detalles.stream()
                        .anyMatch(d -> d.getProducto().getId().equals(anuncio.getProducto().getId()));
                
                if (!yaComproProducto) {
                    score += 15;
                    motivos.add("Nuevo para ti");
                }
            }
            
            // 5. Verificar si es una promoción (+10 puntos)
            if (anuncio.getTipo() == TipoAnuncio.PROMOCION) {
                score += 10;
                motivos.add("¡Promoción especial!");
            }
            
            // 6. Añadir puntos por prioridad del anuncio (0-50 puntos, max prioridad 10)
            if (anuncio.getPrioridad() != null) {
                score += anuncio.getPrioridad() * 5;
            }
            
        } catch (Exception e) {
            logger.error("Error calculando score para anuncio {}: {}", anuncio.getId(), e.getMessage());
        }
        
        return new ResultadoScoring(score, motivos);
    }
    
    /**
     * Verifica si un cliente pertenece a un segmento
     */
    private boolean clientePerteneceASegmento(Cliente cliente, Segmento segmento) {
        try {
            // Verificar si el cliente está directamente en el segmento
            if (segmento.getClientes() != null && !segmento.getClientes().isEmpty()) {
                return segmento.getClientes().stream()
                        .anyMatch(c -> c.getId().equals(cliente.getId()));
            }
            
            // Si el segmento no tiene clientes asignados, retornar true (segmento abierto)
            return true;
        } catch (Exception e) {
            logger.error("Error verificando pertenencia a segmento: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene todos los detalles de pedidos de un usuario
     */
    private List<DetallePedido> obtenerDetallesPedidosUsuario(Usuario usuario) {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll().stream()
                    .filter(p -> p.getComprador() != null && 
                                p.getComprador().getId().equals(usuario.getId()))
                    .collect(Collectors.toList());
            
            List<DetallePedido> detalles = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                List<DetallePedido> detallesPedido = detallePedidoRepository.findAll().stream()
                        .filter(d -> d.getPedido().getId().equals(pedido.getId()))
                        .collect(Collectors.toList());
                detalles.addAll(detallesPedido);
            }
            return detalles;
        } catch (Exception e) {
            logger.error("Error obteniendo detalles de pedidos: {}", e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Convierte un Anuncio a DTO
     */
    private AnuncioDTO convertirADTO(Anuncio anuncio, ResultadoScoring resultado) {
        AnuncioDTO dto = new AnuncioDTO();
        dto.setId(anuncio.getId());
        dto.setTitulo(anuncio.getTitulo());
        dto.setDescripcion(anuncio.getDescripcion());
        dto.setImagenUrl(anuncio.getImagenUrl());
        dto.setUrlDestino(anuncio.getUrlDestino());
        dto.setTipo(anuncio.getTipo().name());
        dto.setScoreRelevancia(resultado.score);
        dto.setMotivoPersonalizacion(String.join(", ", resultado.motivos));
        dto.setCategoria(anuncio.getCategoria());
        dto.setPrioridad(anuncio.getPrioridad());
        
        // Información del producto
        if (anuncio.getProducto() != null) {
            dto.setProductoId(Long.valueOf(anuncio.getProducto().getId()));
            dto.setProductoNombre(anuncio.getProducto().getNombre());
            dto.setProductoPrecio(anuncio.getProducto().getPrecio().doubleValue());
        }
        
        // Información de la tienda
        if (anuncio.getTienda() != null) {
            dto.setTiendaId(Long.valueOf(anuncio.getTienda().getId()));
            dto.setTiendaNombre(anuncio.getTienda().getNombre());
            dto.setTiendaLogo(anuncio.getTienda().getLogoUrl());
        }
        
        return dto;
    }
    
    /**
     * Registra una impresión de anuncio
     */
    @Transactional
    public void registrarImpresion(Long anuncioId, Usuario usuario, Integer posicion) {
        try {
            Optional<Anuncio> anuncioOpt = anuncioRepository.findById(anuncioId);
            if (!anuncioOpt.isPresent()) {
                logger.warn("Anuncio {} no encontrado para registrar impresión", anuncioId);
                return;
            }
            
            Anuncio anuncio = anuncioOpt.get();
            
            // Incrementar contador de impresiones en el anuncio
            anuncio.setImpresiones(anuncio.getImpresiones() + 1);
            anuncioRepository.save(anuncio);
            
            // Crear registro personalizado
            ResultadoScoring scoring = calcularScoreRelevancia(anuncio, usuario);
            
            AnuncioPersonalizado personalizado = new AnuncioPersonalizado();
            personalizado.setUsuario(usuario);
            personalizado.setAnuncio(anuncio);
            personalizado.setFechaMostrado(LocalDateTime.now());
            personalizado.setScoreRelevancia(scoring.score);
            personalizado.setPosicion(posicion);
            personalizado.setClickeado(false);
            personalizado.setConvertido(false);
            
            anuncioPersonalizadoRepository.save(personalizado);
            
            logger.info("Impresión registrada para anuncio {} usuario {} {}", anuncioId, usuario.getNombre(), usuario.getApellido());
        } catch (Exception e) {
            logger.error("Error registrando impresión: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Registra un clic en un anuncio
     */
    @Transactional
    public void registrarClic(Long anuncioId, Usuario usuario) {
        try {
            Optional<Anuncio> anuncioOpt = anuncioRepository.findById(anuncioId);
            if (!anuncioOpt.isPresent()) {
                logger.warn("Anuncio {} no encontrado para registrar clic", anuncioId);
                return;
            }
            
            Anuncio anuncio = anuncioOpt.get();
            
            // Incrementar contador de clics
            anuncio.setClics(anuncio.getClics() + 1);
            anuncioRepository.save(anuncio);
            
            // Actualizar registros personalizados
            List<AnuncioPersonalizado> registros = anuncioPersonalizadoRepository
                    .findByUsuarioIdAndAnuncioId(usuario.getId().longValue(), anuncioId);
            
            if (!registros.isEmpty()) {
                // Actualizar el más reciente
                AnuncioPersonalizado ultimo = registros.get(registros.size() - 1);
                ultimo.registrarClic();
                anuncioPersonalizadoRepository.save(ultimo);
            }
            
            logger.info("Clic registrado para anuncio {} usuario {} {}", anuncioId, usuario.getNombre(), usuario.getApellido());
        } catch (Exception e) {
            logger.error("Error registrando clic: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Clase interna para almacenar resultados de scoring
     */
    private static class ResultadoScoring {
        double score;
        List<String> motivos;
        
        ResultadoScoring(double score, List<String> motivos) {
            this.score = score;
            this.motivos = motivos;
        }
    }
}
