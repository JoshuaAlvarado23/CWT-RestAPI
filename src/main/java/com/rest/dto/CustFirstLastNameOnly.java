package com.rest.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustFirstLastNameOnly {

	
	@NotBlank(message = "cannot be blank.")
	@Size(min = 2, max = 20, message = "lenght should be between 2 and 20")
	@Pattern(regexp = "^[A-Za-z]+$", message = "should not contain numbers.")
	@Column(name = "first_name", length = 20)
	private String firstname;
	
	@NotBlank(message = "cannot be blank.")
	@Size(min = 2, max = 20,  message = "lenght should be between 2 and 20")
	@Pattern(regexp = "^[A-Za-z]+$",  message = "should not contain numbers.")
	@Column(name = "last_name", length = 20)
	private String lastname;
	
}
