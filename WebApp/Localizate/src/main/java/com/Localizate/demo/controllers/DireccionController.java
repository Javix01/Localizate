package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Localizate.demo.domain.Direccion;
import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.DireccionService;
import com.Localizate.demo.services.UsuarioService;


@Controller
public class DireccionController {
	
	
	private final DireccionService direccionService;
	private final UsuarioService usuarioService;

	public DireccionController(DireccionService direccionService, UsuarioService usuarioService) {
		this.direccionService = direccionService;
		// TODO Auto-generated constructor stub
		this.usuarioService = usuarioService;
	}
		
	 /**
    ADD DIRECCION     
   */
    // Metodo para mostrar el formulario de aniadir direccion
	@GetMapping("/addDireccion/{usuarioId}")
	public String mostrarFormularioAddDireccion(@PathVariable Long usuarioId, Model model) {
	    // Logica para mostrar el formulario de aniadir direccion
		System.out.println("DireccionController "+ " @GetMapping(\"/addDireccion/{usuarioId}\")");
		
		//optional a lo bruto, mejorarlo...
		model.addAttribute("usuario",usuarioService.findUsuarioById(usuarioId).get() );
		
	    return "addDireccion";
	}

    // MEtodo para procesar la solicitud de aniadir usuario
    @PostMapping("/guardarDireccion/{usuarioId}")
    public String guardarDireccion(@PathVariable Long usuarioId, Direccion direccion, Model model) {
        // Deberias implementar la logica en tu servicio
        // Podemos asumir que tienes un servicio llamado direccionService para gestionar las direcciones
    	System.out.println("\tDireccionController" + "@PostMapping(\"/guardarDireccion\")");
    	
        // Asociar la direccion al usuario
    	direccion.setUsuario(usuarioService.findUsuarioById(usuarioId).get());
    	// Persistir la direccion.  
    	direccionService.crearDireccion(direccion);       
    	
        // Redirigir a la lista de usuarios despues de agregar uno nuevo. Se mostrara la nueva direccion para el usuario.
        return "redirect:/listUsuarios";
    }
    
    /**
     * BORRAR USUARIO
     * 
     */
    
    @DeleteMapping ("deleteDireccion/{id}")
    public String deleteDireccion (@PathVariable Long id) {
    	System.out.println("\t direccionController::deleteDireccion");
        // Deberias implementar la logica en tu servicio
    	direccionService.deleteDireccionById(id);
        // Deberias, tal vez, devolver una vista u otra segun el resultado
    	
    	return "redirect:/listUsuarios";
    }
    
    
    /**
     * ACTUALIZAR DIRECCION
     * 
     */
    
    
    // Metodo para mostrar el formulario de actualizacion de usuario
//   Ejemplo url: http://localhost:8080/actualizarDireccion/1?usuarioId=1    
    @GetMapping("/actualizarDireccion/{id}")
    public String mostrarFormularioActualizarDireccion(@PathVariable Long id, @RequestParam("usuarioId") Long usuarioId , Model model) {
    	
    	System.out.println("\t @GetMapping DireccionController::mostrarFormularioActualizarDireccion");
    	
    	//para acceder a la direccion en la vista.
    	model.addAttribute("direccion",direccionService.findDireccionById(id).get() );
    	
    	//para acceder a la direccion en la vista.
    	System.out.println("@RequestParam Long usuarioId: " + usuarioId);    	
    	model.addAttribute("usuario",usuarioService.findUsuarioById(usuarioId).get());    	
    	return "actualizarDireccion";
    	
    }
    
    // Metodo para procesar la solicitud de actualización de usuario
    @PostMapping("/actualizarDireccion/{id}")
    public String actualizarDireccion(@PathVariable Long id, @ModelAttribute Direccion direccion ) {
    	
    	System.out.println("\t @PostMapping DireccionController::actualizarDireccion");
    	    	   	
    	// le asociamos nuevamente el usuario. Ya que direccion va a venir sin usuario.
    	// Recuperar el usuario asociado usando el ID enviado en el formulario
        Long usuarioId = direccion.getUsuario().getId();
        Usuario usuario = usuarioService.findUsuarioById(usuarioId).get();
        
       ////le asociamos nuevamente el usuario. Ya que direccion va a venir sin todos los campos de usuario.
    	direccion.setUsuario(usuario);  
    	
    	System.out.println(direccion.toString());

        direccionService.actualizarDireccion(direccion);
        return "redirect:/listUsuarios";
    	
    }

}
