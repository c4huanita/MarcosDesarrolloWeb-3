package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.model.Usuario;
import com.patitasfelices.demo.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Login, registro público y cierre de sesión. */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Quien se registra desde el login no puede escoger su id ni su rol
    @InitBinder("usuarioRegistro")
    public void restringirCampos(WebDataBinder binder) {
        binder.setDisallowedFields("id", "rol*");
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuarioRegistro", new Usuario());
        return "login";
    }

    @PostMapping("/registro")
    public String registrarUsuarioPublico(@Valid @ModelAttribute("usuarioRegistro") Usuario usuario,
            BindingResult result, Model model) {

        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            result.rejectValue("password", "password.requerida", "La contraseña es obligatoria");
        }
        if (usuarioService.emailEnUso(usuario.getEmail(), null)) {
            result.rejectValue("email", "email.duplicado", "Este correo ya está registrado");
        }
        if (result.hasErrors()) {
            model.addAttribute("abrirModalRegistro", true);
            return "login";
        }

        // Si el correo termina en @patitas.com es Administrador, sino es Adoptante
        String nombreRol = usuario.getEmail().toLowerCase().endsWith("@patitas.com") ? "Administrador" : "Adoptante";
        usuario.setRol(usuarioService.obtenerRolPorNombre(nombreRol));

        usuarioService.guardarUsuario(usuario);
        return "redirect:/login?registrado";
    }

    @PostMapping("/procesar-login")
    public String procesarLogin(@RequestParam(required = false) String email,
            @RequestParam(required = false) String password, HttpSession session) {

        Usuario usuarioBD = usuarioService.validarLogin(email, password);
        if (usuarioBD == null) {
            return "redirect:/login?error";
        }

        String nombreRol = (usuarioBD.getRol() != null) ? usuarioBD.getRol().getNombre() : "Adoptante";
        session.setAttribute("usuario", usuarioBD.getEmail());
        session.setAttribute("usuarioId", usuarioBD.getId());
        session.setAttribute("rol", nombreRol);

        return nombreRol.equalsIgnoreCase("Administrador") ? "redirect:/admin" : "redirect:/mascotas";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
