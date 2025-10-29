package com.tom.auth.monolithic.exception;

import org.springframework.http.HttpStatus;

import com.tom.auth.monolithic.exception.global.GlobalRuntimeException;

@SuppressWarnings("serial")
public class InternalException extends GlobalRuntimeException {

	public InternalException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public InternalException(String message, Throwable cause) {
		super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
