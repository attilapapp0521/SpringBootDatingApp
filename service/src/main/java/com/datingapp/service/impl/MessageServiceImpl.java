package com.datingapp.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.datingapp.domain.MessageEntity;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.CreateMessageDto;
import com.datingapp.dto.MessageDto;
import com.datingapp.mapper.MessageMapper;
import com.datingapp.repository.MessageRepository;
import com.datingapp.service.MessageService;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	private final UserServiceImpl userServiceImpl;
	private final MessageRepository messageRepository;
	private final MessageMapper messageMapper;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public MessageDto createMessage(CreateMessageDto createMessageDto) {
		UserEntity sender = userServiceImpl.findUserByUsername(userServiceImpl.getAuthenticatedUserName());
		UserEntity recipient = userServiceImpl.findUserByUsername(createMessageDto.getRecipientUsername());

		if (recipient == null) {
			logger.warn("Not found recipient");
			throw new IllegalStateException("Not found"); //NOT_FOUND
		} else if (sender.getUsername().equals(createMessageDto.getRecipientUsername())) {
			logger.warn("The recipient and the sender is same user ");
			throw new IllegalStateException("You cannot send messages to yourself"); //BAD_REQUEST
		}
		MessageEntity message = messageMapper.mapToEntity(sender, recipient, createMessageDto.getContent());
		messageRepository.save(message);

		logger.info("The message has saved");
		return getMessageDto(message);
	}

	private MessageDto getMessageDto(MessageEntity message) {
		MessageDto messageDto = messageMapper.mapToDto(message);
//		messageDto.setSenderPhotoUrl((String) userServiceImpl.getPhotos(message.getSender()).getSecond());
//		messageDto.setRecipientPhotoUrl((String) userServiceImpl.getPhotos(message.getRecipient()).getSecond());

		return messageDto;
	}

	public Page<MessageDto> getMessagesForUsers(String container, Pageable pageable) {
		String username = userServiceImpl.getAuthenticatedUserName();

		if (container != null && container.equals("Inbox")) {
			logger.info("Get inbox messaging...");
			Page<MessageEntity> messages = messageRepository.getMessagesForUserInbox(username, pageable);
			return messages.map(this::getMessageDto);
		} else if (container != null && container.equals("Outbox")) {
			logger.info("Get outbox messaging...");
			Page<MessageEntity> messages = messageRepository.getMessagesForUserOutbox(username, pageable);
			return messages.map(this::getMessageDto);
		}
		logger.info("Get unread messaging...");
		Page<MessageEntity> messages = messageRepository.getMessagesForUserUnread(username, pageable);
		return messages.map(this::getMessageDto);
	}

	public List<MessageDto> getMessageThread(String recipientUsername) {
		String currentUsername = userServiceImpl.getAuthenticatedUserName();
		setReadMessages(currentUsername);

		List<MessageEntity> messages = messageRepository.getMessageThread(currentUsername, recipientUsername);
		return messages.stream()
				.map(this::getMessageDto).collect(Collectors.toList());
	}

	private void setReadMessages(String username) {
		List<MessageEntity> messages = messageRepository.findAll();
		for (MessageEntity message : messages) {
			if (message.getDateRead() == null &&
					message.getRecipient().getUsername().equals(username)) {
				message.setDateRead(LocalDateTime.now());
				messageRepository.save(message);
				logger.info("Set message read in " + message.getDateRead());
			}
		}

	}

	public void deleteMessage(Long id) {
		String username = userServiceImpl.getAuthenticatedUserName();
		MessageEntity message = messageRepository.getById(id);
		if (message == null) {
			logger.info("Message was not found");
			throw new IllegalStateException("Not found"); //NOT_FOUND
		} else if (!message.getSender().getUsername().equals(username)
				&& !message.getRecipient().getUsername().equals(username)) {
			logger.info("Message is not possible this user");
			throw new IllegalStateException("UNAUTHORIZED"); // UNAUTHORIZED
		}

		if (message.getSender().getUsername().equals(username)) {
			message.setSenderDeleted(true);
			logger.info("Sender has deleted the message");
		} else if (message.getRecipient().getUsername().equals(username)) {
			message.setRecipientDeleted(true);
			logger.info("Recipient has deleted the message");
		}

		if (message.isSenderDeleted() && message.isRecipientDeleted()) {
			messageRepository.delete(message);
			logger.info("Message is deleted in database");
		}

	}
}
