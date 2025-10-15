package com.tom.auth.monolithic.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.tom.auth.monolithic.user.model.User;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequestScope
public class SecurityUtils {

    private final HttpServletRequest request;

    public SecurityUtils(HttpServletRequest request) {
        this.request = request;
    }

    public User getAuthenticatedUserOrThrow() {
        return getAuthenticatedUser()
                .orElseThrow(() -> new UsernameNotFoundException("No authenticated user found in the security context."));
    }
    
	public String getRequestingClientIp() {
		return this.request.getRemoteAddr();	
	}
	
	public void invalidateUserSession() {
		var session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	}
    
    private Optional<User> getAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(User.class::isInstance)
                .map(User.class::cast);
    }
    
}
