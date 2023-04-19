package com.datingapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.MemberDto;
import com.datingapp.dto.MemberUpdateDto;
import com.datingapp.dto.ImageDto;
import com.datingapp.helpers.UserParams;

public interface UserService {
	Page<MemberDto> getUsers(UserParams userParams, Pageable pageable);

	MemberDto getUser(String username);

	void updateUser(MemberUpdateDto memberUpdateDto);

	ImageDto addImage(MultipartFile multipartFile);

	void setMainPhoto(Long photoId);

	void deletePhoto(Long photoId);

	UserEntity findUserByUsername(String username);
}
