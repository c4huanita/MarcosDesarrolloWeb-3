package com.patitasfelices.demo.service;

import com.patitasfelices.demo.model.Categoria;
import com.patitasfelices.demo.repository.CategoriaRepository;
import com.patitasfelices.demo.repository.MascotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final MascotaRepository mascotaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, MascotaRepository mascotaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.mascotaRepository = mascotaRepository;
    }

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public long contar() {
        return categoriaRepository.count();
    }

    /** Registra una categoría nueva o actualiza una existente (si trae id). */
    @Transactional
    public void guardar(Categoria form) {
        Categoria categoria = new Categoria();
        if (form.getId() != null) {
            categoria = categoriaRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("La categoría no existe."));
        }
        categoria.setNombre(form.getNombre());
        categoria.setDescripcion(form.getDescripcion());
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        if (mascotaRepository.existsByCategoriaId(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar: la categoría tiene mascotas asociadas. Reasígnalas o elimínalas primero.");
        }
        categoriaRepository.deleteById(id);
    }
}
