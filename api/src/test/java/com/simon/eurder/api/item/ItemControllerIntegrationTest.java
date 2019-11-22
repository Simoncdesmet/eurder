package com.simon.eurder.api.item;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.service.item.ItemService;
import io.restassured.RestAssured;
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

import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class ItemControllerIntegrationTest extends RestAssuredTest {


    @Value("${server.port}")
    private int port;

    private CreateItemDto createItemDto;

    @Autowired
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        createItemDto = new CreateItemDto()
                .withName("Golfball")
                .withDescription("Just a golf ball")
                .withPriceInEuro("1")
                .withAmountInStock("50");
    }

    @AfterEach
    void tearDown() {
        itemService.clearItems();
    }

    @Sql(scripts = {"classpath:delete-rows.sql"})
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

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingItemWithoutPrice_returnsBadRequest() {

        CreateItemDto createItemDtoWithoutPrice = new CreateItemDto()
                .withName("Golfball without price")
                .withDescription("")
                .withAmountInStock("50");

        RestAssured
                .given()
                .body(createItemDtoWithoutPrice)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(8922)
                .post("api/v1/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Sql(scripts = {"classpath:delete-rows.sql"})
    @Test
    void whenCreatingItemWithStringsInPrice_returnsBadRequest() {

        CreateItemDto createItemWithStringsInPrice = new CreateItemDto()
                .withName("Golfball with strings in price")
                .withDescription("")
                .withPriceInEuro("50 euro")
                .withAmountInStock("50");

        RestAssured
                .given()
                .body(createItemWithStringsInPrice)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(8922)
                .post("api/v1/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-item.sql"})
    @Test
    void whenUpdatingItemWithNewAmount_amountIsUpdated() {

        CreateItemDto updateItemDtoWithNewAmount = new CreateItemDto()
                .withName("Golfball without price")
                .withDescription("")
                .withPriceInEuro("1")
                .withAmountInStock("60");

        ItemDto updatedItemDto = RestAssured
                .given()
                .body(updateItemDtoWithNewAmount)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(8922)
                .put("api/v1/items/" + "Golf ball")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value()).extract().as(ItemDto.class);

        assertEquals(updatedItemDto.getExternalID(), updatedItemDto.getExternalID());
        assertEquals(updatedItemDto.getAmountInStock(), 60);

    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenUpdatingItemWithNonExistingID_returnsBadRequest() {

        String result = RestAssured
                .given()
                .body(createItemDto)
                .accept(JSON)
                .contentType(JSON)
                .when()
                .port(8922)
                .put("api/v1/items/" + "FakeID")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value()).extract().asString();

        assertTrue(result.contains("This item does not exist!"));
    }
}