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

    public void actualizarEstablecimiento(Establecimiento establecimiento) {
        Establecimiento existente = establecimientoRepository.findById(establecimiento.getId())
                .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));

        // Mantener el usuario creador original
        establecimiento.setUsuario(existente.getUsuario());

        // Actualizar los demás datos del establecimiento
        existente.setNombre(establecimiento.getNombre());
        existente.setCiudad(establecimiento.getCiudad());
        existente.setLocalizacion(establecimiento.getLocalizacion());
        existente.setCalle(establecimiento.getCalle());
        existente.setTelefono(establecimiento.getTelefono());
        existente.setEmail(establecimiento.getEmail());
        existente.setWeb(establecimiento.getWeb());
        existente.setTipoEstablecimiento(establecimiento.getTipoEstablecimiento());

        // Guardar los cambios
        establecimientoRepository.save(existente);
    }


    @Override
    public Optional<Establecimiento> findEstablecimientoById(Long id) {
        return Optional.ofNullable(establecimientoRepository.findEstablecimientoConUsuario(id));
    }

    @Override
    public void deleteEstablecimientoById(Long id) {
        establecimientoRepository.deleteById(id);
    }

    @Override
    public List<Establecimiento> buscarPorFiltros(String nombre, String ciudad, String tipoServicio) {
        return establecimientoRepository.findByFilters(nombre, ciudad, tipoServicio);
    }

    @Override
    public List<Establecimiento> findEstablecimientosByUsuarioId(Long usuarioId) {
        return establecimientoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public void eliminarEstablecimiento(Long id) {
    	Establecimiento establecimiento = establecimientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Establecimiento no encontrado"));

        // Aquí puedes añadir lógica para manejar relaciones antes de eliminar, si es necesario.
        establecimientoRepository.delete(establecimiento);
    }
}