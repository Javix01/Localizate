package com.Localizate.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva, Long> {

	List<Reserva> findByEstablecimientoId(Long establecimientoId);
	
	// Método para encontrar reservas de un usuario específico
    List<Reserva> findByUsuarioId(Long usuarioId);
    
 // Método para encontrar todas las reservas de un establecimiento
    List<Reserva> findByEstablecimiento(Establecimiento establecimiento);
}