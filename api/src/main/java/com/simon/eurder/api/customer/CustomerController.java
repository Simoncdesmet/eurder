package com.simon.eurder.api.customer;

import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.domain.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@RequestMapping(path = "api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDtoMapper customerDtoMapper;
    private final CustomerInputValidator customerInputValidator;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CustomerService customerService, CustomerDtoMapper customerDtoMapper, CustomerInputValidator customerInputValidator) {
        this.customerInputValidator = customerInputValidator;
        logger.info("Constructing!");
        this.customerService = customerService;
        this.customerDtoMapper = customerDtoMapper;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto registerAsCustomer(@RequestBody CreateCustomerDto createCustomerDto) {
        customerInputValidator.validateInput(createCustomerDto);
        logger.info("Creating customer...");
        Customer createdCustomer = customerDtoMapper.CreateCustomerDtoToCustomer(createCustomerDto);
        logger.info("Adding customer to repository...");
        customerService.createCustomer(createdCustomer);
        logger.info("Returning customer...");
        return customerDtoMapper.CustomerToDto(createdCustomer);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(OK)
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers().stream()
                .map(customerDtoMapper::CustomerToDto).collect(Collectors.toList());
    }


    @GetMapping(produces = "application/json", value ="/{customerID}")
    @ResponseStatus(OK)
    public CustomerDto getCustomerByID(@PathVariable("customerID") String customerID) {
        return customerDtoMapper.CustomerToDto(customerService.getCustomerByID(customerID));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleInvalidInput(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), exception.getMessage());
    }
}
