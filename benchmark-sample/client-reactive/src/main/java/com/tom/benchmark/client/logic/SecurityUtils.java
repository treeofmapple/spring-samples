package com.tom.benchmark.client.logic;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final ServerHttpRequest request;

    public String getRequestingClientIp() {
    	return this.request.getRemoteAddress().getHostString();
    }

    /*

    public User getAuthenticatedUserOrThrow() {
        return getAuthenticatedUser()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User is not authenticated"));
    }

	public void invalidateUserSession() {
		var session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	    SecurityContextHolder.clearContext();
	}

    private Optional<User> getAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast);
    }

     */

}
