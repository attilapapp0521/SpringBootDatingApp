package com.datingapp.mapper;

import org.mapstruct.Mapper;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.AdminDto;

@Mapper(componentModel = "spring")
public interface AdminMapper {
	AdminDto userMapToAdminDto(UserEntity user);
}
