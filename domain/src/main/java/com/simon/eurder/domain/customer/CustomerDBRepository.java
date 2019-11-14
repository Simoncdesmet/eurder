package com.simon.eurder.domain.customer;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class CustomerDBRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void createCustomer(Customer customer) {

        entityManager.persist(customer);

    }

    public List<Customer> getCustomers() {
        return entityManager.createQuery("select cust from Customer cust", Customer.class).getResultList();
    }

    public void clearCustomers() {
        entityManager.createQuery("delete from Customer").executeUpdate();
    }

    public Customer getCustomerByID(String id) {
        return entityManager.createQuery("select cust from Customer cust where EXTERNALID = :id", Customer.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElseThrow(() -> new IllegalArgumentException("Customer not found!"));
    }
}
