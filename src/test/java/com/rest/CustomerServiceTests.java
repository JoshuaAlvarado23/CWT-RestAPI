package com.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.rest.entities.Customer;
import com.rest.persistence.CustomerRepo;
import com.rest.services.CustomerServiceImpl;


@SpringBootTest
class CustomerServiceTests {

	List<Customer> customerList = new ArrayList<>();
	
	@Mock
	private CustomerRepo custRepository;
	
	@InjectMocks
	private CustomerServiceImpl custService;
	
	
	@Nested
	@DisplayName("Accepted Scenarios")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class accepted {
		
		@BeforeEach
		void init() {	
			customerList.add(new Customer(1, "Joshua", "Alvarado", "joshua@email.com", "Manila", null));
			customerList.add(new Customer(2, "Joel", "Cerbo", "joel@email.com", "Cebu", null));
			customerList.add(new Customer(3, "Mae", "Berondo", "mae@email.com", "Caloocan", null));
			
		}
		@AfterEach
		void destroy() {
			customerList.clear();
		}

		@Test
		@Order(3)
		@DisplayName("Get Customer List Size Test")
		void getNotNullList() {
			when(custRepository.findAll()).thenReturn(customerList);
			assertEquals(3, custService.getAllCustomers().size());

		}
		
		@Test
		@Order(1)
		@DisplayName("Get Customer List Size (Empty) Test")
		void getEmptyList() {
			customerList = new ArrayList<>();
			when(custRepository.findAll()).thenReturn(customerList);
			assertEquals(0, custService.getAllCustomers().size());
			
		}
		
		@Test
		@Order(4)
		@DisplayName("Get Customer By ID Test")
		void getCustomerById() {
			Customer customer = customerList.get(1);
			
			when(custRepository.findById(2)).thenReturn(Optional.of(customer));
		
			assertEquals("Joel", custService.findCustomerByID(2).getFirstname());
			assertEquals("Cerbo", custService.findCustomerByID(2).getLastname());
			assertEquals("joel@email.com", custService.findCustomerByID(2).getEmail());
			assertEquals("Cebu", custService.findCustomerByID(2).getLocation());
		}
		
		@Test
		@Order(5)
		@DisplayName("Update Customer Test")
		void updateCustomer() {
			Customer MockCustomer = customerList.get(2);
			Customer updateCustomer = new Customer(3, "UpdatedName", "NewName", "UpdateCheck@email.com", "Tagaytay", null);
			
			when(custRepository.findById(3)).thenReturn(Optional.of(MockCustomer));
			
			Customer output = custService.updateCustomer(MockCustomer, updateCustomer);
			
			assertEquals(3, output.getCustId());
			assertEquals("UpdatedName", output.getFirstname());
			assertEquals("NewName", output.getLastname());
			assertEquals("UpdateCheck@email.com", output.getEmail());
			assertEquals("Tagaytay", output.getLocation());	
		}
		
		@Test
		@Order(2)
		@DisplayName("Add Customer Functionality Test")
		void addCustomer() {
			
			Customer newCustomer = new Customer(4, "newFName", "newLName", "new@email.com", "newLocation", null);
			
			when(custRepository.save(newCustomer)).thenReturn(newCustomer);
			
			assertEquals(newCustomer, custService.createCustomer(newCustomer));	
		}
		
		@Test
		@Order(6)
		@DisplayName("Delete Customer Functionality Test")
		void deleteCustomer() {
			
			Customer delCustomer = new Customer(4, "newFName", "newLName", "new@email.com", "newLocation", null);
			
			doNothing().when(custRepository).delete(delCustomer);
			
			custService.deleteCustomer(delCustomer);
			
			verify(custRepository, times(1)).delete(delCustomer);
		}
	}
}
