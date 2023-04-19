package com.datingapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MemberDto {
	private Long id;
	private String username;
	private String photoUrl;
	private Integer age;
	private String knownAs;
	private LocalDateTime created;
	private LocalDateTime lastActive;
	private String gender;
	private String introduction;
	private String lookingFor;
	private String interests;
	private String city;
	private String country;
	private List<ImageDto> photos;
}
