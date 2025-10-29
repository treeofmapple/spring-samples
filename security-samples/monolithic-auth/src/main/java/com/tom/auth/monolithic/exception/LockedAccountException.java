package com.tom.auth.monolithic.exception;

import org.springframework.http.HttpStatus;

import com.tom.auth.monolithic.exception.global.GlobalRuntimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class LockedAccountException extends GlobalRuntimeException {

	public LockedAccountException(String message) {
		super(message, HttpStatus.LOCKED);
	}
	
	public LockedAccountException(String message, Throwable cause) {
		super(message, cause, HttpStatus.LOCKED);
	}
	
}
