package com.Localizate.demo.services;

import java.util.List;
import java.util.Optional;

import com.Localizate.demo.domain.Establecimiento;

public interface EstablecimientoService {

    public List<Establecimiento> findAllEstablecimientos();

    public void crearEstablecimiento(Establecimiento establecimiento);

    public void actualizarEstablecimiento(Establecimiento establecimiento);

    public Optional<Establecimiento> findEstablecimientoById(Long id);

    public void deleteEstablecimientoById(Long id);
    
    public List<Establecimiento> buscarPorFiltros(String nombre, String ciudad, String tipoServicio);
}
