package com.patitasfelices.demo.service;

import com.patitasfelices.demo.model.Rol;
import com.patitasfelices.demo.model.Usuario;
import com.patitasfelices.demo.repository.RolRepository;
import com.patitasfelices.demo.repository.SolicitudRepository;
import com.patitasfelices.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final SolicitudRepository solicitudRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            SolicitudRepository solicitudRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public List<Rol> obtenerRoles() {
        return rolRepository.findAll();
    }

    public Rol obtenerRolPorNombre(String nombre) {
        return rolRepository.findByNombreIgnoreCase(nombre).orElse(null);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public long contar() {
        return usuarioRepository.count();
    }

    /** true si el correo ya lo usa OTRO usuario (idExcluir = el usuario que se está editando). */
    public boolean emailEnUso(String email, Long idExcluir) {
        if (email == null) {
            return false;
        }
        return usuarioRepository.findByEmailIgnoreCase(email)
                .filter(u -> idExcluir == null || !u.getId().equals(idExcluir))
                .isPresent();
    }

    /**
     * Crea un usuario o actualiza uno existente. En una edición, si la contraseña
     * viene vacía se conserva la actual.
     */
    @Transactional
    public void guardarUsuario(Usuario form) {
        Usuario usuario = new Usuario();
        if (form.getId() != null) {
            usuario = usuarioRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("El usuario no existe."));
        }

        usuario.setNombre(form.getNombre());
        usuario.setEmail(form.getEmail());
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            usuario.setPassword(form.getPassword());
        }
        if (form.getRol() != null && form.getRol().getId() != null) {
            usuario.setRol(rolRepository.findById(form.getRol().getId())
                    .orElseThrow(() -> new IllegalArgumentException("El rol seleccionado no existe.")));
        }
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        solicitudRepository.desvincularUsuario(id);
        usuarioRepository.deleteById(id);
    }

    public Usuario validarLogin(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        return usuarioRepository.findByEmailAndPassword(email, password);
    }
}
