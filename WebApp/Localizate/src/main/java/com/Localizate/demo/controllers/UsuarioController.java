package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Localizate.demo.domain.Usuario;
import com.Localizate.demo.services.UsuarioService;

@Controller
public class UsuarioController {
	
	
	private final UsuarioService usuarioService;
	
	public UsuarioController(UsuarioService usuarioService) {
		// TODO Auto-generated constructor stub
		System.out.println("\t Builder of " + this.getClass().getSimpleName());
		this.usuarioService=usuarioService;
	}
	
	@GetMapping("/listUsuarios")
	public String listUsuariosPage(Model model) {
		System.out.println("\n\t Recojo la peticion a @GetMapping(\"/listUsuarios\")");		
		
		model.addAttribute("listaUsuarios",usuarioService.findAllUsers());
		return "listUsuarios";		
	}
	
	 /**
    ADD USUARIO     
   */
    // Metodo para mostrar el formulario de aniadir usuario
    @GetMapping("/addUsuario")
    public String mostrarFormularioAddUsuario(Model model) {
    	System.out.println("\n\t @GetMapping(\"/addUsuario\")");
    	// Se pasa un objeto vacío al modelo
        model.addAttribute("usuario", new Usuario()); 
        return "addUsuario"; // Esto coincide con el nombre de tu plantilla Thymeleaf (addUsuario.html)
    }
    
    // Metodo para procesar la solicitud de aniadir usuario
    @PostMapping("/guardarUsuario")
    public String guardarUsuario(Usuario usuario, Model model) {
        // Asumimos que tienes un servicio llamado usuarioService 
    	// para gestionar los usuarios que contiene la logica
    	System.out.println("\n\t @PostMapping(\"/guardarUsuario\")");
    	usuarioService.crearUsuario(usuario);
 
    	// Se puede incluir otro html de confirmacion que indique que se ha guardado con exito
        // Yo lo voy a redirigir a la lista de usuarios directamente despues de agregar uno nuevo.
    	// Lo redirijo a la URL no a la vista .html para que vuelva a hacer el proceso.
        return "redirect:/listUsuarios";
    }
	
    /**
     * Actualizar USUARIO
     * 
     */
    
    // Metodo para mostrar el formulario de actualización de usuario
    @GetMapping("/actualizarUsuario/{id}")
    public String mostrarFormularioActualizarUsuario(@PathVariable Long id, Model model) {
    	System.out.println("\t UsuarioController::mostrarFormularioActualizarUsuario");
    	
    	//para acceder al usuario en la vista
    	model.addAttribute("usuario",usuarioService.findUsuarioById(id).get() );
    	return "actualizarUsuario";
    	
    }
    
    // Método para procesar la solicitud de actualización de usuario
    @PostMapping("/actualizarUsuario/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario) {
    	
    	// Podemos asumir que tienes un servicio llamado usuarioService para gestionar los usuarios
    	//usuario viene sin direcciones.
    	
//    	Explicacion:
//    	Si el objeto Usuario tiene una lista de direcciones, pero no se incluyen campos correspondientes 
//    	a la lista de direcciones en el formulario de actualizacion de usuario, la lista de direcciones 
//    	en el objeto Usuario recibido por el controlador será null. 
//    	Sin embargo, al realizar la operación de actualizacion y guardar en el repositorio, la lista de direcciones no se actualizara a null.
//
//    	La razon es que la vinculacion automática (@ModelAttribute) solo afecta a las propiedades del objeto 
//    	que tienen campos correspondientes en el formulario. 
//    	Si no hay campos en el formulario para la lista de direcciones, 
//    	esa propiedad en el objeto Usuario permanecerá null si no se inicializa explícitamente.
    	
    	//En el servicio solo actualizamos los campos nombres y email de la BD, por tanto, conservamos sus direcciones intactas.
        usuarioService.actualizarUsuario(usuario);
        return "redirect:/listUsuarios";
    	
    }
    
	
    /**
     * Delete USUARIO
     * 
     */
    
    @DeleteMapping("/deleteUsuario/{id}")
    public String deleteUsuario(@PathVariable Long id) {
    	System.out.println("\t usuarioController::deleteUsuario");
    	usuarioService.deleteUsuarioById(id);
    	// Redirigir a la lista de usuarios despues de borrarlo
        return "redirect:/listUsuarios";
    }

	

}
