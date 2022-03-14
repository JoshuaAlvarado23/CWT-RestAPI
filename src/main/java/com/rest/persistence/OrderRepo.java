package com.rest.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.entities.Orders;

public interface OrderRepo extends JpaRepository<Orders, Integer>{

}
