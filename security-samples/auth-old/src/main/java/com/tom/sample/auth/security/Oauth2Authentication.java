package com.tom.sample.auth.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tom.sample.auth.common.Operations;
import com.tom.sample.auth.mapper.AuthenticationMapper;
import com.tom.sample.auth.model.User;
import com.tom.sample.auth.model.enums.Role;
import com.tom.sample.auth.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Oauth2Authentication implements AuthenticationSuccessHandler {

	@Value("${application.security.expiration}")
	private String jwtExpiration;

	@Value("${application.security.refresh-token.expiration}")
	private String refreshExpiration;
	
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final Operations operations;
	private final AuthenticationMapper mapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		String email = oauth2User.getAttribute("email");
		String name = oauth2User.getAttribute("name");
		Integer age = oauth2User.getAttribute("age");

		if (email == null) {
			throw new RuntimeException("Email not found from OAuth2 provider");
		}

		var userOptional = userRepository.findByEmail(email);
		User user;

		if (userOptional.isPresent()) {
			user = userOptional.get();
		} else {
			user = new User();
			user = mapper.buildAttributes(
					name != null ? name : email.split("@")[0], 
					email.split("@")[0], 
					age != null ? age : 0, 
					email, 
					"",
					true,
					null
				);
			user.setRole(Role.USER);
			userRepository.save(user);
		}
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
		operations.addCookie(response, "access_token", jwtToken, operations.parseDuration(jwtExpiration));
		operations.addCookie(response, "refresh_token", refreshToken, operations.parseDuration(refreshExpiration));
		response.sendRedirect("/home");
	}
}
