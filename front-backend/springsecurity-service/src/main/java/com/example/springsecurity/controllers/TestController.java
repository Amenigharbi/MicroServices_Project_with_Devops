package com.example.springsecurity.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	// accessible à tout le monde, sans restriction de rôle
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	// accessible aux utilisateurs ayant les rôles USER, TOURIST ou ADMIN
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('TOURIST') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}
//accessible uniquement aux utilisateurs ayant le rôle TOURIST
	@GetMapping("/TOURIST")
	@PreAuthorize("hasRole('TOURIST')")
	public String TouristAccess() {
		return "TOURIST Board.";
	}
//accessible uniquement aux utilisateurs ayant le rôle ADMIN
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
	//accessible aux utilisateurs ayant les rôles USER, TOURIST ou ADMIN
	@PostMapping("/auth")
	@PreAuthorize("hasRole('USER') or hasRole('TOURIST') or hasRole('ADMIN')")
	public String userrAccess() {
		return "User Content.";
	}
}
