package com.remington.unieats.marketplace.service.crm;

import com.remington.unieats.marketplace.dto.crm.ClienteDTO;
import com.remington.unieats.marketplace.model.entity.Cliente;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Crear perfil de cliente para un usuario
     */
    @Transactional
    public Cliente crearPerfilCliente(Usuario usuario) {
        Optional<Cliente> existente = clienteRepository.findByUsuario(usuario);
        if (existente.isPresent()) {
            return existente.get();
        }

        Cliente cliente = new Cliente();
        cliente.setUsuario(usuario);
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setUltimaInteraccion(LocalDateTime.now());
        cliente.setAceptaMarketing(true);
        cliente.setAceptaEmail(true);
        cliente.setAceptaSms(false);
        cliente.setPuntosFidelidad(0);
        cliente.setNivelCliente("BRONCE");
        cliente.setValorTotalCompras(0.0);
        cliente.setNumeroPedidos(0);
        cliente.setTicketPromedio(0.0);

        return clienteRepository.save(cliente);
    }

    /**
     * Obtener cliente por ID de usuario
     */
    public Optional<Cliente> obtenerClientePorUsuarioId(Integer usuarioId) {
        return clienteRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Actualizar estadísticas de compra del cliente
     */
    @Transactional
    public Cliente actualizarEstadisticasCompra(Long clienteId, Double montoCompra) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        cliente.setNumeroPedidos(cliente.getNumeroPedidos() + 1);
        cliente.setValorTotalCompras(cliente.getValorTotalCompras() + montoCompra);
        cliente.setTicketPromedio(cliente.getValorTotalCompras() / cliente.getNumeroPedidos());
        cliente.setUltimaCompra(LocalDateTime.now());
        cliente.setUltimaInteraccion(LocalDateTime.now());

        // Actualizar nivel de cliente basado en valor total de compras
        actualizarNivelCliente(cliente);

        // Actualizar puntos de fidelidad (1 punto por cada 1000 pesos)
        int puntosNuevos = (int) (montoCompra / 1000);
        cliente.setPuntosFidelidad(cliente.getPuntosFidelidad() + puntosNuevos);

        return clienteRepository.save(cliente);
    }

    /**
     * Actualizar nivel del cliente según sus compras
     */
    private void actualizarNivelCliente(Cliente cliente) {
        double valorTotal = cliente.getValorTotalCompras();
        
        if (valorTotal >= 1000000) {
            cliente.setNivelCliente("PLATINUM");
        } else if (valorTotal >= 500000) {
            cliente.setNivelCliente("ORO");
        } else if (valorTotal >= 200000) {
            cliente.setNivelCliente("PLATA");
        } else {
            cliente.setNivelCliente("BRONCE");
        }
    }

    /**
     * Obtener clientes que aceptan marketing
     */
    public List<Cliente> obtenerClientesConMarketing() {
        return clienteRepository.findByAceptaMarketingTrue();
    }

    /**
     * Obtener top clientes por valor de compras
     */
    public List<Cliente> obtenerTopClientes(Double valorMinimo) {
        return clienteRepository.findTopClientesByValorCompras(valorMinimo);
    }

    /**
     * Obtener clientes inactivos (sin compras en los últimos 60 días)
     */
    public List<Cliente> obtenerClientesInactivos() {
        LocalDateTime hace60Dias = LocalDateTime.now().minusDays(60);
        return clienteRepository.findClientesInactivos(hace60Dias);
    }

    /**
     * Obtener clientes activos (con compras en los últimos 30 días)
     */
    public List<Cliente> obtenerClientesActivos() {
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        return clienteRepository.findClientesActivosDesde(hace30Dias);
    }

    /**
     * Obtener clientes frecuentes (con mínimo de pedidos)
     */
    public List<Cliente> obtenerClientesFrecuentes(Integer minPedidos) {
        return clienteRepository.findClientesFrecuentes(minPedidos);
    }

    /**
     * Actualizar preferencias de marketing
     */
    @Transactional
    public Cliente actualizarPreferenciasMarketing(Long clienteId, Boolean aceptaMarketing, 
                                                   Boolean aceptaEmail, Boolean aceptaSms) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (aceptaMarketing != null) cliente.setAceptaMarketing(aceptaMarketing);
        if (aceptaEmail != null) cliente.setAceptaEmail(aceptaEmail);
        if (aceptaSms != null) cliente.setAceptaSms(aceptaSms);

        return clienteRepository.save(cliente);
    }

    /**
     * Convertir entidad a DTO
     */
    public ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setUsuarioId(cliente.getUsuario().getId());
        dto.setNombreCompleto(cliente.getUsuario().getNombre() + " " + cliente.getUsuario().getApellido());
        dto.setCorreo(cliente.getUsuario().getCorreo());
        dto.setTelefono(cliente.getUsuario().getTelefono());
        dto.setValorTotalCompras(cliente.getValorTotalCompras());
        dto.setNumeroPedidos(cliente.getNumeroPedidos());
        dto.setTicketPromedio(cliente.getTicketPromedio());
        dto.setUltimaCompra(cliente.getUltimaCompra());
        dto.setFrecuenciaCompra(cliente.getFrecuenciaCompra());
        dto.setCategoriaFavorita(cliente.getCategoriaFavorita());
        dto.setAceptaMarketing(cliente.getAceptaMarketing());
        dto.setAceptaEmail(cliente.getAceptaEmail());
        dto.setAceptaSms(cliente.getAceptaSms());
        dto.setPuntosFidelidad(cliente.getPuntosFidelidad());
        dto.setNivelCliente(cliente.getNivelCliente());
        dto.setFechaRegistro(cliente.getFechaRegistro());
        dto.setUltimaInteraccion(cliente.getUltimaInteraccion());
        return dto;
    }

    /**
     * Obtener todos los clientes como DTOs
     */
    public List<ClienteDTO> obtenerTodosClientesDTO() {
        return clienteRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener estadísticas generales
     */
    public Double obtenerTicketPromedioGeneral() {
        return clienteRepository.getTicketPromedioGeneral();
    }

    /**
     * Contar clientes nuevos en período
     */
    public Long contarClientesNuevos(LocalDateTime desde) {
        return clienteRepository.countClientesNuevos(desde);
    }
}
