package com.patitasfelices.demo.repository;

import com.patitasfelices.demo.dto.SolicitudListadoDTO;
import com.patitasfelices.demo.model.SolicitudAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<SolicitudAdopcion, Long> {

    long countByEstado(String estado);

    List<SolicitudAdopcion> findTop5ByOrderByIdDesc();

    // Al eliminar un usuario, sus solicitudes se conservan sin dueño
    @Modifying(clearAutomatically = true)
    @Query("update SolicitudAdopcion s set s.usuario = null where s.usuario.id = :usuarioId")
    void desvincularUsuario(@Param("usuarioId") Long usuarioId);

    @Modifying(clearAutomatically = true)
    @Query("update SolicitudAdopcion s set s.estado = :estado where s.id = :id")
    int actualizarEstado(@Param("id") Long id, @Param("estado") String estado);

    // 1. Consulta multitabla con DTO (Solicitudes + Mascotas + Categorias + Usuarios)
    @Query("SELECT new com.patitasfelices.demo.dto.SolicitudListadoDTO(" +
           "s.id, s.nombreCompleto, s.telefono, s.motivo, s.estado, s.fechaSolicitud, " +
           "m.id, COALESCE(m.nombre, 'Sin mascota'), COALESCE(c.nombre, 'Sin categoría'), COALESCE(u.email, 'Público')) " +
           "FROM SolicitudAdopcion s " +
           "LEFT JOIN s.mascota m " +
           "LEFT JOIN m.categoria c " +
           "LEFT JOIN s.usuario u " +
           "ORDER BY s.id DESC")
    List<SolicitudListadoDTO> listarTodasConDTO();

    // 2. Filtro en cascada por Categoría con DTO
    @Query("SELECT new com.patitasfelices.demo.dto.SolicitudListadoDTO(" +
           "s.id, s.nombreCompleto, s.telefono, s.motivo, s.estado, s.fechaSolicitud, " +
           "m.id, m.nombre, c.nombre, COALESCE(u.email, 'Público')) " +
           "FROM SolicitudAdopcion s " +
           "JOIN s.mascota m " +
           "JOIN m.categoria c " +
           "LEFT JOIN s.usuario u " +
           "WHERE c.id = :categoriaId " +
           "ORDER BY s.id DESC")
    List<SolicitudListadoDTO> listarPorCategoriaConDTO(@Param("categoriaId") Long categoriaId);

    // 3. Filtro en cascada por Mascota con DTO
    @Query("SELECT new com.patitasfelices.demo.dto.SolicitudListadoDTO(" +
           "s.id, s.nombreCompleto, s.telefono, s.motivo, s.estado, s.fechaSolicitud, " +
           "m.id, m.nombre, COALESCE(c.nombre, 'Sin categoría'), COALESCE(u.email, 'Público')) " +
           "FROM SolicitudAdopcion s " +
           "JOIN s.mascota m " +
           "LEFT JOIN m.categoria c " +
           "LEFT JOIN s.usuario u " +
           "WHERE m.id = :mascotaId " +
           "ORDER BY s.id DESC")
    List<SolicitudListadoDTO> listarPorMascotaConDTO(@Param("mascotaId") Long mascotaId);
}