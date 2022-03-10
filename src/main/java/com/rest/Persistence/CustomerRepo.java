package com.rest.Persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.Entities.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer>{
	
	Optional<Customer> findByEmail(String email);


}
