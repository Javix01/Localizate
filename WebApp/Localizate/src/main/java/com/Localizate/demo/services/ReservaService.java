package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

public interface ReservaService {

	/**
     * Obtiene una lista de reservas asociadas a un establecimiento por su ID.
     * 
     * @param establecimientoId ID del establecimiento.
     * @return Lista de reservas.
     */
    List<Reserva> getReservasPorEstablecimiento(Long establecimientoId);

    /**
     * Guarda una nueva reserva o actualiza una existente.
     * 
     * @param reserva Objeto de la reserva a guardar.
     * @return Reserva guardada.
     */
    Reserva guardarReserva(Reserva reserva);

    /**
     * Obtiene una reserva por su ID.
     * 
     * @param id ID de la reserva.
     * @return Reserva encontrada, si existe.
     */
    Optional<Reserva> findReservaById(Long id);

    /**
     * Elimina una reserva por su ID.
     * 
     * @param id ID de la reserva a eliminar.
     */
    void eliminarReservaById(Long id);
}