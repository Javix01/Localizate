package com.Localizate.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.Localizate.demo.domain.Establecimiento;

public interface EstablecimientoRepository extends CrudRepository<Establecimiento, Long> {

    @Query("SELECT e FROM Establecimiento e JOIN FETCH e.usuario WHERE e.id = :id")
    Establecimiento findEstablecimientoConUsuario(@Param("id") Long id);

    @Query("SELECT DISTINCT e FROM Establecimiento e LEFT JOIN FETCH e.usuario")
    List<Establecimiento> findAllWithUsuario();

    @Query("SELECT e FROM Establecimiento e " +
            "WHERE (:nombre IS NULL OR e.nombre LIKE %:nombre%) " +
            "AND (:ciudad IS NULL OR e.ciudad LIKE %:ciudad%) " +
            "AND (:tipoServicio IS NULL OR e.tipoEstablecimiento = :tipoServicio)")
    List<Establecimiento> findByFilters(
             @Param("nombre") String nombre,
             @Param("ciudad") String ciudad,
             @Param("tipoServicio") String tipoServicio);

    @Query("SELECT e FROM Establecimiento e WHERE e.usuario.id = :usuarioId")
    List<Establecimiento> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // Método para eliminar por ID
    void deleteById(Long id); // Este método es proporcionado por CrudRepository, pero lo agregamos explícitamente
}
