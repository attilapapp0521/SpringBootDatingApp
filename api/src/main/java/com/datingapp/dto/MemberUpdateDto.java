package com.datingapp.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class MemberUpdateDto {
	private String introduction;
	private String lookingFor;
	private String interests;
	@NotBlank(message = "The city field cannot be empty")
	private String city;
	@NotBlank(message = "The country field cannot be empty")
	private String country;
}
