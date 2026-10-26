package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.service.CategoriaService;
import com.patitasfelices.demo.service.MascotaService;
import com.patitasfelices.demo.service.SolicitudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/solicitudes")
public class SolicitudController {

    private static final Logger log = LoggerFactory.getLogger(SolicitudController.class);

    private final SolicitudService solicitudService;
    private final CategoriaService categoriaService; // <-- Añadido
    private final MascotaService mascotaService;     // <-- Añadido

    public SolicitudController(SolicitudService solicitudService, CategoriaService categoriaService, MascotaService mascotaService) {
        this.solicitudService = solicitudService;
        this.categoriaService = categoriaService;
        this.mascotaService = mascotaService;
    }

    // PÁGINA 2 EN CASCADA + CONSULTA DTO: Listado filtrado de solicitudes
    @GetMapping
    public String listar(@RequestParam(required = false) Long categoriaId,
                         @RequestParam(required = false) Long mascotaId,
                         Model model) {
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("mascotasDelSelect", mascotaService.obtenerPorCategoria(categoriaId));
        // Aquí usa la consulta con DTO multitabla
        model.addAttribute("solicitudes", solicitudService.listarFiltradasConDTO(categoriaId, mascotaId));
        model.addAttribute("categoriaSeleccionada", categoriaId);
        model.addAttribute("mascotaSeleccionada", mascotaId);
        model.addAttribute("estados", SolicitudService.ESTADOS);
        model.addAttribute("activeMenu", "solicitudes");
        return "admin-solicitudes";
    }

    @PostMapping("/estado")
    public String cambiarEstado(@RequestParam Long id, @RequestParam String estado,
            RedirectAttributes redirectAttributes) {
        try {
            solicitudService.actualizarEstado(id, estado);
            redirectAttributes.addFlashAttribute("exito", "Estado de la solicitud actualizado a \"" + estado + "\".");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error al actualizar la solicitud", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar la solicitud.");
        }
        return "redirect:/admin/solicitudes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            solicitudService.eliminarSolicitud(id);
            redirectAttributes.addFlashAttribute("exito", "Solicitud eliminada correctamente.");
        } catch (Exception e) {
            log.error("Error al eliminar la solicitud", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la solicitud.");
        }
        return "redirect:/admin/solicitudes";
    }
}