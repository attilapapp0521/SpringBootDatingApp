package com.datingapp.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdminDto {
	private Long id;
	private String username;
	private List<String> roles;
}
