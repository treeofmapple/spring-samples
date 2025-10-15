package com.tom.auth.monolithic.exception;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class AlreadyExistsException extends DataIntegrityViolationException {
	
	public AlreadyExistsException(String msg) {
		super(msg);
	}
	
	public AlreadyExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
