package com.Localizate.demo.controllers;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.EstablecimientoServiceImpl;
import com.Localizate.demo.services.ReservaServiceImpl;
import com.Localizate.demo.services.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservasController {

    private final ReservaServiceImpl reservaService;
    private final EstablecimientoServiceImpl establecimientoService;
    private final UsuarioServiceImpl usuarioService;

    @Autowired
    public ReservasController(ReservaServiceImpl reservaService, EstablecimientoServiceImpl establecimientoService, UsuarioServiceImpl usuarioService) {
        this.reservaService = reservaService;
        this.establecimientoService = establecimientoService;
        this.usuarioService = usuarioService;
    }

    // Mostrar formulario para crear una nueva reserva
    @GetMapping("/crearReserva/{establecimientoId}")
    public String mostrarFormularioReserva(@PathVariable Long establecimientoId, Model model) {
        Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(establecimientoId);

        if (establecimientoOpt.isPresent()) {
            Establecimiento establecimiento = establecimientoOpt.get();

            // Obtener todas las reservas del establecimiento
            List<Reserva> reservas = reservaService.obtenerReservasPorEstablecimiento(establecimientoId);

            model.addAttribute("establecimiento", establecimiento);
            model.addAttribute("fecha", LocalDate.now());
            model.addAttribute("reservas", reservas); // Agregar las reservas al modelo
            return "formularioReserva";
        } else {
            model.addAttribute("error", "Establecimiento no encontrado.");
            return "redirect:/listEstablecimientos";
        }
    }

    // Guardar la reserva
    @PostMapping("/guardarReserva")
    public String guardarReserva(@RequestParam Long establecimientoId,
                                 @RequestParam String fecha,
                                 @RequestParam String hora, Model model) {
        // Obtener usuario logueado
        Usuario usuarioLogueado = usuarioService.obtenerUsuarioLogueado();

        if (usuarioLogueado == null) {
            model.addAttribute("error", "Usuario no autenticado.");
            return "redirect:/login";  // Redirigir al login si no está autenticado
        }

        // Verificar si el establecimiento existe
        Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(establecimientoId);
        
        if (establecimientoOpt.isPresent()) {
            Establecimiento establecimiento = establecimientoOpt.get();

            // Crear nueva reserva
            Reserva reserva = new Reserva();
            reserva.setEstablecimiento(establecimiento);
            reserva.setCliente(usuarioLogueado.getNombre());
            reserva.setFecha(LocalDate.parse(fecha));  // Convertir fecha de string a LocalDate
            reserva.setHora(hora);
            reserva.setUsuario(usuarioLogueado);

            // Guardar la reserva
            reservaService.crearReserva(reserva);
            
            return "redirect:/listEstablecimientos";  // Redirigir al listado de establecimientos
        } else {
            model.addAttribute("error", "Establecimiento no encontrado.");
            return "redirect:/listEstablecimientos";  // Redirigir si el establecimiento no existe
        }
    }
    
    @DeleteMapping("/eliminarReserva/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return "redirect:/misReservas"; // Redirige a la lista de reservas del usuario
    }
}