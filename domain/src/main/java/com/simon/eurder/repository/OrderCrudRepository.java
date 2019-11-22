package com.simon.eurder.repository;

import com.simon.eurder.domain.order.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderCrudRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    Optional<Order> findByExternalId(String id);

    List<Order> findAllByCustomerID(String customerId);

}
