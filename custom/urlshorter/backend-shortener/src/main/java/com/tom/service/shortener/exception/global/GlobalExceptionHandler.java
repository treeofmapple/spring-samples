package com.tom.service.shortener.exception.global;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private record ApiErrorResponse(String message) {}

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomExceptions(CustomGlobalException ex) {
        var errorResponse = new ApiErrorResponse(ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

	/*
	 * Ele vai dar um override no controle de excessão do spring boot. Quando
	 * ocorrer uma excessão em vez de retornar uma linha de argumento grande ele vai
	 * retornar um código pequeno.
	 */
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        var errorResponse = new ApiErrorResponse("Data integrity error");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp) {

		Map<String, String> errors = exp.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
	}

}
