package com.datingapp.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.datingapp.dto.ImageDto;
import com.datingapp.dto.MemberDto;
import com.datingapp.dto.MemberUpdateDto;
import com.datingapp.extension.HttpExtension;
import com.datingapp.helpers.UserParams;
import com.datingapp.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;

	@GetMapping
	public ResponseEntity<List<MemberDto>> getUsers(UserParams userParams,
			Pageable pageable) {
		log.debug("Users reading of database");
		if (userParams.getMinAge() < 18 || userParams.getMaxAge() > 120
				|| userParams.getMinAge() == null || userParams.getMaxAge() == null
				|| userParams.getMinAge() > 120 || userParams.getMaxAge() < 18) {
			return new ResponseEntity("The spectrum is between 18 and 120", BAD_REQUEST);
		}
		Page<MemberDto> users = userService.getUsers(userParams, pageable);
		HttpHeaders responseHeaders = new HttpHeaders();
		HttpExtension.addPaginationHeader(responseHeaders, users.getNumber(),
				users.getNumberOfElements(), users.getTotalElements(),
				users.getTotalPages());

		return ResponseEntity.ok().headers(responseHeaders).body(users.getContent());
	}

	@GetMapping("/{username}")
	public ResponseEntity<MemberDto> getUser(@PathVariable String username) {
		log.debug("User reading of database...");
		MemberDto memberDto = userService.getUser(username);
		if (memberDto == null) {
			return new ResponseEntity<>(NOT_FOUND);
		}

		return new ResponseEntity<>(memberDto, OK);
	}

	@PutMapping
	public ResponseEntity<Void> updateUser(@Valid @RequestBody MemberUpdateDto memberUpdateDto) {
		log.debug("User updating...");
		userService.updateUser(memberUpdateDto);
		return new ResponseEntity<>(OK);
	}

	@PostMapping("/add-photo")
	public ResponseEntity<ImageDto> addPhoto(@RequestParam("file") MultipartFile multipartFile) {
		log.debug("Photo adding of database and cloud...");
		ImageDto imageDto = userService.addImage(multipartFile);
		return new ResponseEntity<>(imageDto, CREATED);
	}

	@PutMapping("/set-main-photo/{photoId}")
	public ResponseEntity<Void> setMainPhoto(@PathVariable Long photoId) {
		userService.setMainPhoto(photoId);
		return new ResponseEntity<>(OK);
	}

	@DeleteMapping("/delete-photo/{photoId}")
	public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
		userService.deletePhoto(photoId);
		return new ResponseEntity<>(NO_CONTENT);
	}
}
