package com.tom.auth.monolithic.exception.global;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY) 
public record ErrorResponse(
		
		Instant timestamp,
		int status,
		String error,
		String message,
		String path,
		Map<String, String> errors
) {
	
    public ErrorResponse(int status, String error, String message, String path) {
        this(
            Instant.now(),
            status,
            error,
            message,
            path,
            null
        );
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> validationErrors) {
        this(
            Instant.now(),
            status,
            error,
            message,
            path,
            validationErrors 
        );
    }
	
}
