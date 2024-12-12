package com.Localizate.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva, Long> {

	// Método para encontrar reservas por establecimiento
    List<Reserva> findByEstablecimientoId(Long establecimientoId);

    // Método para encontrar reservas por usuario (si lo necesitas)
    List<Reserva> findByUsuarioId(Long usuarioId);
    
 // Método para contar reservas por establecimiento
    long countByEstablecimientoId(Long establecimientoId);
}