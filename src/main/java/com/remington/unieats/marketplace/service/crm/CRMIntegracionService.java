package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.Pedido;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio de integración entre el sistema de pedidos y el CRM
 */
@Service
public class CRMIntegracionService {

    private static final Logger logger = LoggerFactory.getLogger(CRMIntegracionService.class);

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstadisticasCRMService estadisticasService;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Sincronizar usuario con CRM al momento de registro
     */
    @Transactional
    public void sincronizarNuevoUsuario(Usuario usuario) {
        try {
            // Verificar si el usuario ya tiene perfil de cliente
            Optional<Cliente> clienteExistente = clienteRepository.findByUsuario(usuario);
            
            if (clienteExistente.isEmpty()) {
                // Crear perfil de cliente automáticamente
                Cliente cliente = clienteService.crearPerfilCliente(usuario);
                
                // Registrar interacción de registro
                estadisticasService.registrarInteraccion(
                    cliente.getId(),
                    "REGISTRO",
                    "WEB",
                    "Nuevo usuario registrado en la plataforma",
                    null
                );
                
                logger.info("Perfil de CRM creado para usuario: {}", usuario.getCorreo());
            }
        } catch (Exception e) {
            logger.error("Error al sincronizar usuario con CRM: {}", e.getMessage());
        }
    }

    /**
     * Actualizar perfil de cliente cuando se realiza un pedido
     */
    @Transactional
    public void procesarNuevoPedido(Pedido pedido) {
        try {
            Usuario comprador = pedido.getComprador();
            
            // Obtener o crear perfil de cliente
            Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(comprador);
            Cliente cliente;
            
            if (clienteOpt.isEmpty()) {
                cliente = clienteService.crearPerfilCliente(comprador);
            } else {
                cliente = clienteOpt.get();
            }

            // Calcular total del pedido
            Double totalPedido = pedido.getDetalles().stream()
                    .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario().doubleValue())
                    .sum();

            // Actualizar estadísticas de compra
            clienteService.actualizarEstadisticasCompra(cliente.getId(), totalPedido);

            // Registrar interacción de compra
            estadisticasService.registrarInteraccion(
                cliente.getId(),
                "COMPRA",
                "WEB",
                "Pedido #" + pedido.getId() + " realizado",
                totalPedido
            );

            logger.info("Pedido #{} procesado en CRM para cliente: {}", 
                       pedido.getId(), comprador.getCorreo());
            
        } catch (Exception e) {
            logger.error("Error al procesar pedido en CRM: {}", e.getMessage());
        }
    }

    /**
     * Registrar visita de usuario a un producto
     */
    @Transactional
    public void registrarVisitaProducto(Usuario usuario, Integer productoId, String nombreProducto) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(usuario);
            
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                
                estadisticasService.registrarInteraccion(
                    cliente.getId(),
                    "VISITA",
                    "WEB",
                    "Visita al producto: " + nombreProducto,
                    null
                );
            }
        } catch (Exception e) {
            logger.error("Error al registrar visita a producto: {}", e.getMessage());
        }
    }

    /**
     * Registrar cuando un usuario agrega un producto al carrito pero no completa la compra
     */
    @Transactional
    public void registrarCarritoAbandonado(Usuario usuario, Double valorCarrito) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(usuario);
            
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                
                estadisticasService.registrarInteraccion(
                    cliente.getId(),
                    "CARRITO_ABANDONADO",
                    "WEB",
                    "Carrito abandonado con valor: $" + valorCarrito,
                    valorCarrito
                );
                
                logger.info("Carrito abandonado registrado para: {}", usuario.getCorreo());
            }
        } catch (Exception e) {
            logger.error("Error al registrar carrito abandonado: {}", e.getMessage());
        }
    }

    /**
     * Actualizar categoría favorita del cliente
     */
    @Transactional
    public void actualizarCategoriaFavorita(Usuario usuario, String categoria) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findByUsuario(usuario);
            
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                cliente.setCategoriaFavorita(categoria);
                clienteRepository.save(cliente);
                
                logger.info("Categoría favorita actualizada para: {} -> {}", 
                           usuario.getCorreo(), categoria);
            }
        } catch (Exception e) {
            logger.error("Error al actualizar categoría favorita: {}", e.getMessage());
        }
    }

    /**
     * Calcular y actualizar frecuencia de compra
     */
    @Transactional
    public void calcularFrecuenciaCompra(Cliente cliente) {
        try {
            if (cliente.getNumeroPedidos() < 2) {
                cliente.setFrecuenciaCompra("NUEVO");
                return;
            }

            // Lógica simple para determinar frecuencia
            // En producción, esto debería analizar las fechas entre pedidos
            int pedidos = cliente.getNumeroPedidos();
            
            if (pedidos >= 20) {
                cliente.setFrecuenciaCompra("DIARIA");
            } else if (pedidos >= 10) {
                cliente.setFrecuenciaCompra("SEMANAL");
            } else if (pedidos >= 5) {
                cliente.setFrecuenciaCompra("QUINCENAL");
            } else {
                cliente.setFrecuenciaCompra("MENSUAL");
            }
            
            clienteRepository.save(cliente);
        } catch (Exception e) {
            logger.error("Error al calcular frecuencia de compra: {}", e.getMessage());
        }
    }
}
