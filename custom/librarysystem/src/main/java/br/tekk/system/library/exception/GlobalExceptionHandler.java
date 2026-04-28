package br.tekk.system.library.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handle(UserNotFoundException exp) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMsg());
	}

	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<String> handle(BookNotFoundException exp) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMsg());
	}

	@ExceptionHandler(BookItemNotFoundException.class)
	public ResponseEntity<String> handle(BookItemNotFoundException exp) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exp.getMsg());
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handle(UserAlreadyExistsException exp) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exp.getMsg());
	}

	@ExceptionHandler(BookAlreadyExistsException.class)
	public ResponseEntity<String> handle(BookAlreadyExistsException exp) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exp.getMsg());
	}

	@ExceptionHandler(BookItemAlreadyExistsException.class)
	public ResponseEntity<String> handle(BookItemAlreadyExistsException exp) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exp.getMsg());
	}

	@ExceptionHandler(MaxBookRentException.class)
	public ResponseEntity<String> handle(MaxBookRentException exp) {
		return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exp.getMsg());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exp) {

		var errors = new HashMap<String, String>();

		exp.getBindingResult().getAllErrors().forEach(error -> {
			var fieldName = ((FieldError) error).getField();
			var errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errors));
	}

}