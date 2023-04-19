package com.datingapp.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.datingapp.domain.ImageEntity;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.ImageDto;

public interface ImageService {

	ImageEntity uploadImage(MultipartFile multipartFile);

	ImageEntity getImage(Long id);

	void setMainImage(Long imageId, List<ImageEntity> images);

	void deleteImage(Long id);
}
