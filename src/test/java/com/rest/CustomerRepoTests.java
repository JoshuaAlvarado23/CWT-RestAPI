package com.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.rest.entities.Customer;
import com.rest.persistence.CustomerRepo;

@SpringBootTest
class CustomerRepoTests {
	
	@Mock
	private CustomerRepo repository;
	
	List<Customer> mockCustomerList;
	
	@BeforeEach
	void init() { 
		mockCustomerList = new ArrayList<>();
		mockCustomerList.add(new Customer(1, "Joshua", "Alvarado", "joshua@email.com", "Manila", null));
		mockCustomerList.add(new Customer(2, "Joel", "Cerbo", "joel@email.com", "Cebu", null));
		mockCustomerList.add(new Customer(3, "Mae", "Berondo", "mae@email.com", "Caloocan", null));
	}
	
	@AfterEach
	void destroy() {
		mockCustomerList.clear();
		mockCustomerList = null;
	}
	
	
	@Test
	@DisplayName("Get CustomerList Count from Repository")
	void getCustomerListCount() {
		when(repository.findAll()).thenReturn(mockCustomerList);
		
		assertEquals(3, repository.findAll().size());
	}
	
	@Test
	@DisplayName("Get CustomerList from Repository")
	void getCustomerList() {

		when(repository.findAll()).thenReturn(mockCustomerList);
		assertEquals(mockCustomerList, repository.findAll());
	}
	
	@Test
	@DisplayName("Get Customer by ID from Repository")
	void getCustomerById() {
		Customer mockCustomer = mockCustomerList.get(1);
		
		when(repository.findById(2)).thenReturn(Optional.of(mockCustomer));
		
		assertEquals(2, repository.findById(2).get().getCustId());
		assertEquals("Joel", repository.findById(2).get().getFirstname());
		assertEquals("Cerbo", repository.findById(2).get().getLastname());
		assertEquals("joel@email.com", repository.findById(2).get().getEmail());
		assertEquals("Cebu", repository.findById(2).get().getLocation());
	}
	
	@Test
	@DisplayName("Get Customer by Email from Repository")
	void getCustomerByEmail() {
		Customer mockCustomer = mockCustomerList.get(0);	
		
		when(repository.findByEmail("joshua@email.com")).thenReturn(Optional.of(mockCustomer));
		
		assertEquals("Joshua", repository.findByEmail("joshua@email.com").get().getFirstname());
	}

}
