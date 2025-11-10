package com.remington.unieats.marketplace.service;

import com.remington.unieats.marketplace.dto.DashboardStatsDTO;
import com.remington.unieats.marketplace.dto.TiendaDetallesDTO;
import com.remington.unieats.marketplace.dto.TiendaPopularDTO;
import com.remington.unieats.marketplace.dto.UsuarioAdminDTO;
import com.remington.unieats.marketplace.model.entity.Rol;
import com.remington.unieats.marketplace.model.entity.Tienda;
import com.remington.unieats.marketplace.model.entity.Usuario;
import com.remington.unieats.marketplace.model.entity.Pedido;
import com.remington.unieats.marketplace.model.enums.EstadoTienda;
import com.remington.unieats.marketplace.model.repository.RolRepository;
import com.remington.unieats.marketplace.model.repository.TiendaRepository;
import com.remington.unieats.marketplace.model.repository.UsuarioRepository;
import com.remington.unieats.marketplace.model.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private TiendaRepository tiendaRepository;
    @Autowired private PedidoRepository pedidoRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalUsuarios(usuarios.size());
        stats.setTotalEstudiantes(usuarios.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getNombre().equals("ESTUDIANTE"))).count());
        stats.setTotalVendedores(usuarios.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getNombre().equals("VENDEDOR"))).count());
        stats.setTotalTiendas(tiendaRepository.count());
        
        // Calcular pedidos por día (últimos 15 días)
        List<Long> pedidosPorDia = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        
        for (int i = 14; i >= 0; i--) {
            LocalDate fecha = hoy.minusDays(i);
            LocalDateTime inicioDia = fecha.atStartOfDay();
            LocalDateTime finDia = fecha.atTime(23, 59, 59);
            
            List<Pedido> pedidosDelDia = pedidoRepository.findAll().stream()
                .filter(p -> p.getFechaCreacion().isAfter(inicioDia) && p.getFechaCreacion().isBefore(finDia))
                .collect(Collectors.toList());
            
            pedidosPorDia.add((long) pedidosDelDia.size());
        }
        
        stats.setPedidosPorDia(pedidosPorDia);
        return stats;
    }

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardarUsuario(UsuarioAdminDTO usuarioDTO) {
        if (usuarioDTO.getRolesIds() == null || usuarioDTO.getRolesIds().isEmpty()) {
            throw new RuntimeException("El usuario debe tener al menos un rol seleccionado.");
        }
        Optional<Usuario> existentePorCorreo = usuarioRepository.findByCorreo(usuarioDTO.getCorreo());
        if (existentePorCorreo.isPresent() && !existentePorCorreo.get().getId().equals(usuarioDTO.getId())) {
            throw new RuntimeException("El correo electrónico ya está en uso por otro usuario.");
        }
        Usuario usuario;
        if (usuarioDTO.getId() != null && usuarioDTO.getId() != 0) {
            usuario = usuarioRepository.findById(usuarioDTO.getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        } else {
            usuario = new Usuario();
            usuario.setFechaCreacion(LocalDateTime.now());
        }
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setCedula(usuarioDTO.getCedula());
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setContrasenaHash(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        Set<Rol> roles = usuarioDTO.getRolesIds().stream()
                .map(rolId -> rolRepository.findById(rolId).orElse(null))
                .filter(rol -> rol != null).collect(Collectors.toSet());
        usuario.setRoles(roles);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void cambiarEstadoUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(!usuario.isActivo());
        usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }
    
    @Override
    public List<Tienda> listarTodasLasTiendas() {
        return tiendaRepository.findAllWithVendedor();
    }

    @Override
    public void aprobarTienda(Integer tiendaId) {
        Tienda tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        tienda.setEstado(EstadoTienda.ACTIVA);
        tiendaRepository.save(tienda);
    }

    @Override
    public void rechazarOInhabilitarTienda(Integer tiendaId) {
        Tienda tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        tienda.setEstado(EstadoTienda.INACTIVA);
        tiendaRepository.save(tienda);
    }

    @Override
    public void reactivarTienda(Integer tiendaId) {
        Tienda tienda = tiendaRepository.findById(tiendaId)
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        tienda.setEstado(EstadoTienda.ACTIVA);
        tiendaRepository.save(tienda);
    }

    @Override
    public Optional<TiendaDetallesDTO> buscarTiendaDetallesPorId(Integer id) {
        return tiendaRepository.findById(id).map(tienda -> {
            TiendaDetallesDTO dto = new TiendaDetallesDTO();
            dto.setId(tienda.getId());
            dto.setNombre(tienda.getNombre());
            dto.setNit(tienda.getNit());
            dto.setDescripcion(tienda.getDescripcion());
            dto.setLogoUrl(tienda.getLogoUrl());
            dto.setEstado(tienda.getEstado().name());
            dto.setFechaCreacion(tienda.getFechaCreacion());
            dto.setNombreVendedor(tienda.getVendedor().getNombre() + " " + tienda.getVendedor().getApellido());
            return dto;
        });
    }

    @Override
    public List<TiendaPopularDTO> obtenerTiendasMasPopulares(int limite) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        
        // Contar pedidos por tienda
        Map<Integer, Long> pedidosPorTienda = new HashMap<>();
        Map<Integer, String> nombresPorTienda = new HashMap<>();
        
        for (Pedido pedido : pedidos) {
            if (pedido.getTienda() != null) {
                Integer tiendaId = pedido.getTienda().getId();
                pedidosPorTienda.put(tiendaId, pedidosPorTienda.getOrDefault(tiendaId, 0L) + 1);
                nombresPorTienda.put(tiendaId, pedido.getTienda().getNombre());
            }
        }
        
        // Convertir a lista de DTOs y ordenar
        List<TiendaPopularDTO> tiendasPopulares = pedidosPorTienda.entrySet().stream()
            .map(entry -> new TiendaPopularDTO(nombresPorTienda.get(entry.getKey()), entry.getValue()))
            .sorted((t1, t2) -> Long.compare(t2.getCantidadPedidos(), t1.getCantidadPedidos()))
            .limit(limite)
            .collect(Collectors.toList());
        
        return tiendasPopulares;
    }
}