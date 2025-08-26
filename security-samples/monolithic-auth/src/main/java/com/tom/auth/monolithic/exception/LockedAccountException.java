package com.tom.auth.monolithic.exception;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class LockedAccountException extends RuntimeException {

	public LockedAccountException(String msg) {
		super(msg);
	}
	
	public LockedAccountException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
