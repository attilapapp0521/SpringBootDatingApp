package com.datingapp.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.datingapp.domain.ImageEntity;
import com.datingapp.domain.MessageEntity;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.MessageDto;

@Mapper(componentModel = "spring")
public interface MessageMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "messageSent", expression = ("java(now())"))
	MessageEntity mapToEntity(UserEntity sender, UserEntity recipient, String content);

	@Mapping(target = "senderId", source = "sender.id")
	@Mapping(target = "senderUsername", source = "sender.username")
	@Mapping(target = "senderPhotoUrl", source = "sender.photos", qualifiedByName = "getImageUrl")
	@Mapping(target = "recipientId", source = "recipient.id")
	@Mapping(target = "recipientUsername", source = "recipient.username")
	@Mapping(target = "recipientPhotoUrl", source = "recipient.photos", qualifiedByName = "getImageUrl")
	MessageDto mapToDto(MessageEntity message);

	default LocalDateTime now() {
		return LocalDateTime.now();
	}

	@Named("getImageUrl")
	default String getImageUrl(List<ImageEntity> images) {
		if (images.isEmpty()) {
			return null;
		}
		return images.stream().filter(ImageEntity::getIsMain)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Main image not selected!"))
				.getUrl();
	}
}
