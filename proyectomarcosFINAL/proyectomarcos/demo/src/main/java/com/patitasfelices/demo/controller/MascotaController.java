package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.model.Mascota;
import com.patitasfelices.demo.service.CategoriaService;
import com.patitasfelices.demo.service.ImagenService;
import com.patitasfelices.demo.service.MascotaService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CRUD de mascotas (relacionadas con categorías). */
@Controller
@RequestMapping("/admin/mascotas")
public class MascotaController {

    private static final Logger log = LoggerFactory.getLogger(MascotaController.class);

    private final MascotaService mascotaService;
    private final CategoriaService categoriaService;
    private final ImagenService imagenService;

    public MascotaController(MascotaService mascotaService, CategoriaService categoriaService,
            ImagenService imagenService) {
        this.mascotaService = mascotaService;
        this.categoriaService = categoriaService;
        this.imagenService = imagenService;
    }

    private void cargarListado(Model model) {
        model.addAttribute("mascotas", mascotaService.obtenerTodas());
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("activeMenu", "mascotas");
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        cargarListado(model);
        model.addAttribute("mascotaNueva", new Mascota());
        return "admin-mascotas";
    }

    // REGISTRAR (abre el modal vacío)
    @GetMapping("/nueva")
    public String nueva(Model model) {
        cargarListado(model);
        model.addAttribute("mascotaNueva", new Mascota());
        model.addAttribute("abrirModal", true);
        return "admin-mascotas";
    }

    // EDITAR (abre el modal con los datos cargados)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Mascota mascota = mascotaService.buscarPorId(id);
        if (mascota == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la mascota a editar.");
            return "redirect:/admin/mascotas";
        }
        cargarListado(model);
        model.addAttribute("mascotaNueva", mascota);
        model.addAttribute("abrirModal", true);
        return "admin-mascotas";
    }

    // GUARDAR (registrar o actualizar según tenga id)
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("mascotaNueva") Mascota mascota, BindingResult result,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model, RedirectAttributes redirectAttributes) {

        if (mascota.getCategoria() == null || mascota.getCategoria().getId() == null) {
            result.rejectValue("categoria", "categoria.requerida", "Debes seleccionar una categoría");
        }

        boolean hayFoto = file != null && !file.isEmpty();
        if (hayFoto) {
            String tipo = file.getContentType();
            if (tipo == null || !(tipo.equals("image/png") || tipo.equals("image/jpeg"))) {
                result.rejectValue("imagen", "imagen.invalida", "La foto debe ser un archivo PNG o JPG");
            }
        }

        if (result.hasErrors()) {
            cargarListado(model);
            model.addAttribute("abrirModal", true);
            return "admin-mascotas";
        }

        try {
            boolean esNueva = mascota.getId() == null;
            String rutaImagen = hayFoto ? imagenService.guardar(file) : null;
            mascotaService.guardar(mascota, rutaImagen);
            redirectAttributes.addFlashAttribute("exito",
                    esNueva ? "Mascota registrada correctamente." : "Mascota actualizada correctamente.");
        } catch (Exception e) {
            log.error("Error al guardar la mascota", e);
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar la mascota.");
        }
        return "redirect:/admin/mascotas";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mascotaService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Mascota eliminada correctamente.");
        } catch (Exception e) {
            log.error("Error al eliminar la mascota", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la mascota.");
        }
        return "redirect:/admin/mascotas";
    }
}
