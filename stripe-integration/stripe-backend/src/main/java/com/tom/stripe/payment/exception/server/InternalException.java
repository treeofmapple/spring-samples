package com.tom.stripe.payment.exception.server;

import org.springframework.http.HttpStatus;

import com.tom.stripe.payment.exception.global.GlobalRuntimeException;

@SuppressWarnings("serial")
public class InternalException extends GlobalRuntimeException {

	public InternalException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public InternalException(String message, Throwable cause) {
		super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
