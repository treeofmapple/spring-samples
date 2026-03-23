package com.tom.stripe.payment.exception.payment;

import org.springframework.http.HttpStatus;

import com.tom.stripe.payment.exception.global.GlobalRuntimeException;

@SuppressWarnings("serial")
public class PaymentException extends GlobalRuntimeException {

	public PaymentException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}

	public PaymentException(String message, Throwable cause) {
		super(message, cause, HttpStatus.BAD_REQUEST);
	}
	
}
