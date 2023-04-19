package com.datingapp.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MessageDto {
	private long id;
	private long senderId;
	private String senderUsername;
	private String senderPhotoUrl;
	private String content;
	private long recipientId;
	private String recipientUsername;
	private String recipientPhotoUrl;
	private LocalDateTime dateRead;
	private LocalDateTime messageSent;
}
