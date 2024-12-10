package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(Usuario usuario, Model model) {
        // Verificar si el email ya está en uso
        if (usuarioService.existeUsuarioConEmail(usuario.getEmail())) {
            model.addAttribute("error", "El email ya está en uso.");
            model.addAttribute("usuario", usuario);  // Mantener los datos ya ingresados
            return "registro";  // Volver al formulario con el mensaje de error
        }

        // Verificar si el usuario tiene un rol asignado
        if (usuario.getRole() == null || usuario.getRole().isEmpty()) {
            // Asignar el rol por defecto, por ejemplo "USER"
            usuario.setRole("USER");
        }

        // Registrar al usuario
        usuarioService.registrarUsuario(usuario);
        
        // Redirigir a la página de login con un mensaje de éxito
        return "redirect:/login?registroExitoso";
    }
}