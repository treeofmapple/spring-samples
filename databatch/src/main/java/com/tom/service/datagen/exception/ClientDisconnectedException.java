package com.tom.service.datagen.exception;

import com.tom.service.datagen.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class ClientDisconnectedException extends CustomGlobalException {

	public ClientDisconnectedException(String msg) {
		super(msg);
	}

	public ClientDisconnectedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
