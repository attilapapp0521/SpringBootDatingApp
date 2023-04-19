package com.datingapp.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.datingapp.dto.CreateMessageDto;
import com.datingapp.dto.MessageDto;

public interface MessageService {
	MessageDto createMessage(CreateMessageDto createMessageDto);

	Page<MessageDto> getMessagesForUsers(String container, Pageable pageable);

	List<MessageDto> getMessageThread(String username);

	void deleteMessage(Long id);
}
