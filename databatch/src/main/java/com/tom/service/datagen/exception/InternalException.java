package com.tom.service.datagen.exception;

import org.springframework.http.HttpStatus;

import com.tom.service.datagen.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InternalException extends CustomGlobalException {
	public InternalException(String message) {
		super(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
