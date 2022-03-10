package com.rest.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ValidatorService {
	
	
	public Map<String,String> validate (BindingResult bindingResult) {
		
		Map<String, String> output = new HashMap<>();
		bindingResult.getFieldErrors().stream().forEach(err->{
			output.put(err.getField(), err.getDefaultMessage());
		});
		
		return output;
	}

}
