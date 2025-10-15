package com.tom.auth.monolithic.exception;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InvalidTokenException extends RuntimeException {

	public InvalidTokenException(String msg) {
		super(msg);
	}

	public InvalidTokenException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
