package com.simon.eurder.api.item;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.api.customer.CreateCustomerDto;
import com.simon.eurder.domain.item.Item;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class ItemControllerIntegrationTest extends RestAssuredTest {


    @Value("${server.port}")
    private int port;

    private CreateItemDto createItemDto;

    @BeforeEach
    void setUp() {
        createItemDto = new CreateItemDto()
                .withName("Golfball")
                .withDescription("Just a golf ball")
                .withPriceInEuro(1)
                .withAmountInStock(50);
    }

    @Test
    void whenCreatingItem_itemIsCreated() {

        RestAssured
                .given()
                .body(createItemDto)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(8922)
                .post("api/v1/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }
}