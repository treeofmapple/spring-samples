package com.tom.aws.kafka.common;

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
	
	public void invalidateUserSession() {
		var session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	}
    
}
