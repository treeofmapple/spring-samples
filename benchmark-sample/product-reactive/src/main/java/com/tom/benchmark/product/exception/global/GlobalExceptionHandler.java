package com.tom.benchmark.product.exception.global;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private record ApiErrorResponse(String message) {
	}

	@ExceptionHandler(GlobalRuntimeException.class)
	public ResponseEntity<ApiErrorResponse> handleCustomExceptions(GlobalRuntimeException ex) {
		var errorResponse = new ApiErrorResponse(ex.getMessage());
		return ResponseEntity.status(ex.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(GlobalDateTimeException.class)
	public ResponseEntity<ApiErrorResponse> handleGlobalDateTime(GlobalDateTimeException ex) {
		return ResponseEntity.status(ex.getStatus()).body(new ApiErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<ApiErrorResponse> handleMultipartException(MultipartException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(
				"Current request is not a multipart request. Please ensure you are sending 'form-data'."));
	}

	@ExceptionHandler(UnsupportedMediaTypeStatusException.class)
	public ResponseEntity<ApiErrorResponse> handleUnsupportedMedia(UnsupportedMediaTypeStatusException ex) {
		String message = "Invalid Media Type Ensure you are using 'form-data' in your request body.";
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ApiErrorResponse(message));
	}

	@ExceptionHandler(ServerWebInputException.class)
	public ResponseEntity<ApiErrorResponse> handleMissingParams(ServerWebInputException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiErrorResponse("Missing required parameter: " + ex.getReason()));
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp) {
		Map<String, String> errors = exp.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
	}

}
