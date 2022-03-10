package com.rest.Exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> globalExceptionHandler (Exception ex, WebRequest web) {
		ErrorMessage err = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value()
				,LocalDateTime.now() 
				,ex.getMessage()
				,web.getDescription(false));
		return new ResponseEntity<>("Status : "+ 
				err.getStatusCode()+" -> "+err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);	
	}
	
	@ExceptionHandler(value = NotFoundExceptionHandler.class)
	public ResponseEntity<String> notFoundExceptionHandler (NotFoundExceptionHandler ex, WebRequest web) {
		ErrorMessage err = new ErrorMessage(HttpStatus.NOT_FOUND.value()
				,LocalDateTime.now()
				,ex.getMessage()
				,web.getDescription(false));
		return new ResponseEntity<>("Status : "+ 
				err.getStatusCode()+" -> "+err.getMessage(), HttpStatus.NOT_FOUND);
	}
	
//new comment for git merge
	
	@ExceptionHandler(value = EmailExceptionHandling.class)
	public ResponseEntity<String> emailExceptionHandler (EmailExceptionHandling ex, WebRequest web) {
		ErrorMessage err = new ErrorMessage(HttpStatus.BAD_REQUEST.value()
				,LocalDateTime.now()
				,ex.getMessage()
				,web.getDescription(false));
		return new ResponseEntity<>("Status : "+ 
				err.getStatusCode()+" -> "+err.getMessage() , HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> validationErrorHandler (BindingResult e) {
		Map<String, String> validationErrors = new HashMap<>();
		e.getFieldErrors().stream().forEach(err -> {
			validationErrors.put(err.getField(), err.getDefaultMessage());
		});
		return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
	}

}
