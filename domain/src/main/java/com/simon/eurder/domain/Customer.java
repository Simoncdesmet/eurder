package com.simon.eurder.domain;

import java.util.UUID;

public class Customer {

    private String customerID, firstName, lastName, email, phoneNumber;
    private CustomerAddress customerAddress;

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
