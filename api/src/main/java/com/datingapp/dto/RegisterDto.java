package com.datingapp.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RegisterDto {
	@NotBlank(message = "Username is mandatory")
	String username;
	@NotBlank(message = "KnownAs is mandatory")
	String knownAs;
	@NotBlank(message = "Gender is mandatory")
	String gender;
	@NotNull(message = "Date of birth is mandatory" )
	LocalDateTime dateOfBirth;
	@NotBlank(message = "City is mandatory")
	String city;
	@NotBlank(message = "Country is mandatory")
	String country;
	@NotBlank(message = "Password is mandatory")
	String password;
}
