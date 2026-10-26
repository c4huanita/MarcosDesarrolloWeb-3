package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.model.Mascota;
import com.patitasfelices.demo.model.SolicitudAdopcion;
import com.patitasfelices.demo.service.CategoriaService;
import com.patitasfelices.demo.service.MascotaService;
import com.patitasfelices.demo.service.SolicitudService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final MascotaService mascotaService;
    private final SolicitudService solicitudService;
    private final CategoriaService categoriaService; // <-- Añadido

    public HomeController(MascotaService mascotaService, SolicitudService solicitudService, CategoriaService categoriaService) {
        this.mascotaService = mascotaService;
        this.solicitudService = solicitudService;
        this.categoriaService = categoriaService;
    }

    @InitBinder("solicitudAdopcion")
    public void restringirCampos(WebDataBinder binder) {
        binder.setDisallowedFields("id", "estado", "mascota*", "usuario*", "fechaSolicitud");
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    // PÁGINA 1 EN CASCADA: Catálogo público con los 2 desplegables (Categoría -> Mascota)
    @GetMapping("/mascotas")
    public String listarMascotasPublicas(@RequestParam(required = false) Long categoriaId,
                                         @RequestParam(required = false) Long mascotaId,
                                         Model model) {
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("mascotasDelSelect", mascotaService.obtenerPorCategoria(categoriaId));
        model.addAttribute("mascotas", mascotaService.filtrarEnCascada(categoriaId, mascotaId));
        model.addAttribute("categoriaSeleccionada", categoriaId);
        model.addAttribute("mascotaSeleccionada", mascotaId);
        model.addAttribute("solicitudAdopcion", new SolicitudAdopcion());
        return "mascotas";
    }

    @GetMapping("/adoptar")
    public String adoptar() {
        return "adoptar";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @PostMapping("/adoptar/guardar")
    public String guardarSolicitud(@Valid @ModelAttribute("solicitudAdopcion") SolicitudAdopcion solicitud,
            BindingResult result,
            @RequestParam(value = "mascotaId", required = false) Long mascotaId,
            HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        Mascota seleccionada = (mascotaId != null) ? mascotaService.buscarPorId(mascotaId) : null;
        if (seleccionada == null) {
            result.reject("mascota.requerida", "Selecciona una mascota válida para adoptar.");
        }

        if (result.hasErrors()) {
            model.addAttribute("mascotas", mascotaService.obtenerTodas());
            model.addAttribute("mascotaSeleccionada", seleccionada);
            model.addAttribute("abrirModalAdopcion", true);
            return "mascotas";
        }

        try {
            Long usuarioId = (Long) session.getAttribute("usuarioId");
            solicitudService.guardarSolicitud(solicitud, mascotaId, usuarioId);
            redirectAttributes.addFlashAttribute("exito", "¡Solicitud enviada correctamente!");
        } catch (Exception e) {
            log.error("Error al guardar la solicitud de adopción", e);
            redirectAttributes.addFlashAttribute("error", "Hubo un error al enviar la solicitud.");
        }
        return "redirect:/mascotas";
    }
}