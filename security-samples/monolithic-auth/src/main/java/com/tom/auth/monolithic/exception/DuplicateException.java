package com.tom.auth.monolithic.exception;

import org.springframework.http.HttpStatus;

import com.tom.auth.monolithic.exception.global.GlobalRuntimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class DuplicateException extends GlobalRuntimeException {
	public DuplicateException(String message) {
		super(message, HttpStatus.CONFLICT);
	}

	public DuplicateException(String message, Throwable cause) {
		super(message, cause, HttpStatus.CONFLICT);
	}
}
