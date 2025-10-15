package com.tom.aws.lambda.exception.global;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tom.aws.lambda.exception.ActiveSessionException;
import com.tom.aws.lambda.exception.AlreadyExistsException;
import com.tom.aws.lambda.exception.DuplicateException;
import com.tom.aws.lambda.exception.InternalException;
import com.tom.aws.lambda.exception.InvalidDateException;
import com.tom.aws.lambda.exception.LockedAccountException;
import com.tom.aws.lambda.exception.NotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ActiveSessionException.class)
	public ResponseEntity<ErrorResponse> handleActiveSessionException(ActiveSessionException ex, HttpServletRequest request) {
	    ErrorResponse errorDetails = new ErrorResponse(
	            HttpStatus.CONFLICT.value(),
	            "Session Conflict",
	            ex.getMessage(),
	            request.getRequestURI()
	    );
	    return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
	}
	
    @ExceptionHandler(LockedAccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountLocked(LockedAccountException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
        		HttpStatus.UNAUTHORIZED.value(),
        		"Account Locked", 
        		exp.getMessage(),
        		request.getRequestURI()
		);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
	
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpired(BadCredentialsException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
        		HttpStatus.UNAUTHORIZED.value(),
        		"Invalid Credentials", 
        		exp.getMessage(),
        		request.getRequestURI()
		);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ErrorResponse> handleResourceNotFound(NotFoundException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            exp.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler({ AlreadyExistsException.class, DuplicateException.class })
    public ResponseEntity<ErrorResponse> handleDataIntegrityConflict(RuntimeException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            exp.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler({ InvalidDateException.class })
    public ResponseEntity<ErrorResponse> handleInvalidInputDate(NumberFormatException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Input",
            exp.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
	
    @ExceptionHandler({ IllegalStateException.class })
    public ResponseEntity<ErrorResponse> handleInvalidStateTransition(IllegalStateException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            exp.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InternalException.class })
    public ResponseEntity<ErrorResponse> handleUnexpectedServerError(InternalException exp, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            exp.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exp, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        
        exp.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Validation failed for one or more fields",
            request.getRequestURI(),
            validationErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}