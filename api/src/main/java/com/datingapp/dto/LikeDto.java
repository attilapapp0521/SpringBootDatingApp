package com.datingapp.dto;

import lombok.Data;

@Data
public class LikeDto {
	private long id;
	private String username;
	private int age;
	private String knownAs;
	private String photoUrl;
	private String city;
}
