package com.simon.eurder.api.order;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;
import com.simon.eurder.service.order.OrderService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class OrderControllerTest extends RestAssuredTest {

    @Value("${server.port}")
    private int port;


    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPostingRequestForOrder_orderIsCorrectlyCreated() {

        String result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createOrderRequest("Golf ball", 50))
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();
        assertTrue(result.contains("Total price for order is: 50.0"));
        assertEquals("Golf ball", orderService.getAllOrders().get(0).getItemGroups().get(0).getItemID());

    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPostingRequestForOrderWithoutAmount_returnsBadRequest() {

        ItemGroupDtoWrapper requestWithoutAmount = new ItemGroupDtoWrapper()
                .withItemGroupDto(new ItemGroupDto().withItemID("Golf ball"));

        String result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestWithoutAmount)
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertTrue(result.contains("You need to buy at least 1 copy of each item!"));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPostingRequestForOrderWithoutNegativeAmount_returnsBadRequest() {

        ItemGroupDtoWrapper requestWithNegativeAmount = new ItemGroupDtoWrapper()
                .withItemGroupDto(new ItemGroupDto().withItemID("Golf ball").withAmount(-5));

        String result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestWithNegativeAmount)
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().asString();

        assertTrue(result.contains("You need to buy at least 1 copy of each item!"));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPlacingOrder_amountInStockIsUpdated() {
        String result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createOrderRequest("Golf ball", 30))
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();
        assertEquals(itemService.getItemByID("Golf ball").getAmountInStock(), 20);
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPlacingOrderThatExceedsStock_amountInReorderIsUpdated() {
        String result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createOrderRequest("Golf ball", 60))
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();


        assertEquals(itemService.getItemByID("Golf ball").getReorderAmount(), 10);
        assertEquals(itemService.getItemByID("Golf ball").getAmountInStock(), 0);
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPlacingMultipleOrdersThatExceedsStock_amountInReorderIsUpdated() {

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createOrderRequest("Golf ball", 70))
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createOrderRequest("Golf ball", 50))
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();

        assertEquals(itemService.getItemByID("Golf ball").getReorderAmount(), 70);
        assertEquals(itemService.getItemByID("Golf ball").getAmountInStock(), 0);
    }



    private ItemGroupDtoWrapper createOrderRequest(String itemID, int amount) {
        return new ItemGroupDtoWrapper()
                .withItemGroupDto(new ItemGroupDto().withItemID(itemID).withAmount(amount));
    }

}