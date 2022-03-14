package com.rest.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.entities.CustFirstLastNameOnly;
import com.rest.entities.Customer;
import com.rest.exceptions.EmailExceptionHandling;
import com.rest.exceptions.NotFoundExceptionHandler;
import com.rest.services.ICustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
	
	@Autowired
	private ICustomerService customerService;
	
	
	@GetMapping("/id/{id}")
	public Customer getByID(@Valid @PathVariable Integer id) {
		Customer cust = customerService.findCustomerByID(id);
		if(cust == null) {
			throw new NotFoundExceptionHandler(id);
		}
		return cust;
		
	}
	
	@GetMapping("/all")
	public List<Customer> getAllCust() {
		return customerService.getAllCustomers();
	}
	
	@PostMapping("/secured/create")
	public ResponseEntity<Customer> createCust (@Valid @RequestBody Customer customerDTO,
												@RequestHeader Map<String, String> headers) {
			
			headers.entrySet().stream().forEach(e-> log.info(e.getValue()));
			
			if(customerService.getCustomerByEmail(customerDTO.getEmail()) != null) {
				throw new EmailExceptionHandling("Email already exist!");
			} else {
				return new ResponseEntity<>(customerService.createCustomer(customerDTO), 
													HttpStatus.CREATED);
			}
	}
	
	@DeleteMapping("/secured/delete/{id}") 
	public ResponseEntity<Customer> deleteCust (@Valid @PathVariable Integer id) {
		
		Customer cust = customerService.findCustomerByID(id);
		if(cust == null) {
			throw new NotFoundExceptionHandler(id,"Deletion Failed.");
		}
		
		customerService.deleteCustomer(cust);
		return ResponseEntity.ok(cust);
	}
	
	
	@PutMapping("/secured/update/{id}") 
	public ResponseEntity<Customer> updateCust (@Valid @PathVariable Integer id, 
												@Valid @RequestBody Customer customerDTO) { 
			Customer getCustomer = customerService.findCustomerByID(id); 
				if(getCustomer == null) {
					throw new NotFoundExceptionHandler(id);
				}
				
				return new ResponseEntity<>(customerService.updateCustomer(getCustomer, customerDTO), 
													HttpStatus.ACCEPTED);
	}
	  
	@PatchMapping("/secured/partialupdatefirstandlastname/{id}") 
	public ResponseEntity<Customer> partialUpdateCust (@Valid @PathVariable Integer id, 
												@Valid @RequestBody CustFirstLastNameOnly custpart) { 
		
			Customer getCustomer = customerService.findCustomerByID(id); 
			
			  if(getCustomer == null) {
				  throw new NotFoundExceptionHandler(id);
			  }
			  return new ResponseEntity<>(	customerService.custPartialUpdate(getCustomer, custpart), 
					  								HttpStatus.ACCEPTED);
	}
	 
	
}
