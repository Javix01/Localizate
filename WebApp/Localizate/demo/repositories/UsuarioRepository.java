package com.Localizate.demo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.Localizate.demo.domain.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	
	// Método para encontrar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Método para verificar si un email ya está registrado
    boolean existsByEmail(String email);
}

