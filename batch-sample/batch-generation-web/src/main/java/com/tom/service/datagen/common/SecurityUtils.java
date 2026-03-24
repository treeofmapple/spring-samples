package com.tom.service.datagen.common;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class SecurityUtils {

	private final HttpServletRequest request;

	public SecurityUtils(HttpServletRequest request) {
		this.request = request;
	}

	public String getRequestingClientIp() {
		return this.request.getRemoteAddr();
	}

	public HttpServletRequest getRequest() {
		return this.request;
	}

}
