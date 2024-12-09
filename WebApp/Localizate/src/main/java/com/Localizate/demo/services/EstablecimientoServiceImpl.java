package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.repositories.EstablecimientoRepository;

@Service
public class EstablecimientoServiceImpl implements EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;

    @Autowired
    public EstablecimientoServiceImpl(EstablecimientoRepository establecimientoRepository) {
        this.establecimientoRepository = establecimientoRepository;
    }

    @Override
    public List<Establecimiento> findAllEstablecimientos() {
        return establecimientoRepository.findAllWithUsuario();
    }

    @Override
    public void crearEstablecimiento(Establecimiento establecimiento) {
        establecimientoRepository.save(establecimiento);
    }

    @Override
    public void actualizarEstablecimiento(Establecimiento establecimiento) {
        establecimientoRepository.save(establecimiento);
    }

    @Override
    public Optional<Establecimiento> findEstablecimientoById(Long id) {
        return establecimientoRepository.findById(id);
    }

    @Override
    public void deleteEstablecimientoById(Long id) {
        establecimientoRepository.deleteById(id);
    }
    
    public List<Establecimiento> buscarPorFiltros(String nombre, String ciudad, String tipoServicio) {
        return establecimientoRepository.findByFilters(nombre, ciudad, tipoServicio);
    }

}
