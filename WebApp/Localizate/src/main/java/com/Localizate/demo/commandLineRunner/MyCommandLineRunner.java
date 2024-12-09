package com.Localizate.demo.commandLineRunner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Localizate.demo.domain.Direccion;
import com.Localizate.demo.domain.Establecimiento;
import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.repositories.DireccionRepository;
import com.Localizate.demo.repositories.EstablecimientoRepository;
import com.Localizate.demo.repositories.UsuarioRepository;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final EstablecimientoRepository establecimientoRepository;

    public MyCommandLineRunner(UsuarioRepository usuarioRepository, DireccionRepository direccionRepository, EstablecimientoRepository establecimientoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.establecimientoRepository = establecimientoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\t MyCommandLineRunner is running! ");
        poblarBD();
    }

    void poblarBD() {
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Luiky");
        usuario.setEmail("luiky@unex.es");

        // Guardar el usuario
        Usuario usuarioSaved = usuarioRepository.save(usuario);

        // Crear las direcciones
        Direccion direccion1 = new Direccion();
        direccion1.setCalle("Calle 1");
        direccion1.setCiudad("Ciudad 1");
        direccion1.setCodigoPostal(1);
        direccion1.setUsuario(usuarioSaved);

        Direccion direccion2 = new Direccion();
        direccion2.setCalle("Calle 2");
        direccion2.setCiudad("Ciudad 2");
        direccion2.setCodigoPostal(2);
        direccion2.setUsuario(usuarioSaved);

        // Guardar las direcciones
        Direccion direccion1Saved = direccionRepository.save(direccion1);
        Direccion direccion2Saved = direccionRepository.save(direccion2);

        // Asignar direcciones al usuario
        usuarioSaved.getDirecciones().add(direccion1Saved);
        usuarioSaved.getDirecciones().add(direccion2Saved);

        // Persistir el usuario
        usuarioSaved = usuarioRepository.save(usuarioSaved);

        // Crear establecimientos para el usuario
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
        establecimiento1.setUsuario(usuarioSaved);

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
        establecimiento2.setUsuario(usuarioSaved);

        // Guardar establecimientos
        establecimientoRepository.save(establecimiento1);
        establecimientoRepository.save(establecimiento2);

        // ---- Crear el segundo usuario y sus datos
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Pedro");
        usuario2.setEmail("pedro@unex.es");

        Usuario usuario2Saved = usuarioRepository.save(usuario2);

        Direccion direccion3 = new Direccion();
        direccion3.setCalle("Calle 3");
        direccion3.setCiudad("Ciudad 3");
        direccion3.setCodigoPostal(3);
        direccion3.setUsuario(usuario2Saved);

        Direccion direccion3Saved = direccionRepository.save(direccion3);

        usuario2Saved.getDirecciones().add(direccion3Saved);
        usuario2Saved = usuarioRepository.save(usuario2Saved);

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
        establecimiento3.setUsuario(usuario2Saved);

        // Guardar establecimiento
        establecimientoRepository.save(establecimiento3);

        System.out.println("Datos de prueba para usuarios, direcciones y establecimientos creados exitosamente.");
    }
}
