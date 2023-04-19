package com.datingapp.service;

import com.datingapp.dto.LoginDto;
import com.datingapp.dto.RegisterDto;
import com.datingapp.dto.UserDto;

public interface AccountService {
	void register(RegisterDto registerDto);

	UserDto login(LoginDto loginDto);

	boolean userExists(String userName);
}
