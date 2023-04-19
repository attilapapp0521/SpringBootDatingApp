package com.datingapp.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datingapp.dto.LikeDto;
import com.datingapp.extension.HttpExtension;
import com.datingapp.service.LikesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {
	private final LikesService likesService;

	@PostMapping("/{username}")
	public ResponseEntity<Void> addLike(@PathVariable String username){
		 likesService.addLike(username);

		 return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<LikeDto>> getUserLikes(@RequestParam String predicate, Pageable pageable){

		Page<LikeDto> users = likesService.getUsersLikes(predicate, pageable);
		HttpHeaders httpHeaders = new HttpHeaders();
		//TODO ez a rész nem működik a HttpExtension rész rossz adatot ad vissza, ezt másképp kell megoldani
		HttpExtension.addPaginationHeader(httpHeaders, users.getNumber(),
				users.getNumberOfElements(), users.getTotalElements(),
				users.getTotalPages());

		return ResponseEntity.ok().headers(httpHeaders).body(users.getContent());
	}
}
