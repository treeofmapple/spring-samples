package com.tom.stripe.payment.exception.global;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private record ApiErrorResponse(String message) {}

    @ExceptionHandler(GlobalRuntimeException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomExceptions(GlobalRuntimeException ex) {
        var errorResponse = new ApiErrorResponse(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

	@ExceptionHandler(GlobalDateTimeException.class)
	public ResponseEntity<ApiErrorResponse> handleGlobalDateTime(GlobalDateTimeException ex) {
		return ResponseEntity.status(ex.getStatus()).body(new ApiErrorResponse(ex.getMessage()));
	}
    
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp) {
		Map<String, String> errors = exp.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
	}

}
