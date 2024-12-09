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
    public String guardarEstablecimiento(Establecimiento establecimiento) {
        establecimientoService.crearEstablecimiento(establecimiento);
        return "redirect:/listEstablecimientos";
    }

    @GetMapping("/actualizarEstablecimiento/{id}")
    public String mostrarFormularioActualizarEstablecimiento(@PathVariable Long id, Model model) {
        model.addAttribute("establecimiento", establecimientoService.findEstablecimientoById(id).orElseThrow(() -> 
            new IllegalArgumentException("Establecimiento no encontrado")));
        return "actualizarEstablecimiento";
    }

    @PostMapping("/actualizarEstablecimiento/{id}")
    public String actualizarEstablecimiento(@PathVariable Long id, @ModelAttribute Establecimiento establecimiento) {
        establecimientoService.actualizarEstablecimiento(establecimiento);
        return "redirect:/listEstablecimientos";
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
}
