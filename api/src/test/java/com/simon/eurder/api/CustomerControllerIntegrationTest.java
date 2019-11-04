package com.simon.eurder.api;

import com.simon.eurder.api.customer.CreateCustomerDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    void whenCreatingCustomer_customerIsInRepository() {

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
}