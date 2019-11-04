package com.simon.eurder.api.customer;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoMapper {


    public Customer CreateCustomerDtoToCustomer(CreateCustomerDto createCustomerDto) {
        return new Customer(
                createCustomerDto.getFirstName(),
                createCustomerDto.getLastName(),
                createCustomerDto.getEmail(),
                createCustomerDto.getPhoneNumber(),
                new CustomerAddress(
                        createCustomerDto.getStreetName(),
                        createCustomerDto.getStreetNumber(),
                        createCustomerDto.getPostalCode(),
                        createCustomerDto.getCity()));
    }

    public CustomerDto CustomerToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto()
                .withCustomerID(customer.getCustomerID())
                .withFirstName(customer.getFirstName())
                .withLastName(customer.getLastName())
                .withEmail(customer.getEmail())
                .withPhoneNumber(customer.getPhoneNumber())
                .withStreetName(customer.getCustomerAddress().getStreetName())
                .withStreetNumber(customer.getCustomerAddress().getStreetNumber())
                .withPostalCode(customer.getCustomerAddress().getPostalCode())
                .withCity(customer.getCustomerAddress().getCity());
        return customerDto;
    }

}
