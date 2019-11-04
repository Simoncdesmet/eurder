package com.simon.eurder.api;

import com.simon.eurder.domain.Customer;
import com.simon.eurder.domain.CustomerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoMapper {

    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);


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
        logger.info("finished creating CustomerDto");
        return customerDto;
    }

    private CustomerAddressDto customerAddressToDto(CustomerAddress customerAddress) {
        return new CustomerAddressDto()
                .withCity(customerAddress.getCity())
                .withPostalCode(customerAddress.getPostalCode())
                .withStreetName(customerAddress.getStreetName())
                .withStreetNumber(customerAddress.getStreetNumber());
    }
}
