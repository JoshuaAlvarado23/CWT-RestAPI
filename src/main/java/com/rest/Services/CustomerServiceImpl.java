package com.rest.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.Entities.CustFirstLastNameOnly;
import com.rest.Entities.Customer;
import com.rest.Persistence.CustomerRepo;


@Service
public class CustomerServiceImpl implements ICustomerService {
	
	@Autowired
	private CustomerRepo customerRepo;

	@Override
	public Customer findCustomerByID(Integer id) {
		Optional<Customer> temp = customerRepo.findById(id); 
		if(temp.isPresent()) {
			return temp.get();
		}
		return null;
 
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}

	@Override
	public Customer createCustomer(Customer customer) {
		return customerRepo.save(customer);
		
	}

	@Override
	public void deleteCustomer(Customer customer) {
		customerRepo.delete(customer);
	}

	@Override
	public Customer updateCustomer(Customer oldCustomer, Customer newCustomer) {
		if(oldCustomer == null) {
			return null;
		} else {
			newCustomer.setCustId(oldCustomer.getCustId());
			newCustomer.setOrders(oldCustomer.getOrders());
			customerRepo.save(newCustomer);
			return newCustomer;
		}
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		Optional<Customer> cust = customerRepo.findByEmail(email);
		if(cust.isEmpty()) {
			return null;
		}
		return cust.get();
	}

	@Override
	public Customer custPartialUpdate(Customer cust, CustFirstLastNameOnly custpartial) {
		if(cust == null) {
			return null;
		} else {
			cust.setFirstname(custpartial.getFirstname());
			cust.setLastname(custpartial.getLastname());
			customerRepo.save(cust);
			return cust;
		}
	}	
	
}
