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
		return "login";
	}
}