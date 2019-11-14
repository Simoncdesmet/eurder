package com.simon.eurder.domain.customer;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerSeq")
    @SequenceGenerator(name = "customerSeq", sequenceName = "customer_seq", allocationSize = 1)
    @Column(name = "ID")
    private long internalID;

    @Column(name = "EXTERNALID")
    private String customerID;

    @Column(name = "FIRSTNAME")
    private String firstName;
    @Column(name = "LASTNAME")
    private String lastName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @Embedded
    private CustomerAddress customerAddress;


    private Customer() {
    }

    public Customer(String firstName, String lastName, String email, String phoneNumber, CustomerAddress customerAddress) {
        this(UUID.randomUUID().toString(), firstName, lastName, email, phoneNumber, customerAddress);
    }

    public Customer(String customerID, String firstName, String lastName, String email, String phoneNumber, CustomerAddress customerAddress) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.customerAddress = customerAddress;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }
}
