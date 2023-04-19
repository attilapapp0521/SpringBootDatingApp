package com.datingapp.dto;

import lombok.Data;

@Data
public class UserDto {
	private String username;
	private String token;
	private String photoUrl;
	private String knownAs;
	private String gender;
}
