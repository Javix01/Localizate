package com.example.data;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.data.model.Establecimiento;
import com.example.data.model.Persona;
import com.example.data.model.Reserva;
import com.example.data.repository.EstablecimientoRepository;
import com.example.data.repository.PersonaRepository;
import com.example.data.repository.ReservaRepository;

@SpringBootTest
public class PersonaEstablecimientoReservaTest {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private EstablecimientoRepository establecimientoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Test
    @Transactional
    public void testCrearPersonaEstablecimientoReserva() {
        // Crear personas
        Persona persona1 = new Persona();
        persona1.setNombre("Juan");
        persona1.setCorreo("juan@example.com");

        Persona persona2 = new Persona();
        persona2.setNombre("Ana");
        persona2.setCorreo("ana@example.com");

        // Guardar personas
        personaRepository.saveAll(Arrays.asList(persona1, persona2));

        // Crear establecimientos
        Establecimiento est1 = new Establecimiento();
        est1.setNombre("Restaurante El Sabor");
        est1.setLocalizacion("Centro");
        est1.setCalle("Calle Principal");
        est1.setPoblacion("Madrid");
        est1.setCorreo("contacto@elsabor.com");
        est1.setTelefono("123456789");
        est1.setFoto("foto_restaurante.jpg");

        Establecimiento est2 = new Establecimiento();
        est2.setNombre("Café de la Plaza");
        est2.setLocalizacion("Plaza Mayor");
        est2.setCalle("Calle Mayor");
        est2.setPoblacion("Madrid");
        est2.setCorreo("info@cafedelaplaza.com");
        est2.setTelefono("987654321");
        est2.setFoto("foto_cafe.jpg");

        // Asignar propietarios (persona1 y persona2) a los establecimientos
        est1.setPersonas(Arrays.asList(persona1, persona2));
        est2.setPersonas(Arrays.asList(persona1));

        // Guardar establecimientos
        establecimientoRepository.saveAll(Arrays.asList(est1, est2));

        // Crear una reserva para persona1 en el establecimiento est1
        Reserva reserva1 = new Reserva();
        reserva1.setNombreEstablecimiento(est1.getNombre());
        reserva1.setHora(LocalTime.of(19, 0));
        reserva1.setFecha(LocalDate.now());
        reserva1.setPersona(persona1);
        reserva1.setEstablecimiento(est1);

        // Crear otra reserva para persona2 en el establecimiento est2
        Reserva reserva2 = new Reserva();
        reserva2.setNombreEstablecimiento(est2.getNombre());
        reserva2.setHora(LocalTime.of(20, 0));
        reserva2.setFecha(LocalDate.now());
        reserva2.setPersona(persona2);
        reserva2.setEstablecimiento(est2);

        // Guardar reservas
        reservaRepository.saveAll(Arrays.asList(reserva1, reserva2));

        // Verificaciones
        assertNotNull(persona1.getId());
        assertNotNull(persona2.getId());
        assertNotNull(est1.getId());
        assertNotNull(est2.getId());
        assertNotNull(reserva1.getId());
        assertNotNull(reserva2.getId());

        // Verificar que los establecimientos tienen los propietarios asignados correctamente
        Establecimiento est1FromDb = establecimientoRepository.findById(est1.getId()).orElse(null);
        assertNotNull(est1FromDb);
        assertEquals(2, est1FromDb.getPersonas().size());  // est1 tiene 2 dueños

        Establecimiento est2FromDb = establecimientoRepository.findById(est2.getId()).orElse(null);
        assertNotNull(est2FromDb);
        assertEquals(1, est2FromDb.getPersonas().size());  // est2 tiene 1 dueño

        // Verificar que las reservas están asociadas a la persona y el establecimiento correctos
        Reserva reserva1FromDb = reservaRepository.findById(reserva1.getId()).orElse(null);
        assertNotNull(reserva1FromDb);
        assertEquals(persona1.getId(), reserva1FromDb.getPersona().getId());
        assertEquals(est1.getId(), reserva1FromDb.getEstablecimiento().getId());

        Reserva reserva2FromDb = reservaRepository.findById(reserva2.getId()).orElse(null);
        assertNotNull(reserva2FromDb);
        assertEquals(persona2.getId(), reserva2FromDb.getPersona().getId());
        assertEquals(est2.getId(), reserva2FromDb.getEstablecimiento().getId());
    }
}
