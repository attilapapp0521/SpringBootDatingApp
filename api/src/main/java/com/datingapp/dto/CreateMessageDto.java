package com.datingapp.dto;

import lombok.Data;

@Data
public class CreateMessageDto {
	private String recipientUsername;
	private String content;
}
