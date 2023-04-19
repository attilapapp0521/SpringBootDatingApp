package com.datingapp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.datingapp.domain.UserEntity;
import com.datingapp.dto.AdminDto;
import com.datingapp.enumeration.Roles;
import com.datingapp.mapper.AdminMapper;
import com.datingapp.service.AdminService;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	private final UserServiceImpl userServiceImpl;
	private final AccountServiceImpl accountServiceImpl;
	private final AdminMapper adminMapper;

	//TODO PAGE használata
	public List<AdminDto> getUsersWithRoles() {
		List<UserEntity> users = userServiceImpl.getAllUser();
		List<AdminDto> adminDtoList = new ArrayList<>();

		for (UserEntity user : users) {
			AdminDto adminDto = adminMapper.userMapToAdminDto(user);
			adminDto.setRoles(user.getRoles().stream().map(Enum::toString).map(role -> role.substring(5))
					.collect(Collectors.toList()));
			adminDtoList.add(adminDto);
		}
		return adminDtoList;
	}

	public void editRoles(String username, String roles) {
		UserEntity user = userServiceImpl.findUserByUsername(username);
		String[] rolesSlice = roles.split(",");

		if (user == null) {
			throw new IllegalStateException("Entity not found"); //NOT_FOUND
		} else if (roles.isEmpty()) {
			throw new IllegalStateException("At least one role must be assigned"); //BAD_REQUEST
		}

		user.setRoles(Arrays.stream(rolesSlice)
				.map(name -> Roles.valueOf("ROLE_" + name)).collect(Collectors.toList()));
		accountServiceImpl.saveUser(user);
	}
}
