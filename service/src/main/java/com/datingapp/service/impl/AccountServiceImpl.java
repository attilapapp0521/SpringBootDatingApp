package com.datingapp.service.impl;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.LoginDto;
import com.datingapp.dto.RegisterDto;
import com.datingapp.dto.UserDto;
import com.datingapp.enumeration.Roles;
import com.datingapp.jwt.JwtProvider;
import com.datingapp.mapper.UserMapper;
import com.datingapp.repository.UserRepository;
import com.datingapp.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final UserMapper userMapper;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private UserServiceImpl userServiceImpl;

	public boolean userExists(String username) {
		return userRepository.getUserByUsername(username) != null;
	}

	public void register(RegisterDto registerDto) {
		String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
		UserEntity user = userMapper.mapToEntity(registerDto);
		if (user.getUsername().equals("admin")) {
			user.getRoles().add(Roles.ROLE_ADMIN);
		} else {
			user.getRoles().add(Roles.ROLE_USER);
		}
		user.setPassword(encodedPassword);
		user.setCreated(LocalDateTime.now());
		user.setLastActive(LocalDateTime.now());
		saveUser(user);
		logger.debug("New user (username: " + user.getUsername() + ") saved in database.");

	}

	public void saveUser(UserEntity user) {
		userRepository.save(user);
	}

	public UserDto login(LoginDto loginDto) {
		UserEntity user = userRepository.getUserByUsername(loginDto.getUsername());
		if (user == null) {
			logger.warn("Username failed");
			throw new IllegalStateException("Invalid username");  //BAD_REQUEST
		} else if (loginDto.getPassword() == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
			logger.warn("Invalid password");
			throw new IllegalStateException("Invalid password"); //BAD_REQUEST
		}

		String token = jwtProvider.generateToken(user);
		user.setLastActive(LocalDateTime.now());
		UserDto userDto = userMapper.mapToDto(user);
		userDto.setToken(token);
		//		userDto.setPhotoUrl((String) userServiceImpl.getPhotos(user).getSecond());
		userRepository.save(user);
		logger.debug("Login is successful.");

		return userDto;
	}

}
