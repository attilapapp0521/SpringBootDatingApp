package com.datingapp.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.datingapp.domain.UserEntity;
import com.datingapp.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	private final UserService userService;

	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userService.findUserByUsername(username);
		return CustomUserDetails.fromUserToCustomUserDetails(user);
	}
}
