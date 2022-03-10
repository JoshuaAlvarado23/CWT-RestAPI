package com.rest.Controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.Entities.CustFirstLastNameOnly;
import com.rest.Entities.Customer;
import com.rest.Exceptions.EmailExceptionHandling;
import com.rest.Exceptions.NotFoundExceptionHandler;
import com.rest.Services.ICustomerService;
import com.rest.Services.ValidatorService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private ValidatorService validatorService;
	
	@GetMapping("/id/{id}")
	public Customer getByID(@Valid @PathVariable Integer id) {
		Customer cust = customerService.findCustomerByID(id);
		if(cust == null) {
			throw new NotFoundExceptionHandler("Customer with ID " + id + " does not exist!");
		}
		return cust;
		
	}
	
	@GetMapping("/all")
	public List<Customer> getAllCust() {
		return customerService.getAllCustomers();
	}
	
	@PostMapping("/create")
	public ResponseEntity<Customer> createCust (@Valid @RequestBody Customer customerDTO) {
		
			if(customerService.getCustomerByEmail(customerDTO.getEmail()) != null) {
				throw new EmailExceptionHandling("Email already exist!");
			} else {
				return new ResponseEntity<>(customerService.createCustomer(customerDTO), 
													HttpStatus.CREATED);
			}
	}
	
	@DeleteMapping("/delete/{id}") 
	public ResponseEntity<Customer> deleteCust (@Valid @PathVariable Integer id) {
		
		Customer cust = customerService.findCustomerByID(id);
		if(cust == null) {
			throw new NotFoundExceptionHandler(	"Customer with ID " + id +
												" does not exist! Deletion Failed.");
		}
		
		customerService.deleteCustomer(cust);
		return ResponseEntity.ok(cust);
	}
	
	
	@PutMapping("/update/{id}") 
	public ResponseEntity<Customer> updateCust (@Valid @PathVariable Integer id, 
												@Valid @RequestBody Customer customerDTO) { 
			Customer getCustomer = customerService.findCustomerByID(id); 
				if(getCustomer == null) {
					throw new NotFoundExceptionHandler(	"Customer with ID " + id + 
														" does not exist!");
				}
				
				return new ResponseEntity<>(customerService.updateCustomer(getCustomer, customerDTO), 
													HttpStatus.ACCEPTED);
	}
	  
	@PatchMapping("/partialupdatefirstandlastname/{id}") 
	public ResponseEntity<Customer> partialUpdateCust (@Valid @PathVariable Integer id, 
												@Valid @RequestBody CustFirstLastNameOnly custpart) { 
		
			Customer getCustomer = customerService.findCustomerByID(id); 
			
			  if(getCustomer == null) {
				  throw new NotFoundExceptionHandler("Customer with ID " + id + " does not exist!");
			  }
			  return new ResponseEntity<>(	customerService.custPartialUpdate(getCustomer, custpart), 
					  								HttpStatus.ACCEPTED);
	}
	 
	
}
