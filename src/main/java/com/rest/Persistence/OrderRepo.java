package com.rest.Persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.Entities.Orders;

public interface OrderRepo extends JpaRepository<Orders, Integer>{

}
