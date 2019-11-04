package com.simon.eurder.api.customer;

import org.springframework.stereotype.Component;

@Component
public class CustomerInputValidator {

    public void validateInput(CreateCustomerDto createCustomerDto) {
        if (createCustomerDto.getFirstName() == null)
            throw new IllegalArgumentException("You need to provide a first name!");
        if (createCustomerDto.getLastName() == null)
            throw new IllegalArgumentException("You need to provide a last name!");
        if (createCustomerDto.getEmail() == null)
            throw new IllegalArgumentException("You need to provide an email address!");
        if (createCustomerDto.getPhoneNumber() == null)
            throw new IllegalArgumentException("You need to provide a phone number!");
        if (createCustomerDto.getStreetName() == null)
            throw new IllegalArgumentException("You need to provide a street name!");
        if (createCustomerDto.getStreetNumber() == null)
            throw new IllegalArgumentException("You need to provide a street number!");
        if (createCustomerDto.getCity() == null)
            throw new IllegalArgumentException("You need to provide a city!");
        if (createCustomerDto.getPostalCode() == null)
            throw new IllegalArgumentException("You need to provide a postal code!");
    }
}
