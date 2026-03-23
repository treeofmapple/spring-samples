package com.tom.samples.ai.exception.sql;

import org.springframework.http.HttpStatus;

import com.tom.samples.ai.exception.global.GlobalRuntimeException;

@SuppressWarnings("serial")
public class DataViolationException extends GlobalRuntimeException {

	public DataViolationException(String message) {
		super(message, HttpStatus.CONFLICT);
	}

	public DataViolationException(String message, Throwable cause) {
		super(message, cause, HttpStatus.CONFLICT);
	}
}
