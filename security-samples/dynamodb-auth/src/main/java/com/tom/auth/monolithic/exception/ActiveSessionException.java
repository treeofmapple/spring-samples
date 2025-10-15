package com.tom.auth.monolithic.exception;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class ActiveSessionException extends RuntimeException {
    
	public ActiveSessionException(String message) {
        super(message);
    }
    
	public ActiveSessionException(String message, Throwable cause) {
        super(message, cause);
    }	
}
