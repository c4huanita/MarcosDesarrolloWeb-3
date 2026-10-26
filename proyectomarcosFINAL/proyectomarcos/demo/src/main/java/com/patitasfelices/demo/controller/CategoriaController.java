package com.patitasfelices.demo.controller;

import com.patitasfelices.demo.model.Categoria;
import com.patitasfelices.demo.service.CategoriaService;
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

/** CRUD de categorías. */
@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private static final Logger log = LoggerFactory.getLogger(CategoriaController.class);

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    private void cargarListado(Model model) {
        model.addAttribute("categorias", categoriaService.obtenerTodas());
        model.addAttribute("activeMenu", "categorias");
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        cargarListado(model);
        model.addAttribute("categoriaNueva", new Categoria());
        return "admin-categorias";
    }

    // REGISTRAR (abre el modal vacío)
    @GetMapping("/nueva")
    public String nueva(Model model) {
        cargarListado(model);
        model.addAttribute("categoriaNueva", new Categoria());
        model.addAttribute("abrirModal", true);
        return "admin-categorias";
    }

    // EDITAR (abre el modal con los datos cargados)
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró la categoría.");
            return "redirect:/admin/categorias";
        }
        cargarListado(model);
        model.addAttribute("categoriaNueva", categoria);
        model.addAttribute("abrirModal", true);
        return "admin-categorias";
    }

    // GUARDAR (registrar o actualizar según tenga id)
    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoriaNueva") Categoria categoria, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            cargarListado(model);
            model.addAttribute("abrirModal", true);
            return "admin-categorias";
        }

        try {
            boolean esNueva = categoria.getId() == null;
            categoriaService.guardar(categoria);
            redirectAttributes.addFlashAttribute("exito",
                    esNueva ? "Categoría registrada correctamente." : "Categoría actualizada correctamente.");
        } catch (Exception e) {
            log.error("Error al guardar la categoría", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo guardar la categoría.");
        }
        return "redirect:/admin/categorias";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.eliminar(id);
            redirectAttributes.addFlashAttribute("exito", "Categoría eliminada correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error al eliminar la categoría", e);
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la categoría.");
        }
        return "redirect:/admin/categorias";
    }
}
