package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.EstablecimientoService;
import com.Localizate.demo.services.UsuarioService;

@Controller
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;
    private final UsuarioService usuarioService;

    public EstablecimientoController(EstablecimientoService establecimientoService, UsuarioService usuarioService) {
        this.establecimientoService = establecimientoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listEstablecimientos")
    public String listEstablecimientosPage(Model model) {
        model.addAttribute("listaEstablecimientos", establecimientoService.findAllEstablecimientos());
        return "listaEstablecimientos";
    }

    @GetMapping("/addEstablecimiento")
    public String mostrarFormularioAddEstablecimiento(Model model) {
        model.addAttribute("establecimiento", new Establecimiento());
        return "addEstablecimiento";
    }

    @PostMapping("/guardarEstablecimiento")
    public String guardarEstablecimiento(@ModelAttribute Establecimiento establecimiento, Model model) {
        try {
            Usuario usuarioLogueado = usuarioService.obtenerUsuarioLogueado();

            if (usuarioLogueado == null) {
                throw new IllegalStateException("No se encontró el usuario logueado.");
            }

            establecimiento.setUsuario(usuarioLogueado);

            if (establecimiento.getNombre() == null || establecimiento.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío.");
            }
            if (establecimiento.getCiudad() == null || establecimiento.getCiudad().isEmpty()) {
                throw new IllegalArgumentException("La ciudad no puede estar vacía.");
            }

            establecimientoService.crearEstablecimiento(establecimiento);
            return "redirect:/verUsuario";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("establecimiento", establecimiento);
            return "addEstablecimiento";
        }
    }

    @GetMapping("/actualizarEstablecimiento/{id}")
    public String mostrarFormularioActualizarEstablecimiento(@PathVariable Long id, Model model) {
        model.addAttribute("establecimiento", establecimientoService.findEstablecimientoById(id).orElseThrow(() -> 
            new IllegalArgumentException("Establecimiento no encontrado")));
        return "actualizarEstablecimiento";
    }

    @PostMapping("/actualizarEstablecimiento/{id}")
    public String actualizarEstablecimiento(@PathVariable Long id, @ModelAttribute Establecimiento establecimiento, Model model) {
        try {
            if (establecimiento.getNombre() == null || establecimiento.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío.");
            }
            if (establecimiento.getCiudad() == null || establecimiento.getCiudad().isEmpty()) {
                throw new IllegalArgumentException("La ciudad no puede estar vacía.");
            }

            establecimientoService.actualizarEstablecimiento(establecimiento);
            return "redirect:/listEstablecimientos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("establecimiento", establecimiento);
            return "actualizarEstablecimiento";
        }
    }

    // Método para eliminar un establecimiento
    @PostMapping("/eliminarEstablecimiento/{id}")
    public String eliminarEstablecimiento(@PathVariable Long id, Model model) {
        try {
            // Llamar al servicio para eliminar el establecimiento
            establecimientoService.eliminarEstablecimiento(id);
            return "redirect:/verUsuario";  // Redirigir a la vista de usuario después de la eliminación
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo eliminar el establecimiento.");
            return "verUsuario";  // Regresar con error si algo salió mal
        }
    }
    
}
