package com.datingapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.datingapp.dto.LikeDto;

public interface LikesService {
	void addLike(String username);
	Page<LikeDto> getUsersLikes(String predicate, Pageable pageable);
}
