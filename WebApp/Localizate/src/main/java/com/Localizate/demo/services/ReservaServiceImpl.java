package com.Localizate.demo.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

@Service
public class ReservaServiceImpl implements ReservaService {

	private final ReservaRepository reservaRepository;
	private final EstablecimientoService establecimientoService;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository, EstablecimientoService establecimientoService) {
        this.reservaRepository = reservaRepository;
        this.establecimientoService = establecimientoService;
    }

    @Override
    public void crearReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    @Override
    public List<Reserva> obtenerReservasPorEstablecimiento(Long establecimientoId) {
        return reservaRepository.findByEstablecimientoId(establecimientoId);
    }
    
    public List<Reserva> obtenerReservasPosibles(Long idEstablecimiento) {
        // Generar horarios predefinidos para este ejemplo
        List<Reserva> reservasPosibles = new ArrayList<>();

        for (int i = 9; i <= 18; i++) { // Generar reservas de 9:00 a 18:00
            Reserva reserva = new Reserva();
            reserva.setFecha(LocalDate.now().plusDays(1)); // Ejemplo: todas las reservas son para mañana
            reserva.setHora(String.format("%02d:00", i));
            reserva.setEstablecimiento(establecimientoService.findEstablecimientoById(idEstablecimiento).orElse(null));
            reservasPosibles.add(reserva);
        }

        return reservasPosibles;
    }

    @Override
    public List<Reserva> findReservasByEstablecimientoId(Long establecimientoId) {
        return reservaRepository.findByEstablecimientoId(establecimientoId);
    }
    
    @Override
    public void actualizarReserva(Reserva reserva) {
        reservaRepository.save(reserva);  // Guardar la reserva actualizada
    }
    
    @Override
    public Optional<Reserva> findById(Long reservaId) {
        return reservaRepository.findById(reservaId); // Buscar la reserva por ID
    }
    
    @Override
    public List<Reserva> obtenerReservasPorUsuario(Long usuarioId) {
        // Obtenemos las reservas de un usuario en particular
        return reservaRepository.findByUsuarioId(usuarioId);
    }

	@Override
	public void crearResenia(Reserva reserva) {
		reservaRepository.save(reserva);
	}

	public List<Reserva> findByEstablecimiento(Establecimiento establecimiento) {
	    return reservaRepository.findByEstablecimiento(establecimiento);
	}

	public List<Reserva> findResenasByEstablecimiento(Establecimiento establecimiento) {
        return reservaRepository.findByEstablecimientoAndContenidoIsNotNull(establecimiento);
    }
	
	/**
     * Eliminar una reserva por su ID.
     *
     * @param id ID de la reserva a eliminar.
     */
    public void deleteById(Long id) {
        reservaRepository.deleteById(id);
    }
    
    public List<Reserva> obtenerReservasFuturas(Long establecimientoId) {
        LocalDateTime ahora = LocalDateTime.now();

        List<Reserva> reservas = reservaRepository.findByEstablecimientoId(establecimientoId);

        return reservas.stream()
            .filter(reserva -> {
                LocalDateTime fechaHoraReserva = reserva.getFecha().atTime(
                    Integer.parseInt(reserva.getHora().split(":")[0]),
                    Integer.parseInt(reserva.getHora().split(":")[1])
                );
                return fechaHoraReserva.isAfter(ahora.plusHours(1)); // Filtro las reservas que faltan más de 1 hora
            })
            .collect(Collectors.toList());
    }
}