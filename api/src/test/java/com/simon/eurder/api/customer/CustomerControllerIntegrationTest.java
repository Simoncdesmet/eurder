package com.simon.eurder.api.customer;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.service.customer.CustomerService;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static io.restassured.http.ContentType.JSON;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class CustomerControllerIntegrationTest extends RestAssuredTest {

    @Value("${server.port}")
    private int port;

    private CreateCustomerDto createCustomerDto;

    @Autowired
    private CustomerService customerService;


    @BeforeEach
    void setUp() {
        createCustomerDto = new CreateCustomerDto()
                .withCity("Heverlee")
                .withFirstName("Simon")
                .withLastName("Desmet")
                .withEmail("simoncdesmet@gmail.com")
                .withPhoneNumber("0487/57.70.40")
                .withPostalCode("3001")
                .withStreetName("Leeuwerikenstraat")
                .withStreetNumber("101/3");
    }


    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
    @Test
    void whenCreatingCustomer_customerIsInCreated() {

        RestAssured
                .given()
                .body(createCustomerDto)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(8922)
                .post("api/v1/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
    @Test
    void givenRepositoryWith1Customer_whenGettingAllCustomers_returnsAllCustomers() {


        List<CustomerDto> customers =
                RestAssured
                        .given()
                        .contentType(JSON)
                        .when()
                        .port(8922)
                        .get("api/v1/customers")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body()
                        .jsonPath().getList(".", CustomerDto.class);

        Assertions.assertThat(customers.size()).isEqualTo(1);
        Assertions.assertThat(customers.stream()
                .map(CustomerDto::getEmail)
                .anyMatch(customer -> customer.equals("simoncdesmet@gmail.com"))).isEqualTo(true);
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
    @Test
    void givenRepositoryWithCustomer_whenGettingCustomerByID_returnsCustomer() {


        CustomerDto gotCustomer =
                RestAssured
                        .given()
                        .contentType(JSON)
                        .when()
                        .port(8922)
                        .get("api/v1/customers/"+"Test001")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(CustomerDto.class);

        Assertions.assertThat(gotCustomer.getCustomerID()).isEqualTo("Test001");
        Assertions.assertThat(gotCustomer.getEmail()).isEqualTo("simoncdesmet@gmail.com");
        Assertions.assertThat(gotCustomer.getCity()).isEqualTo("Leuven");

    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
    @Test
    void givenNonExistingCustomerID_whenGettingCustomerByID_returnsBadRequest() {

        String result =
                RestAssured
                        .given()
                        .contentType(JSON)
                        .when()
                        .port(8922)
                        .get("api/v1/customers/"+"FakeID")
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .extract()
                        .asString();

        Assertions.assertThat(result).contains("Customer not found!");

    }
}