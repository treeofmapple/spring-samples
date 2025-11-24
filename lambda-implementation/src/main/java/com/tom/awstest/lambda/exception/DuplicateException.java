package com.tom.awstest.lambda.exception;

import org.springframework.http.HttpStatus;

import com.tom.awstest.lambda.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class DuplicateException extends CustomGlobalException {
	public DuplicateException(String message) {
		super(message, HttpStatus.CONFLICT);
	}
}
