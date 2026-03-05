package com.tom.benchmark.order.exception.server;

import org.springframework.http.HttpStatus;

import com.tom.benchmark.order.exception.global.GlobalDateTimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class ServiceUnavailableException extends GlobalDateTimeException {

	public ServiceUnavailableException(String message) {
		super(message, HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ServiceUnavailableException(String message, Throwable cause) {
		super(message, cause, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
