package com.Localizate.demo.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.Localizate.demo.domain.Direccion;
import com.Localizate.demo.domain.Usuario;

public interface DireccionRepository extends CrudRepository<Direccion, Long> {
	
	List <Direccion> findByUsuarioId(Long id);
	
	List<Direccion> findByUsuario(Usuario u);
		


}
