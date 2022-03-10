package com.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.Controllers.CustomerController;
import com.rest.Entities.CustFirstLastNameOnly;
import com.rest.Entities.Customer;
import com.rest.Exceptions.NotFoundExceptionHandler;
import com.rest.Services.CustomerServiceImpl;


@WebMvcTest(controllers = CustomerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private CustomerServiceImpl customerService;
	
	
	List<Customer> customerList;
	
	
	@Nested
	@DisplayName("Accepted Controller Scenarios")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class AcceptedScenarios {
		@BeforeEach
		void init() {	
			customerList = new ArrayList<>();
			customerList.add(new Customer(1, "Joshua", "Alvarado", "joshua@email.com", "Manila", null));
			customerList.add(new Customer(2, "Joel", "Cerbo", "joel@email.com", "Cebu", null));
			customerList.add(new Customer(3, "Mae", "Berondo", "mae@email.com", "Caloocan", null));
		}
		
		@AfterEach
		void destroy() {
			customerList.clear();
		}
		
		@Test
		@Order(1)
		@DisplayName("Get Customer By ID - Controller Test")
		void getCustomerById() throws Exception {
			Customer customer = customerList.get(0);
		
			
			when(customerService.findCustomerByID(1)).thenReturn(customer);
			
			MvcResult result =  mockMvc.perform(get("/customer/id/{id}",1)
									.contentType("application/json")
									.content(mapper.writeValueAsString(customer)))
									.andExpect(status().isOk())
									.andReturn();	
			String response = result.getResponse().getContentAsString();
			
			assertEquals(customer, mapper.readValue(response, Customer.class));
			assertEquals(1, mapper.readValue(response, Customer.class).getCustId());
			assertEquals("Joshua", mapper.readValue(response, Customer.class).getFirstname());
			assertEquals("Alvarado", mapper.readValue(response, Customer.class).getLastname());
			assertEquals("joshua@email.com", mapper.readValue(response, Customer.class).getEmail());
			assertEquals("Manila", mapper.readValue(response, Customer.class).getLocation());
			
		}
		
		@Test
		@Order(2)
		@DisplayName("Get All Customers - Controller Test")
		void getAllCustomers() throws Exception {
			
			when(customerService.getAllCustomers()).thenReturn(customerList);
			
			MvcResult result =  mockMvc.perform(get("/customer/all")
									.contentType("application/json")
									.content(mapper.writeValueAsString(customerList)))
									.andExpect(status().isOk())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			
			TypeReference<List<Customer>> ListCustomer = new TypeReference<List<Customer>>() {};
			assertEquals(customerList, mapper.readValue(response, ListCustomer));
			assertEquals(3, mapper.readValue(response, ListCustomer).size());
			
		}
		
		@Test
		@Order(3)
		@DisplayName("Create Customer - Controller Test")
		void createCustomer() throws Exception {
			Customer mockCustomer = customerList.get(2);
			
			when(customerService.createCustomer(any(Customer.class))).thenReturn(mockCustomer);
			
			MvcResult result =  mockMvc.perform(post("/customer/create")
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isCreated())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			
			Customer actualCust = mapper.readValue(response, Customer.class);
			
			assertEquals(mockCustomer, actualCust);
			assertEquals(3, actualCust.getCustId());
			assertEquals("Mae", actualCust.getFirstname());
			assertEquals("Berondo", actualCust.getLastname());
			assertEquals("mae@email.com", actualCust.getEmail());
			assertEquals("Caloocan", actualCust.getLocation());
			
		}
		
		@Test
		@Order(4)
		@DisplayName("Delete Customer - Controller Test")
		void deleteCustomer() throws Exception {
			Customer mockCustomer = customerList.get(0);
			
			when(customerService.findCustomerByID(1)).thenReturn(mockCustomer);
			doNothing().when(customerService).deleteCustomer(any(Customer.class));
			customerService.deleteCustomer(mockCustomer);
			
			MvcResult result =  mockMvc.perform(delete("/customer/delete/{id}",1)
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isOk())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			
			Customer actualCust = mapper.readValue(response, Customer.class);
			
			assertEquals(mockCustomer, actualCust);
			assertEquals(1, actualCust.getCustId());
			assertEquals("Joshua", actualCust.getFirstname());
			assertEquals("Alvarado", actualCust.getLastname());
			assertEquals("joshua@email.com", actualCust.getEmail());
			assertEquals("Manila", actualCust.getLocation());
			
		}
		
		@Test
		@Order(5)
		@DisplayName("Update Customer - Controller Test")
		void updateCustomer() throws Exception {
			Customer mockCustomer = customerList.get(1);
			Customer update = new Customer(	mockCustomer.getCustId(), "Adrienne", 
											"Bellosillo", "ad@email.com", "Bacolod", 
											mockCustomer.getOrders());
			
			when(customerService.findCustomerByID(2)).thenReturn(mockCustomer);
			when(customerService.updateCustomer(mockCustomer, update)).thenReturn(update);
			
			MvcResult result =  mockMvc.perform(put("/customer/update/{id}",2)
									.accept(MediaType.APPLICATION_JSON)
									.contentType("application/json")
									.content(mapper.writeValueAsString(update)))
									.andExpect(status().isAccepted())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			
			Customer actualCust = mapper.readValue(response, Customer.class);
			
			assertNotEquals(mockCustomer, actualCust);
			assertEquals(2, actualCust.getCustId());
			assertEquals("Adrienne", actualCust.getFirstname());
			assertEquals("Bellosillo", actualCust.getLastname());
			assertEquals("ad@email.com", actualCust.getEmail());
			assertEquals("Bacolod", actualCust.getLocation());
			
		}
		
		@Test
		@Order(5)
		@DisplayName("Partial Customer Update (First,LastName) - Controller Test")
		void updatePartialCustomer() throws Exception {
			
			Customer mockCustomer = customerList.get(0);
			CustFirstLastNameOnly custPartial = new CustFirstLastNameOnly("Josh", "Alva");
			Customer custFinal = new Customer(	mockCustomer.getCustId(), 
												custPartial.getFirstname(), 
												custPartial.getLastname(), 
												mockCustomer.getEmail(),
												mockCustomer.getLocation(),
												mockCustomer.getOrders());
			
			when(customerService.findCustomerByID(1)).thenReturn(mockCustomer);
			when(customerService.custPartialUpdate(mockCustomer, custPartial)).thenReturn(custFinal);
			
			MvcResult result =  mockMvc.perform(patch("/customer/partialupdatefirstandlastname/{id}",1)
									.accept(MediaType.APPLICATION_JSON)
									.contentType("application/json")
									.content(mapper.writeValueAsString(custFinal)))
									.andExpect(status().isAccepted())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			
			Customer actualCust = mapper.readValue(response, Customer.class);
			
			assertNotEquals(mockCustomer, actualCust);
			assertEquals(1, actualCust.getCustId());
			assertEquals("Josh", actualCust.getFirstname());
			assertEquals("Alva", actualCust.getLastname());
			assertEquals("joshua@email.com", actualCust.getEmail());
			assertEquals("Manila", actualCust.getLocation());
			
		}	
	}
	
	@Nested
	@DisplayName("Negative Controller Scenarios")
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	class NegativeScenarios {
		@BeforeEach
		void init() {	
			customerList = new ArrayList<>();
			customerList.add(new Customer(1, "Joshua", "Alvarado", "joshua@email.com", "Manila", null));
			customerList.add(new Customer(2, "Joel", "Cerbo", "joel@email.com", "Cebu", null));
			customerList.add(new Customer(3, "Mae", "Berondo", "mae@email.com", "Caloocan", null));
		}
		
		@AfterEach
		void destroy() {
			customerList.clear();
			customerList = null;
		}
		
		@Test
		@Order(1)
		@DisplayName("Get Different ID from MockedID")
		void getCustomerByIdFail() throws Exception {
			Customer customer = customerList.get(0);
		
			
			when(customerService.findCustomerByID(1)).thenReturn(customer);
			when(customerService.findCustomerByID(100))
								.thenThrow(new NotFoundExceptionHandler("Customer with ID 100 "
										+ "does not exist!"));
			
			MvcResult result =  mockMvc.perform(get("/customer/id/{id}", 100)
								.contentType("application/json")
								.content(mapper.writeValueAsString(customer)))
								.andExpect(status().isNotFound())
								.andReturn();	
			
			String response = result.getResponse().getContentAsString();
			assertThrows(NotFoundExceptionHandler.class, () -> customerService.findCustomerByID(100));
			assertEquals("Status : 404 -> Customer with ID 100 does not exist!", response);
			
			
		}
		
		@Test
		@Order(2)
		@DisplayName("Create Customer - Invalid First Name - Controller Test")
		void createInvalidFNameCustomer() throws Exception {
			Customer mockCustomer = customerList.get(2);
			mockCustomer.setFirstname("2Harold");
			
			when(customerService.createCustomer(mockCustomer)).thenReturn(mockCustomer);
			
			MvcResult result =  mockMvc.perform(post("/customer/create")
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isBadRequest())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
			assertEquals("{\"firstname\":\"should only contain letters.\"}", response);
				
		}
		
		@Test
		@Order(3)
		@DisplayName("Create Customer - Invalid Email - Controller Test")
		void createInvalidEmailCustomer() throws Exception {
			Customer mockCustomer = customerList.get(1);
			mockCustomer.setEmail("email.com");
			
			when(customerService.createCustomer(mockCustomer)).thenReturn(mockCustomer);
			
			MvcResult result =  mockMvc.perform(post("/customer/create")
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isBadRequest())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();		
			assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
			assertEquals("{\"email\":\"must be a valid email (ex. a@mail.com)\"}", response);
				
		}
		
		@Test
		@Order(4)
		@DisplayName("Create Customer - Null Values - Controller Test")
		void createCustomerWithNullValues() throws Exception {
			Customer mockCustomer = customerList.get(2);
			mockCustomer.setFirstname(null);
			mockCustomer.setLastname(null);
			
			when(customerService.createCustomer(mockCustomer)).thenReturn(mockCustomer);
			
			MvcResult result =  mockMvc.perform(post("/customer/create")
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isBadRequest())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			TypeReference<HashMap<String,String>> mapRef = new TypeReference<HashMap<String,String>>() {};
			HashMap<String,String> validationErrors = mapper.readValue(response, mapRef);
			
			assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
			validationErrors.entrySet().stream().forEach(e-> {
				assertEquals(e.getKey()+"=cannot be blank.", e.toString());
			});
			
				
		}
		
		@Test
		@Order(5)
		@DisplayName("Delete Non Existing Customer - Controller Test")
		void deleteNonExistingCustomer() throws Exception {
			Customer mockCustomer = customerList.get(0);
			
			when(customerService.findCustomerByID(1)).thenReturn(mockCustomer);
			when(customerService.findCustomerByID(99))
					.thenThrow(new NotFoundExceptionHandler("Customer with ID 99 "
															+ "does not exist! Deletion Failed."));
			doNothing().when(customerService).deleteCustomer(mockCustomer);
			customerService.deleteCustomer(mockCustomer);
			
			MvcResult result =  mockMvc.perform(delete("/customer/delete/{id}",99)
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isNotFound())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
			assertEquals("Status : 404 -> "
						+ "Customer with ID 99 does not exist! Deletion Failed.", response);
			
		}
		
		@Test
		@Order(6)
		@DisplayName("Update Non Existing Customer - Controller Test")
		void updateNonExistingCustomer() throws Exception {
			Customer mockCustomer = customerList.get(0);
			
			when(customerService.findCustomerByID(1)).thenReturn(mockCustomer);
			when(customerService.findCustomerByID(98))
					.thenThrow(new NotFoundExceptionHandler("Customer with ID 98 "
															+ "does not exist!"));
			doNothing().when(customerService).deleteCustomer(mockCustomer);
			customerService.deleteCustomer(mockCustomer);
			
			MvcResult result =  mockMvc.perform(delete("/customer/delete/{id}",98)
									.contentType("application/json")
									.content(mapper.writeValueAsString(mockCustomer)))
									.andExpect(status().isNotFound())
									.andReturn();	
		
			String response = result.getResponse().getContentAsString();
			assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
			assertEquals("Status : 404 -> "
						+ "Customer with ID 98 does not exist!", response);
		}
		
		@Test
		@Order(7)
		@DisplayName("Partially Update Non Existing Customer (First,LastName) - Controller Test")
		void updatePartialCustomer() throws Exception {
			
			Customer mockCustomer = customerList.get(0);
			CustFirstLastNameOnly custPartial = new CustFirstLastNameOnly("Josh", "Alva");
			Customer custFinal = new Customer(	mockCustomer.getCustId(), 
												custPartial.getFirstname(), 
												custPartial.getLastname(), 
												mockCustomer.getEmail(),
												mockCustomer.getLocation(),
												mockCustomer.getOrders());
			
			when(customerService.findCustomerByID(1)).thenReturn(mockCustomer);
			when(customerService.findCustomerByID(97))
			.thenThrow(new NotFoundExceptionHandler("Customer with ID 97 "
													+ "does not exist!"));
			when(customerService.custPartialUpdate(mockCustomer, custPartial)).thenReturn(custFinal);
			
			MvcResult result =  mockMvc.perform(
									patch("/customer/partialupdatefirstandlastname/{id}",97)
									.contentType("application/json")
									.content(mapper.writeValueAsString(custFinal)))
									.andExpect(status().isNotFound())
									.andReturn();	
			
			String response = result.getResponse().getContentAsString();
			assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
			assertEquals("Status : 404 -> "
						+ "Customer with ID 97 does not exist!", response);
			
		}	
	}

}


