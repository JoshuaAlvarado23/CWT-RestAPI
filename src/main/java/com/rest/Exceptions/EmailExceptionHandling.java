package com.rest.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailExceptionHandling extends DataIntegrityViolationException {	

	public EmailExceptionHandling(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 2131231L;

	
}
