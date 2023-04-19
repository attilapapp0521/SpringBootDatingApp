package com.datingapp.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.datingapp.dto.AdminDto;
import com.datingapp.service.AdminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
	private final AdminService adminService;

	@GetMapping("/users-with-roles")
	public ResponseEntity<List<AdminDto>> getUsersWithRoles() {
		return new ResponseEntity<>(adminService.getUsersWithRoles(), OK);
	}

	@PostMapping("/edit-roles/{username}")
	public ResponseEntity<Void> editRoles(@PathVariable String username, String roles) {
		adminService.editRoles(username, roles.toUpperCase());
		return new ResponseEntity<>(OK);
	}

	public ResponseEntity<Void> getPhotosForModeration() {
		return new ResponseEntity<>(OK);
	}

}
