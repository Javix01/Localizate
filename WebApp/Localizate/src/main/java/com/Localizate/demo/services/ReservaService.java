
package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

public interface ReservaService {

	public void crearReserva(Reserva reserva);

    public List<Reserva> obtenerReservasPorEstablecimiento(Long establecimientoId);
    
    public List<Reserva> obtenerReservasPosibles(Long idEstablecimiento);
    
 // Método para obtener las reservas por ID de establecimiento
    List<Reserva> findReservasByEstablecimientoId(Long establecimientoId);
    
 // Método para actualizar una reserva
    void actualizarReserva(Reserva reserva);
    
    Optional<Reserva> findById(Long reservaId); // Método para buscar una reserva por ID
    
 // Método para obtener las reservas de un usuario
    List<Reserva> obtenerReservasPorUsuario(Long usuarioId);
    
    void crearResenia(Reserva reserva);
    
    public List<Reserva> findByEstablecimiento(Establecimiento establecimiento);
}