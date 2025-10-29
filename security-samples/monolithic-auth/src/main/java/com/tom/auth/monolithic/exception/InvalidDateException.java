package com.tom.auth.monolithic.exception;

import org.springframework.http.HttpStatus;

import com.tom.auth.monolithic.exception.global.GlobalDateTimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InvalidDateException extends GlobalDateTimeException {

	public InvalidDateException(String message) {
		super(message, HttpStatus.EXPECTATION_FAILED);
	}

	public InvalidDateException(String message, Throwable cause) {
		super(message, cause, HttpStatus.EXPECTATION_FAILED);
	}
}
