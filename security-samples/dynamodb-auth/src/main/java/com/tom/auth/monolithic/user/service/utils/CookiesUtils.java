package com.tom.auth.monolithic.user.service.utils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.tom.auth.monolithic.exception.ActiveSessionException;
import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.repository.TokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookiesUtils {

	private final TokenRepository repository;
	
	public void checkIfUserIsAlreadyAuthenticated(String userInfo, HttpServletRequest request, String refreshTokenName) {
	    getCookieValue(request, refreshTokenName).ifPresent(token -> {
	        repository.findByToken(token)
	            .filter(t -> !t.isRevoked() && !t.isExpired())
	            .ifPresent(activeToken -> {
	                String identifierAttemptingLogin = userInfo;
	                User existingUser = activeToken.getUser();
	                
	                if (!existingUser.getUsername().equals(identifierAttemptingLogin) && 
	                    !existingUser.getEmail().equals(identifierAttemptingLogin)) {
	                    
	                    throw new ActiveSessionException(
	                        "An account is already logged in on this browser. Please log out first."
	                    );
	                }
	            });
	    });
	}
	
	public void addCookie(HttpServletResponse response, String name, String value, Duration duration) {
		ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)    // Prevents access from JavaScript (XSS protection)
                .secure(true)      // Only sent over HTTPS
                .path("/")         // Available to the entire site
                .maxAge(duration) 
                .sameSite("Lax")   // OR Strict
				.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
	public Optional<String> getCookieValue(HttpServletRequest request, String name) {
	    if (request.getCookies() == null) {
	        return Optional.empty();
	    }
		
	    return Arrays.stream(request.getCookies())
	            .filter(cookie -> name.equals(cookie.getName()))
	            .map(Cookie::getValue)
	            .findFirst();
	}
	
	public void clearCookie(HttpServletResponse response, String name) {
	    ResponseCookie cookie = ResponseCookie.from(name, "")
	    		.httpOnly(true)
	    		.path("/")
	            .maxAge(0)
	            .build();
	    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}
	
}
