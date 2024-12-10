package com.Localizate.demo.services;

import java.util.Optional;
import com.Localizate.demo.domain.Usuario;

public interface UsuarioService {

	// Método para registrar un nuevo usuario
    Usuario registrarUsuario(Usuario usuario);

    // Método para autenticar un usuario
    Optional<Usuario> autenticarUsuario(String email, String password);

    // Método para verificar si un email ya está registrado
    boolean existeUsuarioConEmail(String email);

	Optional<Usuario> findUsuarioById(Long usuarioId);
}