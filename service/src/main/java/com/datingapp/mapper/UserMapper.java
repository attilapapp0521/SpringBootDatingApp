package com.datingapp.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.datingapp.domain.ImageEntity;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.MemberDto;
import com.datingapp.dto.RegisterDto;
import com.datingapp.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserEntity mapToEntity(RegisterDto dto);

	@Mapping(target = "photoUrl", source = "photos", qualifiedByName = "toMainImageUrl")
	UserDto mapToDto(UserEntity entity);

	@Mapping(target = "photoUrl", source = "photos", qualifiedByName = "toMainImageUrl")
	MemberDto mapToMemberDto(UserEntity user);

	@Named("toMainImageUrl")
	default String toMainImageUrl(List<ImageEntity> images) {
		if (images.isEmpty()) {
			return null;
		}
		return images.stream().filter(ImageEntity::getIsMain)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Main image not selected!"))
				.getUrl();
	}
}
