package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public List<Reserva> getReservasPorEstablecimiento(Long establecimientoId) {
        return reservaRepository.findByEstablecimientoId(establecimientoId);
    }

    @Override
    public void guardarReserva(Reserva reserva) {
        reservaRepository.save(reserva); // Guarda la reserva en la base de datos
    }

    @Override
    public Optional<Reserva> findReservaById(Long id) {
        return reservaRepository.findById(id);
    }

    @Override
    public void eliminarReservaById(Long id) {
        reservaRepository.deleteById(id);
    }
    
    @Override
    public List<Reserva> getReservasPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }
    
    @Override
    public List<Reserva> findReservasByUsuario(String email) {
    	return reservaRepository.findByCliente(email); // Esto busca las reservas del usuario por email
    }
}