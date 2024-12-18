package com.Localizate.demo.commandLineRunner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.repositories.EstablecimientoRepository;
import com.Localizate.demo.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.Localizate.demo.domain.Reserva;
import com.Localizate.demo.repositories.ReservaRepository;

import java.time.LocalDate;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final ReservaRepository reservaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MyCommandLineRunner(UsuarioRepository usuarioRepository,
                                EstablecimientoRepository establecimientoRepository,
                                ReservaRepository reservaRepository,
                                BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.reservaRepository = reservaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\t MyCommandLineRunner is running! ");
        poblarBD();
    }

    void poblarBD() {
        // Crear el primer usuario con contraseña encriptada
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Luiky");
        usuario1.setEmail("luiky@unex.es");
        usuario1.setPassword(passwordEncoder.encode("password123")); // Contraseña encriptada
        usuario1.setRole("ADMIN");
        // Guardar el usuario
        Usuario usuario1Saved = usuarioRepository.save(usuario1);

        // Crear establecimientos para el primer usuario
        Establecimiento establecimiento1 = new Establecimiento();
        establecimiento1.setNombre("Restaurante La Plaza");
        establecimiento1.setLocalizacion("40.416775, -3.703790");
        establecimiento1.setCalle("Calle Mayor 1");
        establecimiento1.setCiudad("Ciudad 1");
        establecimiento1.setEmail("info@laplaza.com");
        establecimiento1.setTelefono(910000001);
        establecimiento1.setWeb("www.laplaza.com");
        establecimiento1.setReseña(4.5f);
        establecimiento1.setTipoEstablecimiento("Restaurante");
        establecimiento1.setAdmiteReservas(true);
        establecimiento1.setUsuario(usuario1Saved);

        Establecimiento establecimiento2 = new Establecimiento();
        establecimiento2.setNombre("Bar Los Amigos");
        establecimiento2.setLocalizacion("40.415775, -3.703780");
        establecimiento2.setCalle("Calle Amigos 2");
        establecimiento2.setCiudad("Ciudad 2");
        establecimiento2.setEmail("info@losamigos.com");
        establecimiento2.setTelefono(910000002);
        establecimiento2.setWeb("www.losamigos.com");
        establecimiento2.setReseña(4.2f);
        establecimiento2.setTipoEstablecimiento("Bar");
        establecimiento2.setAdmiteReservas(true);
        establecimiento2.setUsuario(usuario1Saved);

        // Guardar establecimientos
        establecimientoRepository.save(establecimiento1);
        establecimientoRepository.save(establecimiento2);

        // Crear una reserva para establecimiento1
        Reserva reserva1 = new Reserva();
        reserva1.setFecha(LocalDate.of(2024, 12, 20));  // Fecha de la reserva
        reserva1.setHora("20:00");  // Hora de la reserva
        reserva1.setEstablecimiento(establecimiento1);  // Relación con el establecimiento
        reserva1.setUsuario(usuario1Saved);  // Relación con el usuario

        // Guardar reserva
        reservaRepository.save(reserva1);

        // ---- Crear el segundo usuario y sus datos
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Pedro");
        usuario2.setEmail("pedro@unex.es");
        usuario2.setPassword(passwordEncoder.encode("securePassword")); // Contraseña encriptada
        usuario2.setRole("USER");
        Usuario usuario2Saved = usuarioRepository.save(usuario2);

        Establecimiento establecimiento3 = new Establecimiento();
        establecimiento3.setNombre("Hotel Luna");
        establecimiento3.setLocalizacion("40.417775, -3.703800");
        establecimiento3.setCalle("Calle Estrellas 3");
        establecimiento3.setCiudad("Ciudad 3");
        establecimiento3.setEmail("info@hotelluna.com");
        establecimiento3.setTelefono(910000003);
        establecimiento3.setWeb("www.hotelluna.com");
        establecimiento3.setReseña(4.8f);
        establecimiento3.setTipoEstablecimiento("Hotel");
        establecimiento3.setAdmiteReservas(true);
        establecimiento3.setUsuario(usuario2Saved);

        // Guardar establecimiento
        establecimientoRepository.save(establecimiento3);

        // Crear una reserva para establecimiento3
        Reserva reserva2 = new Reserva();
        reserva2.setFecha(LocalDate.of(2024, 12, 25));  // Fecha de la reserva
        reserva2.setHora("18:30");  // Hora de la reserva
        reserva2.setEstablecimiento(establecimiento3);  // Relación con el establecimiento
        reserva2.setUsuario(usuario2Saved);  // Relación con el usuario

        // Guardar reserva
        reservaRepository.save(reserva2);
        
     // Crear el primer usuario con contraseña encriptada
        Usuario usuario3 = new Usuario();
        usuario3.setNombre("Javier");
        usuario3.setEmail("javier@javier.es");
        usuario3.setPassword(passwordEncoder.encode("javier")); // Contraseña encriptada
        usuario3.setRole("USER");
        // Guardar el usuario
        Usuario usuario3Saved = usuarioRepository.save(usuario3);
        
        Establecimiento establecimiento4 = new Establecimiento();
        establecimiento4.setNombre("Hotel Luna");
        establecimiento4.setLocalizacion("40.417775, -3.703800");
        establecimiento4.setCalle("Calle Estrellas 3");
        establecimiento4.setCiudad("Ciudad 3");
        establecimiento4.setEmail("info@hotelluna.com");
        establecimiento4.setTelefono(910000003);
        establecimiento4.setWeb("www.hotelluna.com");
        establecimiento4.setReseña(4.8f);
        establecimiento4.setTipoEstablecimiento("Hotel");
        establecimiento4.setAdmiteReservas(true);
        establecimiento4.setUsuario(usuario3Saved);
        
        // Guardar establecimiento
        establecimientoRepository.save(establecimiento4);

        System.out.println("Datos de prueba para usuarios, establecimientos y reservas creados exitosamente.");
    }
}