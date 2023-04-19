package com.datingapp.service.impl;

import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.datingapp.domain.UserEntity;
import com.datingapp.domain.UserLikeEntity;
import com.datingapp.dto.LikeDto;
import com.datingapp.mapper.LikesMapper;
import com.datingapp.repository.LikesRepository;
import com.datingapp.service.LikesService;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {
	private final UserServiceImpl userServiceImpl;
	private final LikesRepository likesRepository;
	private final LikesMapper likesMapper;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void addLike(String username) {
		UserEntity likedUser = userServiceImpl.findUserByUsername(username);
		UserEntity sourceUser = userServiceImpl.findUserByUsername(userServiceImpl.getAuthenticatedUserName());

		if (likedUser == null) {
			throw new IllegalStateException("Entity not found"); //NOT_FOUND
		} else if (sourceUser.getUsername().equals(username)) {
			throw new IllegalStateException("You cannot like yourself"); //BAD_REQUEST
		} else if (isLiked(sourceUser, likedUser)) {
			throw new IllegalStateException("You already like this user"); //BAD_REQUEST
		}

		UserLikeEntity userLike = likesMapper.mapUsersToEntity(sourceUser, likedUser);
		likesRepository.save(userLike);
	}

	public boolean isLiked(UserEntity sourceUser, UserEntity likedUser) {

		UserLikeEntity userLike = likesRepository.
				isLikedUser(sourceUser.getId(), likedUser.getId());
		return userLike != null;
	}

	public Page<LikeDto> getUsersLikes(String predicate, Pageable pageable) {
		UserEntity user = userServiceImpl.findUserByUsername(userServiceImpl.getAuthenticatedUserName());

		if (predicate.equals("liked")) {
			return likesRepository.getUserLikes(user.getUsername(), pageable)
					.map(this::getLikeDto);
		}
		return likesRepository.getUserLikesBy(user.getUsername(), pageable)
				.map(this::getLikeDto);
	}

	private LikeDto getLikeDto(UserEntity user) {

		LikeDto likeDto = likesMapper.mapToDto(user);
		likeDto.setAge(userServiceImpl.calculateAge(user.getDateOfBirth()));
//		likeDto.setPhotoUrl((String) userServiceImpl.getPhotos(user).getSecond());

		return likeDto;
	}
}
