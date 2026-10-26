package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.model.Usuario;
import com.patitasfelices.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD de usuarios (relacionados con roles). */
@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private void cargarListado(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("roles", usuarioService.obtenerRoles());
        model.addAttribute("activeMenu", "usuarios");
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        cargarListado(model);
        model.addAttribute("usuarioNuevo", new Usuario());
        return "admin-usuarios";
    }

    // REGISTRAR (abre el modal vacío)
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        cargarListado(model);
        model.addAttribute("usuarioNuevo", new Usuario());
        model.addAttribute("abrirModal", true);
        return "admin-usuarios";
    }

    // EDITAR (abre el modal con los datos cargados)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el usuario.");
            return "redirect:/admin/usuarios";
        }
        cargarListado(model);
        model.addAttribute("usuarioNuevo", usuario);
        model.addAttribute("abrirModal", true);
        return "admin-usuarios";
    }

    // GUARDAR (registrar o actualizar según tenga id)
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuarioNuevo") Usuario usuario, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {

        boolean esNuevo = usuario.getId() == null;

        if (esNuevo && (usuario.getPassword() == null || usuario.getPassword().isBlank())) {
            result.rejectValue("password", "password.requerida", "La contraseña es obligatoria");
        }
        if (usuario.getRol() == null || usuario.getRol().getId() == null) {
            result.rejectValue("rol", "rol.requerido", "Debes seleccionar un rol");
        }
        if (usuarioService.emailEnUso(usuario.getEmail(), usuario.getId())) {
            result.rejectValue("email", "email.duplicado", "Este correo ya está registrado");
        }

        if (result.hasErrors()) {
            cargarListado(model);
            model.addAttribute("abrirModal", true);
            return "admin-usuarios";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("exito",
                    esNuevo ? "Usuario registrado correctamente." : "Usuario actualizado correctamente.");
        } catch (Exception e) {
            log.error("Error al guardar el usuario", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo guardar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (id.equals(session.getAttribute("usuarioId"))) {
            redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propio usuario mientras estás conectado.");
            return "redirect:/admin/usuarios";
        }
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("exito", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            log.error("Error al eliminar el usuario", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }
}
