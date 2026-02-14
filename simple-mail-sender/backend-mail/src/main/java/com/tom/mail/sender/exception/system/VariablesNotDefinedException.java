package com.tom.mail.sender.exception.system;

import org.springframework.http.HttpStatus;

import com.tom.mail.sender.exception.global.GlobalRuntimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class VariablesNotDefinedException extends GlobalRuntimeException {
	public VariablesNotDefinedException(String message) {
		super(message, HttpStatus.FAILED_DEPENDENCY);
	}
}
