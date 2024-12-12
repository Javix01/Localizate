package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

@Service
public class ReservaServiceImpl implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    // Método para crear una nueva reserva
    @Override
    public Reserva crearReserva(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    // Método para obtener todas las reservas de un establecimiento
    @Override
    public List<Reserva> obtenerReservasPorEstablecimiento(Long establecimientoId) {
        return reservaRepository.findByEstablecimientoId(establecimientoId);
    }

    // Método para obtener todas las reservas de un usuario
    @Override
    public List<Reserva> obtenerReservasPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    // Método para eliminar una reserva por su ID
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    // Método para encontrar una reserva por su ID
    public Optional<Reserva> encontrarReservaPorId(Long id) {
        return reservaRepository.findById(id);
    }
    
    public boolean tieneReservas(Long establecimientoId) {
        long count = reservaRepository.countByEstablecimientoId(establecimientoId);
        return count > 0;
    }
}