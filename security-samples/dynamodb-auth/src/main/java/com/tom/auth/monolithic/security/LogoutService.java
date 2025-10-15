package com.tom.auth.monolithic.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.tom.auth.monolithic.user.model.User;
import com.tom.auth.monolithic.user.service.utils.CookiesUtils;
import com.tom.auth.monolithic.user.service.utils.TokenUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	@Value("${application.security.cookie-name}")
	private String refreshTokenCookieName;

	private final TokenUtils tokenUtils;
	private final CookiesUtils cookiesUtils;
	private final SecurityUtils requestUtils;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
			return;
		}

		User user = (User) authentication.getPrincipal();
		cookiesUtils.clearCookie(response, refreshTokenCookieName);
		tokenUtils.revokeUserAllTokens(user);
		requestUtils.invalidateUserSession();
	}

}
