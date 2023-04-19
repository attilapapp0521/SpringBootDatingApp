package com.datingapp.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datingapp.dto.CreateMessageDto;
import com.datingapp.dto.MessageDto;
import com.datingapp.extension.HttpExtension;
import com.datingapp.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessagesController {
	private final MessageService messageService;

	@PostMapping
	public ResponseEntity<MessageDto> createMessage(@RequestBody CreateMessageDto createMessageDto) {
		log.info("Message saving from user...");
		return new ResponseEntity<>(messageService.createMessage(createMessageDto), CREATED);
	}

	@GetMapping
	public ResponseEntity<List<MessageDto>> getMessagesForUsers(String container, Pageable pageable) {
		log.info("Get messaging...");
		Page<MessageDto> users = messageService.getMessagesForUsers(container, pageable);
		HttpHeaders httpHeaders = new HttpHeaders();
		//TODO nem működik
		HttpExtension.addPaginationHeader(httpHeaders, users.getNumber(),
				users.getNumberOfElements(), users.getTotalElements(),
				users.getTotalPages());

		return ResponseEntity.ok().headers(httpHeaders).body(users.getContent());
	}

	@GetMapping("thread/{username}")
	public ResponseEntity<List<MessageDto>> getMessageThread(@PathVariable String username) {
		log.info("Get thread message...");
		return new ResponseEntity<>(messageService.getMessageThread(username), OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
		log.info("Delete is in progress...");
		 messageService.deleteMessage(id);
		 return new ResponseEntity<>(NO_CONTENT);
	}
}
