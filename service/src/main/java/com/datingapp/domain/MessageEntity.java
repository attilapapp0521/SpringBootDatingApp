package com.datingapp.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "message")
@Getter
@Setter
@EqualsAndHashCode
public class MessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	private UserEntity sender;
	@ManyToOne
	private UserEntity recipient;
	private String content;
	private LocalDateTime dateRead;
	private LocalDateTime messageSent;
	private boolean senderDeleted;
	private boolean recipientDeleted;
}
