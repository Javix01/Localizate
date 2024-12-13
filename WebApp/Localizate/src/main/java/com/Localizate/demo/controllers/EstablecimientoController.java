package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
            // Validaciones simples
            if (establecimiento.getNombre() == null || establecimiento.getNombre().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío.");
            }
            if (establecimiento.getCiudad() == null || establecimiento.getCiudad().isEmpty()) {
                throw new IllegalArgumentException("La ciudad no puede estar vacía.");
            }

            // Recuperar el establecimiento existente
            Establecimiento existente = establecimientoService.findEstablecimientoById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));

            // Mantener el usuario creador
            establecimiento.setUsuario(existente.getUsuario());

            // Actualizar el establecimiento con los nuevos datos, manteniendo el creador original
            establecimientoService.actualizarEstablecimiento(establecimiento);
            return "redirect:/listEstablecimientos";
        } catch (IllegalArgumentException e) {
            // Enviar mensaje de error a la página si la validación falla
            model.addAttribute("error", e.getMessage());
            model.addAttribute("establecimiento", establecimiento);  // Volver a mostrar los datos introducidos
            return "actualizarEstablecimiento";  // Volver a la misma página con el error
        }
    }


    // Método para eliminar un establecimiento
    @PostMapping("/eliminarEstablecimiento/{id}")
    public String eliminarEstablecimiento(@PathVariable Long id, Model model) {
        try {
            // Llamar al servicio para eliminar el establecimiento
            establecimientoService.eliminarEstablecimiento(id);
            return "redirect:/verUsuario";  
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo eliminar el establecimiento.");
            return "verUsuario";  // Regresar con error si algo salió mal
        }
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