package com.example.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.data.model.Persona;

@Repository
public interface PersonaRepository extends CrudRepository<Persona, Long> {
}
