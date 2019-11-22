package com.simon.eurder.repository;

import com.simon.eurder.domain.customer.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerCrudRepository extends CrudRepository<Customer, Long> {
    List<Customer> findAll();
    Optional<Customer> findByCustomerID(String customerId);
    void deleteAll();
}
