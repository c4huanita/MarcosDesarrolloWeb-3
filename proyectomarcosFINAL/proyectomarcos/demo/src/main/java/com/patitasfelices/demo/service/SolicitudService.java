package com.patitasfelices.demo.service;

import com.patitasfelices.demo.dto.SolicitudListadoDTO;
import com.patitasfelices.demo.model.Mascota;
import com.patitasfelices.demo.model.SolicitudAdopcion;
import com.patitasfelices.demo.model.Usuario;
import com.patitasfelices.demo.repository.MascotaRepository;
import com.patitasfelices.demo.repository.SolicitudRepository;
import com.patitasfelices.demo.repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitudService {

    public static final List<String> ESTADOS = List.of("Pendiente", "En revisión", "Aprobada", "Rechazada");

    private final SolicitudRepository solicitudRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, MascotaRepository mascotaRepository,
            UsuarioRepository usuarioRepository) {
        this.solicitudRepository = solicitudRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<SolicitudAdopcion> obtenerTodas() {
        return solicitudRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<SolicitudAdopcion> obtenerRecientes() {
        return solicitudRepository.findTop5ByOrderByIdDesc();
    }

    public long contar() {
        return solicitudRepository.count();
    }

    public long contarPorEstado(String estado) {
        return solicitudRepository.countByEstado(estado);
    }

    // --- NUEVO MÉTODO PARA LISTADO CON DTO Y FILTRO EN CASCADA ---
    public List<SolicitudListadoDTO> listarFiltradasConDTO(Long categoriaId, Long mascotaId) {
        if (mascotaId != null) {
            return solicitudRepository.listarPorMascotaConDTO(mascotaId);
        }
        if (categoriaId != null) {
            return solicitudRepository.listarPorCategoriaConDTO(categoriaId);
        }
        return solicitudRepository.listarTodasConDTO();
    }
    // -------------------------------------------------------------

    @Transactional
    public void guardarSolicitud(SolicitudAdopcion form, Long mascotaId, Long usuarioId) {
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new IllegalArgumentException("La mascota seleccionada no existe."));

        SolicitudAdopcion solicitud = new SolicitudAdopcion();
        solicitud.setNombreCompleto(form.getNombreCompleto());
        solicitud.setTelefono(form.getTelefono());
        solicitud.setMotivo(form.getMotivo());
        solicitud.setMascota(mascota);
        solicitud.setEstado("Pendiente");
        solicitud.setFechaSolicitud(LocalDate.now());

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            solicitud.setUsuario(usuario);
        }
        solicitudRepository.save(solicitud);
    }

    @Transactional
    public void actualizarEstado(Long id, String estado) {
        if (!ESTADOS.contains(estado)) {
            throw new IllegalArgumentException("Estado no válido.");
        }
        if (solicitudRepository.actualizarEstado(id, estado) == 0) {
            throw new IllegalArgumentException("La solicitud no existe.");
        }
    }

    @Transactional
    public void eliminarSolicitud(Long id) {
        solicitudRepository.deleteById(id);
    }
}