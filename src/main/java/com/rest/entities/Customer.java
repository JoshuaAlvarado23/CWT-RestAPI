package com.rest.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
	
	private static final String NAMEREGEX = "^[A-Za-z]+$";
	private static final String NAMEMSG = "should only contain letters.";
	private static final String NAMEMAXLENGTHMSG = "lenght should be between 2 and 20";
	private static final String NOTBLANKMSG = "cannot be blank.";
	private static final String EMAILMSG = "must be a valid email (ex. a@mail.com)";
	private static final String EMAILMAXLENGTHMSG = "length should be between 7 and 20";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer custId;
	
	@NotBlank(message = NOTBLANKMSG)
	@Size(min = 2, max = 20, message = NAMEMAXLENGTHMSG)
	@Pattern(regexp = NAMEREGEX, message = NAMEMSG)
	@Column(name = "first_name", length = 20)
	private String firstname;
	
	@NotBlank(message = NOTBLANKMSG)
	@Size(min = 2, max = 20,  message = NAMEMAXLENGTHMSG)
	@Pattern(regexp = NAMEREGEX,  message = NAMEMSG)
	@Column(name = "last_name", length = 20)
	private String lastname;
	
	@NotBlank(message = NOTBLANKMSG)
	@Email(message = EMAILMSG)
	@Size(min = 7, max = 20, message = EMAILMAXLENGTHMSG)
	@Column(name = "email", length = 20, unique = true)
	private String email;
	
	@Column(name = "location")
	private String location;
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Orders> orders;
}
