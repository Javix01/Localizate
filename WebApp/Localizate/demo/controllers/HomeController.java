package com.Localizate.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

	public HomeController() {
		// TODO Auto-generated constructor stub
		System.out.println("\t Builder of " + this.getClass().getSimpleName());
	}
	@GetMapping("/")
	public String index() {
		System.out.println("\n\t Recojo la peticion a / (http://localhost:8080/)\n"
				+ "\t Devuelvo la vista index\n"
				+ "\t index.html esta en Templates");
		return "index";
	}
	
	@GetMapping("/holaTh")
	public String holaPage (Model model) {
		
		System.out.println("\n\t Recojo la peticion a / (http://localhost:8080/holaTh)\n"
				+ "\t Devuelvo la vista holaTh\n"
				+ "\t holaTh.html esta en Templates\n"
				+ "\t aniado la variable Bienvenida al model y renderizo con Thymeleaf");
		String texto = "Hola mundo con Thymeleaf + Spring";
		model.addAttribute("Bienvenida", texto);
		return "holaTh";
	}
	

}
