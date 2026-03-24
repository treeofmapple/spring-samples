package com.tom.reactive.first.exception.sql;

import org.springframework.http.HttpStatus;

import com.tom.reactive.first.exception.global.GlobalRuntimeException;

@SuppressWarnings("serial")
public class DataViolationException extends GlobalRuntimeException {

	public DataViolationException(String message) {
		super(message, HttpStatus.CONFLICT);
	}

	public DataViolationException(String message, Throwable cause) {
		super(message, cause, HttpStatus.CONFLICT);
	}
}
