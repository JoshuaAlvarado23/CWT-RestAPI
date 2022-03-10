package com.rest.Controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.Entities.Customer;
import com.rest.Entities.Orders;
import com.rest.Exceptions.NotFoundExceptionHandler;
import com.rest.Services.ICustomerService;
import com.rest.Services.IOrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private ICustomerService custService;
	
	@Autowired
	private IOrderService orderService;
	
	@PostMapping("/add/{custId}")
	public ResponseEntity<Orders> addOrder (@Valid @PathVariable Integer custId, 
											@Valid @RequestBody Orders orderDTO) {
		Customer getCust = custService.findCustomerByID(custId);
	
		if(getCust == null) {
			throw new NotFoundExceptionHandler("Customer Not Found!");
		} else {
			orderDTO.setCustomer(getCust);
			orderService.addOrder(orderDTO);
			return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
		}
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Orders>> getAllOrders() {
		return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
	}
	

	
	@DeleteMapping("/delete")
	public ResponseEntity<Orders> deleteOrder(@Valid @RequestParam Integer orderId){
		Orders order = orderService.findOrderById(orderId);
		if(order == null) {
			throw new NotFoundExceptionHandler("Order with ID "+orderId+" not found.");
		} else {
			orderService.deleteOrder(order);
			return new ResponseEntity<>(order, HttpStatus.OK);
		}
	}
	
	@PutMapping("/update/{orderId}")
	public ResponseEntity<Orders> deleteOrder(	@Valid @RequestBody Orders orderDTO,
												@PathVariable @Valid Integer orderId) {
		Orders order = orderService.findOrderById(orderId);
		if(order == null) {
			throw new NotFoundExceptionHandler("Order with ID "+orderId+" not found.");
		} else {
			orderService.updateOrder(orderId, orderDTO);
			return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
		}
	}

	
}
