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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservasController {

    private final ReservaService reservaService;
    private final EstablecimientoService establecimientoService;
    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(ReservasController.class);

    @Autowired
    public ReservasController(ReservaService reservaService, EstablecimientoService establecimientoService, UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.establecimientoService = establecimientoService;
        this.usuarioService = usuarioService;
    }

    // Centralizar la obtención del usuario logueado
    private Usuario obtenerUsuarioLogueado() {
        return usuarioService.obtenerUsuarioLogueado();
    }

    /**
     * Mostrar formulario para crear una nueva reserva.
     */
    @GetMapping("/addReserva/{establecimientoId}")
    public String mostrarFormularioAddReserva(@PathVariable Long establecimientoId, Model model) {
        Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(establecimientoId);

        if (establecimientoOpt.isEmpty()) {
            logger.error("El establecimiento con ID {} no existe.", establecimientoId);
            model.addAttribute("error", "El establecimiento no existe.");
            return "error";
        }

        Establecimiento establecimiento = establecimientoOpt.get();
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setEstablecimiento(establecimiento);

        model.addAttribute("reserva", nuevaReserva);
        
        
        if (establecimientoOpt.isPresent()) {
            List<Reserva> reservas = reservaService.findByEstablecimiento(establecimiento);
            
            model.addAttribute("establecimiento", establecimiento);
            model.addAttribute("reservas", reservas);
        } else {
            model.addAttribute("error", "El establecimiento no existe.");
        }
        
        return "addReserva";
    }

    /**
     * Guardar una nueva reserva.
     */
    @PostMapping("/guardarReserva")
    public String guardarReserva(@ModelAttribute Reserva reserva, Model model) {
        try {
            logger.info("Datos recibidos para la reserva: {}", reserva);

            Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(reserva.getEstablecimiento().getId());
            if (establecimientoOpt.isEmpty()) {
                throw new IllegalArgumentException("El establecimiento no existe.");
            }

            // Validar fecha y hora
            if (reserva.getFecha() == null || reserva.getHora() == null) {
                throw new IllegalArgumentException("La fecha y la hora son obligatorias.");
            }

            if (reserva.getFecha().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de la reserva no puede ser en el pasado.");
            }

            reserva.setUsuario(null); // Por defecto, sin usuario
            reserva.setReservable(true);

            reservaService.crearReserva(reserva);
            return "redirect:/verUsuario";
        } catch (Exception e) {
            logger.error("Error al guardar la reserva: ", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("reserva", reserva);
            return "addReserva";
        }
    }

    /**
     * Mostrar todas las reservas posibles para un establecimiento.
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

        return "reservasPosibles";
    }

    /**
     * Modificar la reserva cuando el usuario hace clic en "Reservar".
     */
    @PostMapping("/reservar/{reservaId}")
    public String reservar(@PathVariable Long reservaId, Model model) {
        Usuario usuario = obtenerUsuarioLogueado();

        Optional<Reserva> reservaOpt = reservaService.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            model.addAttribute("error", "La reserva no existe.");
            return "error";
        }

        Reserva reserva = reservaOpt.get();
        reserva.setUsuario(usuario);
        reserva.setReservable(false); // La reserva ya no es disponible

        reservaService.actualizarReserva(reserva);

        return "redirect:/reservas/nuevo/" + reserva.getEstablecimiento().getId();
    }

    /**
     * Mostrar las reservas de un usuario.
     */
    @GetMapping("/misReservas")
    public String mostrarMisReservas(Model model) {
        Usuario usuario = obtenerUsuarioLogueado();

        if (usuario == null) {
            model.addAttribute("error", "No has iniciado sesión.");
            return "error";
        }

        List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(usuario.getId());

        model.addAttribute("reservas", reservas);
        return "misReservas";
    }

    @PostMapping("/cancelarReserva/{reservaId}")
    public String cancelarReserva(@PathVariable Long reservaId, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reservaOpt = reservaService.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La reserva no existe.");
            return "redirect:/misReservas";
        }

        Reserva reserva = reservaOpt.get();

        // Validar si la reserva tiene una reseña
        if (reserva.getContenido() != null && !reserva.getContenido().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No se puede cancelar una reserva que ya tiene una reseña.");
            return "redirect:/misReservas";
        }

        // Si no hay reseña, proceder con la cancelación
        reserva.setUsuario(null);  
        reserva.setReservable(true); 
        //reserva.setCalificacion(0);  
        reserva.setContenido(""); 

        reservaService.actualizarReserva(reserva);

        Establecimiento establecimiento = reserva.getEstablecimiento();
        actualizarMediaCalificacionesEstablecimiento(establecimiento);

        redirectAttributes.addFlashAttribute("success", "Reserva cancelada con éxito.");
        return "redirect:/misReservas";
    }



    /**
     * Mostrar formulario para crear una reseña.
     */
    @GetMapping("/nueva/{reservaId}")
    public String mostrarFormularioResenia(@PathVariable Long reservaId, Model model) {
        Optional<Reserva> reservaOpt = reservaService.findById(reservaId);
        if (reservaOpt.isEmpty()) {
            model.addAttribute("error", "La reserva no existe.");
            return "error";
        }

        model.addAttribute("reserva", reservaOpt.get());
        return "reseniasNueva";
    }

    /**
     * Guardar reseña de una reserva.
     */
    @PostMapping("/guardar/{reservaId}")
    public String guardarResenia(@PathVariable Long reservaId,
                                 @RequestParam String contenido,
                                 @RequestParam int calificacion,
                                 Model model) {

        Usuario usuario = obtenerUsuarioLogueado();

        Optional<Reserva> reservaOpt = reservaService.findById(reservaId);

        if (reservaOpt.isEmpty()) {
            model.addAttribute("error", "La reserva no existe.");
            return "error";
        }

        Reserva reserva = reservaOpt.get();
        reserva.setContenido(contenido);
        reserva.setCalificacion(calificacion);
        reserva.setFechaCreacion(LocalDateTime.now());
        reserva.setUsuario(usuario);
        reserva.setReservable(false);

        reservaService.actualizarReserva(reserva);
        
     // Obtener el establecimiento asociado a la reserva
        Establecimiento establecimiento = reserva.getEstablecimiento();
        
        // Actualizar la media de calificación del establecimiento
        actualizarMediaCalificacionesEstablecimiento(establecimiento);

        return "redirect:/misReservas";
    }
    
 // Método para actualizar la media de las calificaciones de un establecimiento
    private void actualizarMediaCalificacionesEstablecimiento(Establecimiento establecimiento) {
        // Obtener todas las reservas del establecimiento
        List<Reserva> reservas = reservaService.findByEstablecimiento(establecimiento);
        
        // Calcular la media de las calificaciones
        double sumaCalificaciones = 0;
        int totalCalificaciones = 0;

        for (Reserva reserva : reservas) {
            if (reserva.getCalificacion() > 0) {  // Solo contar calificaciones mayores a 0
                sumaCalificaciones += reserva.getCalificacion();
                totalCalificaciones++;
            }
        }

        // Si hay calificaciones, calcular la media
        if (totalCalificaciones > 0) {
            double mediaCalificaciones = sumaCalificaciones / totalCalificaciones;
            establecimiento.setReseña(mediaCalificaciones);
            establecimientoService.actualizarEstablecimiento(establecimiento);
        }
    }
    
    @GetMapping("/establecimiento/{id}")
    public String verReservasDelEstablecimiento(@PathVariable Long id, Model model) {
        Optional<Establecimiento> establecimientoOpt = establecimientoService.findEstablecimientoById(id);
        
        if (establecimientoOpt.isPresent()) {
            Establecimiento establecimiento = establecimientoOpt.get();
            List<Reserva> reservas = reservaService.findByEstablecimiento(establecimiento);
            
            model.addAttribute("establecimiento", establecimiento);
            model.addAttribute("reservas", reservas);
        } else {
            model.addAttribute("error", "El establecimiento no existe.");
        }
        
        return "formularioReserva"; // Nombre del archivo HTML
    }
    
    @PostMapping("/eliminarReserva/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Reserva> reservaOpt = reservaService.findById(id);
        
        if (reservaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La reserva no existe.");
            return "redirect:/verUsuario";
        }
        
        reservaService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Reserva eliminada correctamente.");
        return "redirect:/verUsuario";
    }

}