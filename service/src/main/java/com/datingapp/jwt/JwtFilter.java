package com.datingapp.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtFilter extends GenericFilterBean {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Autowired
	public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService) {
		this.jwtProvider = jwtProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		log.debug("JwtFilter is filtering");
		String token = getTokenFromRequest((HttpServletRequest) servletRequest);
		if (token != null && jwtProvider.validateToken(token)) {
			String username = jwtProvider.getUsernameFromToken(token);
			CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
			if (customUserDetails != null) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails, null,
						customUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (hasText(bearer) && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}
}
