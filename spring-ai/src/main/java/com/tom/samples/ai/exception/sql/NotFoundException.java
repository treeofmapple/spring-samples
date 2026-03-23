package com.tom.samples.ai.exception.sql;

import org.springframework.http.HttpStatus;

import com.tom.samples.ai.exception.global.GlobalRuntimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends GlobalRuntimeException {
	public NotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
