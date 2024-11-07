package com.example.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.data.model.Persona;
import com.example.data.model.Reserva;

@Repository
public interface ReservaRepository extends CrudRepository<Reserva, Long> {
}
