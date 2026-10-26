package com.patitasfelices.demo.repository;

import com.patitasfelices.demo.dto.MascotaListadoDTO;
import com.patitasfelices.demo.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    // Tu método original intacto
    boolean existsByCategoriaId(Long categoriaId);

    // Para las listas desplegables en cascada (filtra mascotas por categoría)
    List<Mascota> findByCategoriaId(Long categoriaId);

    // Consulta multitabla con DTO (Mascota + Categoria + Conteo de Solicitudes)
    @Query("SELECT new com.patitasfelices.demo.dto.MascotaListadoDTO(" +
           "m.id, m.nombre, m.raza, m.edad, m.sexo, m.tamano, m.peso, m.personalidad, m.descripcion, m.imagen, " +
           "c.id, COALESCE(c.nombre, 'Sin categoría'), COUNT(s.id)) " +
           "FROM Mascota m " +
           "LEFT JOIN m.categoria c " +
           "LEFT JOIN SolicitudAdopcion s ON s.mascota.id = m.id " +
           "GROUP BY m.id, m.nombre, m.raza, m.edad, m.sexo, m.tamano, m.peso, m.personalidad, m.descripcion, m.imagen, c.id, c.nombre")
    List<MascotaListadoDTO> listarConCategoriaYSolicitudes();
}