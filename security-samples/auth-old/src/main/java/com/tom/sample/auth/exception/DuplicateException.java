package com.tom.sample.auth.exception;

import com.tom.sample.auth.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class DuplicateException extends CustomGlobalException {
	
	public DuplicateException(String msg) {
		super(msg);
	}
	
	public DuplicateException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
