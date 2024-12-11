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
	
	public Optional<Usuario> getUsuarioById(Long id);
	
	public Usuario updateUsuario(Long id, Usuario updatedUsuario);
	
	public void deleteUsuario(Long id);
	
	public void actualizarUsuario(Usuario usuario);

	public void deleteUsuarioById(Long id);
	
	public Usuario obtenerUsuarioLogueado();
	
	public Usuario obtenerUsuarioPorId(Long id);
}