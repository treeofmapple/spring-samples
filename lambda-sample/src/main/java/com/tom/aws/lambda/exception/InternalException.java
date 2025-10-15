package com.tom.aws.lambda.exception;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InternalException extends RuntimeException {
	
	public InternalException(String msg) {
		super(msg);
	}

	public InternalException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
