package com.tom.reactive.first.exception.sql;

import org.springframework.http.HttpStatus;

import com.tom.reactive.first.exception.global.GlobalRuntimeException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends GlobalRuntimeException {
	public NotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
}
