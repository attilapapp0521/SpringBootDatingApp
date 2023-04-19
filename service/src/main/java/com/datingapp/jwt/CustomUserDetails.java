package com.datingapp.jwt;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import com.datingapp.domain.UserEntity;

public class CustomUserDetails implements UserDetails {
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> grantedAuthorities;

	public static CustomUserDetails fromUserToCustomUserDetails(UserEntity user) {
		if (user != null) {
			CustomUserDetails customUserDetails = new CustomUserDetails();
			customUserDetails.username = user.getUsername();
			customUserDetails.password = user.getPassword();
			customUserDetails.grantedAuthorities = AuthorityUtils
					.createAuthorityList(user.getRoles().stream().map(Enum::toString)
							.toArray(String[]::new));
			return customUserDetails;
		}

		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
