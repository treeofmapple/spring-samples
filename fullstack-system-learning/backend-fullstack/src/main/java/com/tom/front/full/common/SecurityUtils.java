package com.tom.front.full.common;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
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

	public void logAction(String action, Object target) {
		log.info("IP: {}, is {}: {}", getRequestingClientIp(), action, target);
	}

}
