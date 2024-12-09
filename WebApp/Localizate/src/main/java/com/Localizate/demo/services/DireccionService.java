package com.Localizate.demo.services;

import java.util.Optional;

import com.Localizate.demo.domain.Direccion;

 

public interface DireccionService {
	
	public void actualizarDireccion(Direccion direccion);

	public Direccion crearDireccion (Direccion direccion);
	
	public void deleteDireccionById(Long id);
	
	public Optional<Direccion> findDireccionById(Long id);
}
