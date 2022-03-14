package com.rest.services;

import java.util.List;

import com.rest.entities.Orders;

public interface IOrderService {
	
	public Orders findOrderById(Integer orderId);
	
	public Orders addOrder(Orders order);
	
	public void deleteOrder(Orders order);
	
	public List<Orders> getAllOrders();
	
	public Orders updateOrder(Integer orderId,Orders order);
}
