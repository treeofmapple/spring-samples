package com.tom.sample.auth.exception.global;

@SuppressWarnings("serial")
public abstract class IllegalGlobalException extends IllegalStateException {

	public IllegalGlobalException(String msg) {
		super(msg);
	}

	public IllegalGlobalException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
