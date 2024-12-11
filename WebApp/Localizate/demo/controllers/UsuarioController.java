package com.Localizate.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.EstablecimientoService;
import com.Localizate.demo.services.UsuarioService;
import com.Localizate.demo.domain.Establecimiento;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EstablecimientoService establecimientoService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, BCryptPasswordEncoder passwordEncoder, EstablecimientoService establecimientoService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.establecimientoService = establecimientoService;
    }

    /**
     * Mostrar la información del usuario logueado
     */
    @GetMapping("/verUsuario")
    public String verUsuario(Model model) {
        // Obtener el usuario logueado
        Usuario usuarioLogueado = usuarioService.obtenerUsuarioLogueado();  
        model.addAttribute("usuario", usuarioLogueado);

        List<Establecimiento> listaEstablecimientos;

        // Verificar si el usuario tiene el rol ADMIN
        if ("ADMIN".equals(usuarioLogueado.getRole())) {
            // Si es ADMIN, mostrar todos los establecimientos
            listaEstablecimientos = establecimientoService.findAllEstablecimientos();
        } else {
            // Si no es ADMIN, mostrar solo los establecimientos asociados al usuario
            listaEstablecimientos = establecimientoService.findEstablecimientosByUsuarioId(usuarioLogueado.getId());
        }

        model.addAttribute("listaEstablecimientos", listaEstablecimientos);
        return "verUsuario";  // Vista que muestra la información del usuario
    }

    /**
     * Mostrar el formulario de registro de usuario
     */
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    /**
     * Registrar un nuevo usuario
     */
    @PostMapping("/registro")
    public String registrarUsuario(Usuario usuario, Model model) {
        // Verificar si el email ya está en uso
        if (usuarioService.existeUsuarioConEmail(usuario.getEmail())) {
            model.addAttribute("error", "El email ya está en uso.");
            model.addAttribute("usuario", usuario);  // Mantener los datos ya ingresados
            return "registro";  // Volver al formulario con el mensaje de error
        }

        // Asignar el rol por defecto "USER" si no está asignado
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            usuario.setRole("USER");
        }

        // Registrar al usuario
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registroExitoso";  // Redirigir al login con un mensaje de éxito
    }

    /**
     * Mostrar el formulario de actualización de usuario
     * Se recibe el 'id' para obtener el usuario a actualizar
     */
    @GetMapping("/actualizarUsuario/{id}")
    public String mostrarFormularioActualizarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);  // Obtener el usuario por su ID
        model.addAttribute("usuario", usuario);  // Pasar el usuario al formulario
        return "actualizarUsuario";  // Vista para editar la información del usuario
    }

    /**
     * Procesar la actualización de los datos del usuario
     */
    @PostMapping("/actualizarUsuario")
    public String actualizarUsuario(@ModelAttribute Usuario usuarioFormulario) {
        // Obtener el usuario logueado
        Usuario usuarioLogueado = usuarioService.obtenerUsuarioLogueado();

        // Solo actualizar si el usuario está autorizado a hacerlo
        if (usuarioLogueado != null) {
            // Actualizar los campos de nombre y email si han cambiado
            if (usuarioFormulario.getNombre() != null && !usuarioFormulario.getNombre().equals(usuarioLogueado.getNombre())) {
                usuarioLogueado.setNombre(usuarioFormulario.getNombre());
            }
            
            if (usuarioFormulario.getTelefono() != null && !usuarioFormulario.getTelefono().equals(usuarioLogueado.getTelefono())) {
                usuarioLogueado.setTelefono(usuarioFormulario.getTelefono());
            }

            // Si se ha proporcionado una nueva contraseña, actualizarla
            if (usuarioFormulario.getPassword() != null && !usuarioFormulario.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(usuarioFormulario.getPassword());  // Cifra la nueva contraseña
                usuarioLogueado.setPassword(encodedPassword);  // Asigna la contraseña cifrada
            }

            // Guardar el usuario actualizado en la base de datos
            usuarioService.actualizarUsuario(usuarioLogueado);
        }

        return "redirect:/verUsuario";  // Redirigir a la página de ver usuario
    }

    /**
     * Eliminar el usuario logueado
     */
    @DeleteMapping("/eliminarUsuario")
    public String deleteUsuario() {
        Usuario usuarioLogueado = usuarioService.obtenerUsuarioLogueado();  // Obtener el usuario logueado
        usuarioService.deleteUsuarioById(usuarioLogueado.getId());  // Eliminar el usuario logueado
        return "redirect:/login";  // Redirigir al login después de eliminar la cuenta
    }
}
