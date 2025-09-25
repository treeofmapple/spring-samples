package com.tom.service.datagen.exception;

import org.springframework.http.HttpStatus;

import com.tom.service.datagen.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class DuplicateException extends CustomGlobalException {
	public DuplicateException(String message) {
		super(message, HttpStatus.CONFLICT);
	}
}
