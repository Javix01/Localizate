package com.Localizate.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.services.EstablecimientoService;
import com.Localizate.demo.services.ReservaService;
import com.Localizate.demo.services.UsuarioService;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private EstablecimientoService establecimientoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;  // Usado para obtener usuario, si es necesario

    // Mostrar el formulario para hacer una nueva reserva
    @GetMapping("/nuevo/{idEstablecimiento}")
    public String mostrarFormularioReserva(@PathVariable Long idEstablecimiento, Model model) {
        Establecimiento establecimiento = establecimientoService.findEstablecimientoById(idEstablecimiento)
            .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));

        if (!establecimiento.isAdmiteReservas()) {
            model.addAttribute("mensajeError", "Este establecimiento no admite reservas en este momento.");
            return "error"; // Redirige a una vista de error personalizada
        }

        Reserva reserva = new Reserva();
        reserva.setEstablecimiento(establecimiento);
        model.addAttribute("reserva", reserva);
        model.addAttribute("establecimiento", establecimiento);
        return "formularioReserva";
    }

    // Guardar la reserva y redirigir a "Mis Reservas"
    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, Model model, Principal principal) {
        // Verifica que la reserva no esté vacía o incompleta
        if (reserva == null || reserva.getFecha() == null || reserva.getHora() == null) {
            model.addAttribute("mensajeError", "La reserva no se completó correctamente.");
            return "formularioReserva"; // Retorna al formulario si algo está mal
        }

        // Obtener el email del usuario autenticado
        String email = principal.getName();  // El email del usuario logueado

        // Asignar el email al campo cliente de la reserva
        reserva.setCliente(email);

        // Guardar la reserva
        try {
            reservaService.guardarReserva(reserva); // Asegúrate de que esta función está funcionando
            model.addAttribute("mensajeExito", "Reserva realizada con éxito.");
            return "redirect:/reservas/misReservas"; // Redirige a "Mis Reservas"
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Hubo un problema al guardar la reserva.");
            return "formularioReserva"; // Vuelve al formulario en caso de error
        }
    }



    // Listar todas las reservas de un establecimiento
    @GetMapping("/lista/{idEstablecimiento}")
    public String listarReservas(@PathVariable Long idEstablecimiento, Model model) {
        Establecimiento establecimiento = establecimientoService.findEstablecimientoById(idEstablecimiento)
            .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));

        List<Reserva> reservas = reservaService.getReservasPorEstablecimiento(idEstablecimiento);
        model.addAttribute("reservas", reservas);
        model.addAttribute("establecimiento", establecimiento);
        return "listaReservas";
    }

    // Ver las reservas del usuario actual
    @GetMapping("/misReservas")
    public String misReservas(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Si no está autenticado, redirige al login
        }

        String email = principal.getName(); // Obtiene el email del usuario autenticado
        List<Reserva> reservas = reservaService.findReservasByUsuario(email);
        model.addAttribute("reservas", reservas); // Añade las reservas al modelo
        return "misReservas"; // Devuelve la vista "misReservas.html"
    }
}