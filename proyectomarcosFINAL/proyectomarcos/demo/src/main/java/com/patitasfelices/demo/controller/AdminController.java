package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.service.CategoriaService;
import com.patitasfelices.demo.service.MascotaService;
import com.patitasfelices.demo.service.SolicitudService;
import com.patitasfelices.demo.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Dashboard del panel de administración con datos reales de MySQL. */
@Controller
public class AdminController {

    private final MascotaService mascotaService;
    private final CategoriaService categoriaService;
    private final SolicitudService solicitudService;
    private final UsuarioService usuarioService;

    public AdminController(MascotaService mascotaService, CategoriaService categoriaService,
            SolicitudService solicitudService, UsuarioService usuarioService) {
        this.mascotaService = mascotaService;
        this.categoriaService = categoriaService;
        this.solicitudService = solicitudService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        long totalSolicitudes = solicitudService.contar();

        model.addAttribute("totalMascotas", mascotaService.contar());
        model.addAttribute("totalCategorias", categoriaService.contar());
        model.addAttribute("totalUsuarios", usuarioService.contar());
        model.addAttribute("totalSolicitudes", totalSolicitudes);
        model.addAttribute("pendientes", solicitudService.contarPorEstado("Pendiente"));
        model.addAttribute("enRevision", solicitudService.contarPorEstado("En revisión"));
        model.addAttribute("aprobadas", solicitudService.contarPorEstado("Aprobada"));
        model.addAttribute("rechazadas", solicitudService.contarPorEstado("Rechazada"));
        model.addAttribute("recientes", solicitudService.obtenerRecientes());
        model.addAttribute("activeMenu", "dashboard");
        return "admin";
    }
}
