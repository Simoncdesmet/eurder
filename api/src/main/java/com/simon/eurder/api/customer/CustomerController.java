package com.simon.eurder.api.customer;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.domain.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping(path = "api/v1/customers")
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerDtoMapper customerDtoMapper;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CustomerService customerService, CustomerDtoMapper customerDtoMapper) {
        logger.info("Constructing!");
        this.customerService = customerService;
        this.customerDtoMapper = customerDtoMapper;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto registerAsCustomer(@RequestBody CreateCustomerDto createCustomerDto) {
        logger.info("Creating customer...");
        Customer createdCustomer = customerDtoMapper.CreateCustomerDtoToCustomer(createCustomerDto);
        logger.info("Adding customer...");
        customerService.createCustomer(createdCustomer);
        logger.info("Returning customer...");
        return customerDtoMapper.CustomerToDto(createdCustomer);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public String thisWorks() {
        return "this works";
    }
}
