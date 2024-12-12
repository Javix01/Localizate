package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

public interface ReservaService {

	// Método para crear una nueva reserva
    Reserva crearReserva(Reserva reserva);

    // Método para obtener todas las reservas de un establecimiento
    List<Reserva> obtenerReservasPorEstablecimiento(Long establecimientoId);

    // Método para obtener todas las reservas de un usuario
    List<Reserva> obtenerReservasPorUsuario(Long usuarioId);
    
    // Método para eliminar una reserva por su ID
    public void eliminarReserva(Long id);

    // Método para encontrar una reserva por su ID
    public Optional<Reserva> encontrarReservaPorId(Long id);
    
    public boolean tieneReservas(Long establecimientoId);
}