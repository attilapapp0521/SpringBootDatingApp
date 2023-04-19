package com.datingapp.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.datingapp.domain.ImageEntity;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.ImageDto;
import com.datingapp.dto.MemberDto;
import com.datingapp.dto.MemberUpdateDto;
import com.datingapp.helpers.UserParams;
import com.datingapp.mapper.ImageMapper;
import com.datingapp.mapper.UserMapper;
import com.datingapp.repository.ImageRepository;
import com.datingapp.repository.UserRepository;
import com.datingapp.service.ImageService;
import com.datingapp.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UserMapper userMapper;
	private final ImageMapper imageMapper;
	private final ImageService imageService;
	private final ImageRepository imageRepository;

	public UserEntity findUserByUsername(String username) {
		UserEntity user = userRepository.getUserByUsername(username);
		if (user == null) {
			logger.warn("User was not found.");
		}
		return user;
	}

	public Page<MemberDto> getUsers(UserParams userParams, Pageable pageable) {
		UserEntity user = findUserByUsername(getAuthenticatedUserName());
		userParams.setCurrentUserName(user.getUsername());

		if (userParams.getGender() == null) {
			userParams.setGender(user.getGender().equals("male") ? "female" : "male");
		}

		LocalDateTime minDob = LocalDateTime.now().minusYears(userParams.getMaxAge() + 1);
		LocalDateTime maxDob = LocalDateTime.now().minusYears(userParams.getMinAge());

		return userRepository.findAllUser(userParams.getGender(), minDob,
				maxDob, userParams.getCurrentUserName(), userParams.getOrderBy(),
				pageable).map(this::getMemberDto);
	}

	public MemberDto getUser(String username) {
		UserEntity user = findUserByUsername(username);
		return getMemberDto(user);
	}

	public MemberDto getMemberDto(UserEntity user) {
		if (user != null) {
			MemberDto memberDto = userMapper.mapToMemberDto(user);
			memberDto.setAge(calculateAge(user.getDateOfBirth()));
			logger.debug("User was found");
			//			memberDto.setPhotos((List<ImageDto>) photos.getFirst());
			//			memberDto.setPhotoUrl((String) photos.getSecond());
			return memberDto;
		}

		return null;
	}

	public int calculateAge(LocalDateTime dateOfBirth) {
		int age = 0;
		if (dateOfBirth != null) {
			LocalDate today = LocalDate.now();
			age = today.getYear() - dateOfBirth.getYear();
			if (dateOfBirth.toLocalDate().isAfter(today.minusYears(age)))
				age--;
		}

		return age;
	}

	//	public Pair<List<ImageDto>, String> getPhotos(UserEntity user) {
	//		List<ImageDto> photoDtoList = new ArrayList<>();
	//		String mainPhotoUrl = "";
	//		for (ImageEntity photo : user.getPhotos()) {
	//			ImageDto photoDto = imageMapper.mapToDto(photo);
	//			if (photo.isMain()) {
	//				mainPhotoUrl = photo.getUrl();
	//			}
	//			photoDtoList.add(photoDto);
	//		}
	//		return new Pair<>(photoDtoList, mainPhotoUrl);
	//	}

	public void updateUser(MemberUpdateDto memberUpdateDto) {

		String username = getAuthenticatedUserName();
		UserEntity overwriteUser = updateValues(memberUpdateDto,
				userRepository.getUserByUsername(username));
		logger.debug("User has updated");
		userRepository.save(overwriteUser);
	}

	public String getAuthenticatedUserName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private UserEntity updateValues(MemberUpdateDto memberUpdateDto, UserEntity user) {

		user.setIntroduction(memberUpdateDto.getIntroduction());
		user.setLookingFor((memberUpdateDto.getLookingFor()));
		user.setInterests(memberUpdateDto.getInterests());
		user.setCity(memberUpdateDto.getCity());
		user.setCountry(memberUpdateDto.getCountry());

		return user;
	}

	public ImageDto addImage(MultipartFile multipartFile) {
		UserEntity user = findUserByUsername(getAuthenticatedUserName());
		ImageEntity image = imageService.uploadImage(multipartFile);
		image.setIsMain(user.getPhotos().isEmpty());
		image.setUser(user);

		return imageMapper.mapToDto(imageRepository.save(image));
	}

	public void setMainPhoto(Long photoId) {
		UserEntity user = findUserByUsername(getAuthenticatedUserName());
		imageService.setMainImage(photoId, user.getPhotos());
	}

	public void deletePhoto(Long photoId) {
		imageService.deleteImage(photoId);

	}

	public List<UserEntity> getAllUser() {
		return userRepository.findAll();
	}

}
