package com.rest.services;

import java.util.List;

import com.rest.entities.CustFirstLastNameOnly;
import com.rest.entities.Customer;

public interface ICustomerService {
	
	public Customer findCustomerByID(Integer id);
	
	public List<Customer> getAllCustomers();
	
	public Customer createCustomer(Customer customer);
	
	public void deleteCustomer(Customer customer);
	
	public Customer updateCustomer(Customer oldCustomer, Customer newCustomer);
	
	public Customer getCustomerByEmail(String email);
	
	public Customer custPartialUpdate(Customer customer, CustFirstLastNameOnly custpartial);
}
