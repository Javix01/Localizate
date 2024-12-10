package com.Localizate.demo.controllers;

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

@Controller
@RequestMapping("/reservas")
public class ReservaController {
    @Autowired
    private EstablecimientoService establecimientoService;

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/nuevo/{idEstablecimiento}")
    public String mostrarFormularioReserva(@PathVariable Long idEstablecimiento, Model model) {
        Establecimiento establecimiento = establecimientoService.findEstablecimientoById(idEstablecimiento)
            .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));

        if (!establecimiento.isAdmiteReservas()) {
            return "error"; // Vista de error si no admite reservas
        }

        Reserva reserva = new Reserva();
        reserva.setEstablecimiento(establecimiento);

        model.addAttribute("reserva", reserva);
        model.addAttribute("establecimiento", establecimiento);
        return "formularioReserva";
    }

    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, Model model) {
        reservaService.guardarReserva(reserva);
        return "redirect:/reservas/lista/" + reserva.getEstablecimiento().getId();
    }

    @GetMapping("/lista/{idEstablecimiento}")
    public String listarReservas(@PathVariable Long idEstablecimiento, Model model) {
        List<Reserva> reservas = reservaService.getReservasPorEstablecimiento(idEstablecimiento);
        model.addAttribute("reservas", reservas);
        return "listaReservas";
    }
}