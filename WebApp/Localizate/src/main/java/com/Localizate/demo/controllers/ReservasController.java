package com.Localizate.demo.controllers;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.EstablecimientoService;
import com.Localizate.demo.services.EstablecimientoServiceImpl;
import com.Localizate.demo.services.ReservaService;
import com.Localizate.demo.services.ReservaServiceImpl;
import com.Localizate.demo.services.UsuarioService;
import com.Localizate.demo.services.UsuarioServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservasController {

    private final ReservaService reservaService;
    private final EstablecimientoService establecimientoService;
    private final UsuarioService usuarioService;

    @Autowired
    public ReservasController(ReservaService reservaService, EstablecimientoService establecimientoService, UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.establecimientoService = establecimientoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Mostrar formulario para crear una nueva reserva.
     */
    @GetMapping("/addReserva/{establecimientoId}")
    public String mostrarFormularioAddReserva(@PathVariable Long establecimientoId, Model model) {
        Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(establecimientoId);

        if (establecimientoOpt.isEmpty()) {
            throw new IllegalArgumentException("El establecimiento no existe.");
        }

        Establecimiento establecimiento = establecimientoOpt.get();
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setEstablecimiento(establecimiento); // Asignar el establecimiento

        model.addAttribute("reserva", nuevaReserva);
        return "addReserva";
    }

    /**
     * Guardar una nueva reserva.
     */
    @PostMapping("/guardarReserva")
    public String guardarReserva(@ModelAttribute Reserva reserva, Model model) {
        try {
            System.out.println("Datos recibidos: " + reserva);

            Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(reserva.getEstablecimiento().getId());
            if (establecimientoOpt.isEmpty()) {
                throw new IllegalArgumentException("El establecimiento no existe.");
            }

            if (reserva.getFecha() == null || reserva.getHora() == null) {
                throw new IllegalArgumentException("La fecha y la hora son obligatorias.");
            }

            reserva.setUsuario(null); // Por defecto, sin usuario
            reserva.setReserbable(true);

            reservaService.crearReserva(reserva);
            return "redirect:/verUsuario";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reserva", reserva);
            return "addReserva";
        }
    }
    
    /**
     * Mostrar todas las reservas posibles para un establecimiento
     */
    @GetMapping("/reservas/nuevo/{idEstablecimiento}")
    public String mostrarReservasPosibles(@PathVariable Long idEstablecimiento, Model model) {
        Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(idEstablecimiento);

        if (establecimientoOpt.isEmpty()) {
            model.addAttribute("error", "El establecimiento no existe.");
            return "error";
        }

        Establecimiento establecimiento = establecimientoOpt.get();
        List<Reserva> reservasPosibles = reservaService.findReservasByEstablecimientoId(idEstablecimiento);

        model.addAttribute("establecimiento", establecimiento);
        model.addAttribute("reservas", reservasPosibles);

        return "reservasPosibles"; // Vista que mostraría las reservas posibles
    }
    
    /**
     * Modificar la reserva cuando el usuario hace clic en "Reservar".
     */
    @PostMapping("/reservar/{reservaId}")
    public String reservar(@PathVariable Long reservaId, Model model) {
        // Obtener el usuario logueado
        Usuario usuario = usuarioService.obtenerUsuarioLogueado(); // Obtener el usuario logueado

        Optional<Reserva> reservaOpt = reservaService.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            model.addAttribute("error", "La reserva no existe.");
            return "error";
        }

        Reserva reserva = reservaOpt.get();

        // Asignar el usuario y cambiar la reserva a no reservable
        reserva.setUsuario(usuario);  // Asignamos el usuario logueado a la reserva
        reserva.setReserbable(false);  // Cambiar la propiedad "reserbable" a false

        // Guardar la reserva actualizada
        reservaService.actualizarReserva(reserva);

        // Redirigir al usuario a una vista de confirmación o a su perfil
        return "redirect:/reservas/nuevo/" + reserva.getEstablecimiento().getId();
    }
    
    /**
     * Mostrar las reservas de un usuario.
     */
    @GetMapping("/misReservas")
    public String mostrarMisReservas(Model model) {
        // Obtener el usuario logueado
        Usuario usuario = usuarioService.obtenerUsuarioLogueado();

        if (usuario == null) {
            model.addAttribute("error", "No has iniciado sesión.");
            return "error";
        }

        // Obtener las reservas del usuario
        List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(usuario.getId());

        model.addAttribute("reservas", reservas);
        return "misReservas"; // Vista que mostrará las reservas del usuario
    }
    
}
