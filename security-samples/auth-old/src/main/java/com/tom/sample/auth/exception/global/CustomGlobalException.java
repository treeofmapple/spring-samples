package com.tom.sample.auth.exception.global;

@SuppressWarnings("serial")
public abstract class CustomGlobalException extends RuntimeException {

	public CustomGlobalException(String msg) {
		super(msg);
	}

	public CustomGlobalException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
