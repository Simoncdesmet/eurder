package com.simon.eurder.api.customer;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.api.customer.CreateCustomerDto;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.http.ContentType.JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class CustomerControllerIntegrationTest extends RestAssuredTest {

    @Value("${server.port}")
    private int port;

    private CreateCustomerDto createCustomerDto;


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

    @AfterEach
    void tearDown() {

    }

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
                .anyMatch(customer->customer.equals("simoncdesmet@gmail.com"))).isEqualTo(true);
    }
}