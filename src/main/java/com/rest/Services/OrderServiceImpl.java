package com.rest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.entities.Orders;
import com.rest.persistence.OrderRepo;


@Service
public class OrderServiceImpl implements IOrderService{
	
	@Autowired
	private OrderRepo orderRepository;	
	
	
	
	@Override
	public Orders findOrderById(Integer orderId) {
		Optional<Orders> order = orderRepository.findById(orderId);
		if(order.isPresent()) {
			return order.get();
		}	
		return null;
		
	}

	@Override
	public Orders addOrder(Orders order) {
		orderRepository.save(order);
		return order;
		
	}

	@Override
	public void deleteOrder(Orders order) {
		orderRepository.delete(order);
	}

	@Override
	public Orders updateOrder(Integer orderId, Orders order) {
		Orders orderCheck = findOrderById(orderId);
		if(orderCheck == null) {
			return null;
		} else {
			order.setOrderId(orderId);
			orderRepository.save(order);
			return orderCheck;
		}
		
	}

	@Override
	public List<Orders> getAllOrders() {
		return orderRepository.findAll();
	}
}
