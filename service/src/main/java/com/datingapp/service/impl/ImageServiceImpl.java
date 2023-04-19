package com.datingapp.service.impl;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.datingapp.domain.ImageEntity;
import com.datingapp.mapper.ImageMapper;
import com.datingapp.repository.ImageRepository;
import com.datingapp.service.ImageService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
	private final Cloudinary cloudinary;
	private final ImageRepository imageRepository;
	private final ImageMapper imageMapper;

	public ImageEntity uploadImage(MultipartFile multipartFile) {
		try {
			var uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(),
					ObjectUtils.asMap("transformation",
							new Transformation<>().height(300).width(300).crop("fill").gravity("face")));
			String publicId = (String) uploadResult.get("public_id");
			String url = (String) uploadResult.get("url");

			ImageEntity image = new ImageEntity();
			image.setPublicId(publicId);
			image.setUrl(url);

			return image;

		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalStateException("Error uploading the image file");
		}
	}

	public ImageEntity getImage(Long id) {
		return imageRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(
				String.format("Image not found, id: %d", id), 1));
	}

	//TODO Null nem mehet vissza (exception kezelés)
	public String getMainImageUrl(List<ImageEntity> images) {
		ImageEntity image = images.stream()
				.filter(ImageEntity::getIsMain).findFirst()
				.orElse(null);

		return image.getUrl();
	}

	public void setMainImage(Long imageId, List<ImageEntity> images) {
		ImageEntity newImage = imageRepository.getById(imageId);

		if (newImage.getIsMain()) {
			throw new IllegalStateException("This is already the main photo!");
		}

		images.stream().filter(ImageEntity::getIsMain).forEach(actualImage -> {
			actualImage.setIsMain(false);
			imageRepository.save(actualImage);
		});

		newImage.setIsMain(true);
		imageRepository.save(newImage);
	}

	public void deleteImage(Long id) {
		ImageEntity image = getImage(id);

		try {
			cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException("Image delete from cloud is failed! ");
		}
		imageRepository.delete(image);
	}
}
