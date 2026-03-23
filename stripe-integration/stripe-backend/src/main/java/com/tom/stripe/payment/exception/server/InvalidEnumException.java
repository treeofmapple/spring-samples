package com.tom.stripe.payment.exception.server;

import org.springframework.http.HttpStatus;

import com.tom.stripe.payment.exception.global.GlobalRuntimeException;

@SuppressWarnings("serial")
public class InvalidEnumException extends GlobalRuntimeException {

	public InvalidEnumException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}

	public InvalidEnumException(String message, Throwable cause) {
		super(message, cause, HttpStatus.BAD_REQUEST);
	}

}
