package com.datingapp.jwt;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import com.datingapp.domain.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Value("$(jwt.secret)")
	private String secretKey;

	public String generateToken(UserEntity user) {
		Map<String, Object> claims = getClaimsFromUser(CustomUserDetails.fromUserToCustomUserDetails(user));
		Date date = Date.from(LocalDate.now().
				plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant());
		return Jwts.builder()
				.setSubject(user.getUsername())
				.addClaims(claims)
				.setIssuedAt(new Date())
				.setExpiration(date)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return false;
	}

	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	private Map<String, Object> getClaimsFromUser(CustomUserDetails user) {
		List<String> authorities = new ArrayList<>();
		for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
			authorities.add(grantedAuthority.getAuthority().substring(5));
		}
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", authorities);
		return claims;
	}
}
