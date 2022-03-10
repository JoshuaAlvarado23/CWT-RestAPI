package com.rest.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundExceptionHandler extends RuntimeException{

	private static final long serialVersionUID = 12345678L;

	public NotFoundExceptionHandler(String message) {
		super(message);
	}
	
}
