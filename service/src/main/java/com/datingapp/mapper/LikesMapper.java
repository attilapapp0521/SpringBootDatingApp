package com.datingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.datingapp.domain.UserEntity;
import com.datingapp.domain.UserLikeEntity;
import com.datingapp.dto.LikeDto;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface LikesMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "sourceUser", source = "sourceUser")
	@Mapping(target = "likedUser", source = "likedUser")
	UserLikeEntity mapUsersToEntity(UserEntity sourceUser, UserEntity likedUser);

	LikeDto mapToDto(UserEntity user);
}
