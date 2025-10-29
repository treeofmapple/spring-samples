package com.tom.auth.monolithic.exception;

import org.springframework.http.HttpStatus;

import com.tom.auth.monolithic.exception.global.GlobalRuntimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InvalidTokenException extends GlobalRuntimeException {

	public InvalidTokenException(String message) {
		super(message, HttpStatus.FORBIDDEN);
	}

	public InvalidTokenException(String message, Throwable cause) {
		super(message, cause, HttpStatus.FORBIDDEN);
	}

}
