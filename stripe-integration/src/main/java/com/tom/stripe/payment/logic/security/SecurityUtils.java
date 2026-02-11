package com.tom.stripe.payment.logic.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequestScope
public class SecurityUtils {

    private final HttpServletRequest request;

    public SecurityUtils(HttpServletRequest request) {
        this.request = request;
    }

    public String getRequestingClientIp() {
    	return this.request.getRemoteAddr();	
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
