package com.patitasfelices.demo.service;

import com.patitasfelices.demo.dto.MascotaListadoDTO;
import com.patitasfelices.demo.model.Mascota;
import com.patitasfelices.demo.repository.CategoriaRepository;
import com.patitasfelices.demo.repository.MascotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final CategoriaRepository categoriaRepository;

    public MascotaService(MascotaRepository mascotaRepository, CategoriaRepository categoriaRepository) {
        this.mascotaRepository = mascotaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Mascota> obtenerTodas() {
        return mascotaRepository.findAll();
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    public long contar() {
        return mascotaRepository.count();
    }

    // --- NUEVOS MÉTODOS PARA DTO Y DESPLEGABLES EN CASCADA ---

    public List<MascotaListadoDTO> obtenerTodasConDTO() {
        return mascotaRepository.listarConCategoriaYSolicitudes();
    }

    public List<Mascota> obtenerPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return mascotaRepository.findAll();
        }
        return mascotaRepository.findByCategoriaId(categoriaId);
    }

    public List<Mascota> filtrarEnCascada(Long categoriaId, Long mascotaId) {
        if (mascotaId != null) {
            return mascotaRepository.findById(mascotaId).map(List::of).orElse(List.of());
        }
        if (categoriaId != null) {
            return mascotaRepository.findByCategoriaId(categoriaId);
        }
        return mascotaRepository.findAll();
    }

    // ---------------------------------------------------------

    @Transactional
    public void guardar(Mascota form, String rutaImagen) {
        Mascota mascota = new Mascota();
        if (form.getId() != null) {
            mascota = mascotaRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("La mascota no existe."));
        }

        mascota.setNombre(form.getNombre());
        mascota.setRaza(form.getRaza());
        mascota.setEdad(form.getEdad());
        mascota.setSexo(form.getSexo());
        mascota.setTamano(form.getTamano());
        mascota.setPeso(form.getPeso());
        mascota.setPersonalidad(form.getPersonalidad());
        mascota.setDescripcion(form.getDescripcion());
        mascota.setCategoria(categoriaRepository.findById(form.getCategoria().getId())
                .orElseThrow(() -> new IllegalArgumentException("La categoría seleccionada no existe.")));

        if (rutaImagen != null) {
            mascota.setImagen(rutaImagen);
        }
        mascotaRepository.save(mascota);
    }

    @Transactional
    public void eliminar(Long id) {
        mascotaRepository.deleteById(id);
    }
}