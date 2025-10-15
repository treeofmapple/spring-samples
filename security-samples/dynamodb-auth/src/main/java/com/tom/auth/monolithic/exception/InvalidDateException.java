package com.tom.auth.monolithic.exception;

import java.time.DateTimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InvalidDateException extends DateTimeException {
	
	public InvalidDateException(String msg) {
		super(msg);
	}
	
	public InvalidDateException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
