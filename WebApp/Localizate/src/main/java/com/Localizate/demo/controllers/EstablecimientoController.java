package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.services.EstablecimientoService;

@Controller
public class EstablecimientoController {

    private final EstablecimientoService establecimientoService;
    
    public EstablecimientoController(EstablecimientoService establecimientoService) {
        this.establecimientoService = establecimientoService;
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
            // Validaciones simples
            if (establecimiento.getNombre() == null || establecimiento.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío.");
            }
            if (establecimiento.getCiudad() == null || establecimiento.getCiudad().isEmpty()) {
                throw new IllegalArgumentException("La ciudad no puede estar vacía.");
            }

            // Si las validaciones pasan, guardar el establecimiento
            establecimientoService.crearEstablecimiento(establecimiento);
            return "redirect:/listEstablecimientos";
        } catch (IllegalArgumentException e) {
            // Enviar mensaje de error y los datos al modelo
            model.addAttribute("error", e.getMessage());
            model.addAttribute("establecimiento", establecimiento);  // Para mantener los datos introducidos
            return "addEstablecimiento";  // Redirigir a la página de agregar establecimiento con error
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
            // Validaciones simples
            if (establecimiento.getNombre() == null || establecimiento.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío.");
            }
            if (establecimiento.getCiudad() == null || establecimiento.getCiudad().isEmpty()) {
                throw new IllegalArgumentException("La ciudad no puede estar vacía.");
            }

            // Si la validación pasa, actualizar el establecimiento
            establecimientoService.actualizarEstablecimiento(establecimiento);
            return "redirect:/listEstablecimientos";
        } catch (IllegalArgumentException e) {
            // Enviar mensaje de error a la página si la validación falla
            model.addAttribute("error", e.getMessage());
            model.addAttribute("establecimiento", establecimiento);  // Volver a mostrar los datos introducidos
            return "actualizarEstablecimiento";  // Volver a la misma página con el error
        }
    }

    @DeleteMapping("/deleteEstablecimiento/{id}")
    public String deleteEstablecimiento(@PathVariable Long id) {
        establecimientoService.deleteEstablecimientoById(id);
        return "redirect:/listEstablecimientos";
    }

    @GetMapping("/buscarEstablecimientos")
    public String buscarEstablecimientos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String tipoServicio,
            Model model) {

        // Manejar parámetros nulos o vacíos
        nombre = (nombre == null || nombre.trim().isEmpty()) ? null : nombre;
        ciudad = (ciudad == null || ciudad.trim().isEmpty()) ? null : ciudad;
        tipoServicio = (tipoServicio == null || tipoServicio.trim().isEmpty()) ? null : tipoServicio;

        // Obtener lista filtrada usando el servicio
        List<Establecimiento> listaFiltrada = establecimientoService.buscarPorFiltros(nombre, ciudad, tipoServicio);

        // Agregar lista filtrada al modelo
        model.addAttribute("listaEstablecimientos", listaFiltrada);

        return "listaEstablecimientos";
    }
    
    @GetMapping("/detallesEstablecimiento/{id}")
    public String verDetallesEstablecimiento(@PathVariable Long id, Model model) {
        model.addAttribute("establecimiento", establecimientoService.findEstablecimientoById(id).orElseThrow(() -> 
        new IllegalArgumentException("Establecimiento no encontrado")));
        return "detallesEstablecimiento"; // Nombre del template
    }

    // Endpoint para ver la lista de establecimientos
    @GetMapping("/establecimientos")
    public String listarEstablecimientos(Model model) {
        // Obtener la lista de todos los establecimientos
        List<Establecimiento> establecimientos = establecimientoService.findAllEstablecimientos();

        // Agregar la lista al modelo para que se pueda acceder en la vista
        model.addAttribute("establecimientos", establecimientos);

        // Retornar la vista (puede ser un archivo HTML o Thymeleaf, según tu implementación)
        return "listaEstablecimientos";  // Aquí "establecimientos" es el nombre de la vista que muestra los establecimientos
    }
}