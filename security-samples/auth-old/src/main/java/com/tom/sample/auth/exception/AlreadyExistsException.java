package com.tom.sample.auth.exception;

import com.tom.sample.auth.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class AlreadyExistsException extends CustomGlobalException {
	
	public AlreadyExistsException(String msg) {
		super(msg);
	}
	
	public AlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
