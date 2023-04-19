package com.datingapp.mapper;

import org.mapstruct.Mapper;
import com.datingapp.domain.ImageEntity;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.ImageDto;

@Mapper(componentModel = "spring")
public interface ImageMapper {
	ImageDto mapToDto(ImageEntity image);

}
