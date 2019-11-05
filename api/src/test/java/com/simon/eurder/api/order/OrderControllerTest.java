package com.simon.eurder.api.order;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.domain.customer.CustomerRepository;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.item.ItemRepository;
import com.simon.eurder.domain.order.OrderRepository;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;
import com.simon.eurder.service.order.OrderService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class OrderControllerTest extends RestAssuredTest {

    @Value("${server.port}")
    private int port;


    private ItemGroupDtoWrapper itemGroupDtoWrapper;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ItemService itemService;


    @BeforeEach
    void setUp() {
        itemGroupDtoWrapper = new ItemGroupDtoWrapper()
                .withItemGroupDto(new ItemGroupDto().withItemID("Golf ball").withAmount(50));

        CustomerAddress customerAddress = new CustomerAddress(
                "Leeuwerikenstraat",
                "101/3",
                "3001",
                "Heverlee");

        Customer customer = new Customer(
                "Test001",
                "Simon",
                "Desmet",
                "simoncdesmetgmail.com",
                "0487/57.70.40",
                customerAddress);

        customerService.createCustomer(customer);

        Item item = new Item(
                "Golf ball",
                "Golf ball",
                "A golf ball",
                1,
                50);
        itemService.createItem(item);

    }

    @Test
    void whenPostingRequestForOrder_orderIsCorrectlyCreated() {

        String result = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(itemGroupDtoWrapper)
                .when()
                .port(8922)
                .post("api/v1/orders/Test001")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract().asString();

        assertEquals(result, "Total order price: 50.0");
        assertEquals("Golf ball", orderService.getAllOrders().get(0).getItemGroups().get(0).getItemID());

    }


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

    @Test
    void whenPostingRequestForOrderWithoutNegativeAmount_returnsBadRequest() {

        ItemGroupDtoWrapper requestWithoutAmount = new ItemGroupDtoWrapper()
                .withItemGroupDto(new ItemGroupDto().withItemID("Golf ball").withAmount(-5));

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
}