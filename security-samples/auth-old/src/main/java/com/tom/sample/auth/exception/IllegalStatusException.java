package com.tom.sample.auth.exception;

import com.tom.sample.auth.exception.global.IllegalGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class IllegalStatusException extends IllegalGlobalException {

	public IllegalStatusException(String msg) {
		super(msg);
	}

	public IllegalStatusException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
