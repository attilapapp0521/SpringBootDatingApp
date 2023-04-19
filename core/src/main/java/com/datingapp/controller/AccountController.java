package com.datingapp.controller;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.datingapp.dto.LoginDto;
import com.datingapp.dto.RegisterDto;
import com.datingapp.dto.UserDto;
import com.datingapp.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/account/")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
	private final AccountService accountService;

	@PostMapping("register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto) {
		log.debug("Registration is in progress...");
		if (userExists(registerDto.getUsername())) {
			log.warn("Registration is failed. The username is taken");
			throw new IllegalStateException("Username is taken"); // BAD_REQUEST
		}
		accountService.register(registerDto);
		log.debug("Registration is successful.");
		return new ResponseEntity<>(CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@Valid @RequestBody LoginDto loginDto) {
		log.debug("Login is in progress...");

		 return new ResponseEntity<>(accountService.login(loginDto), OK);
	}

	private boolean userExists(String username) {
		return accountService.userExists(username);
	}

	@GetMapping()
	public ResponseEntity<Void> test(){
		return new  ResponseEntity<>(OK);
	}

}
