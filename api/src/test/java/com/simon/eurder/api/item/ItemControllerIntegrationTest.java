package com.simon.eurder.api.item;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import com.simon.eurder.api.customer.CreateCustomerDto;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.service.item.ItemService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ItemDto createdItemDto;
    private Item itemToUpdate;

    @Autowired
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        createItemDto = new CreateItemDto()
                .withName("Golfball")
                .withDescription("Just a golf ball")
                .withPriceInEuro("1")
                .withAmountInStock("50");
        itemToUpdate = new Item("Golf ball", "Golfball", "A normal golf ball", 1, 50);
        itemService.createItem(itemToUpdate);
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
                .put("api/v1/items/" + itemToUpdate.getItemID())
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value()).extract().as(ItemDto.class);

        assertEquals(updatedItemDto.getItemID(), updatedItemDto.getItemID());
        assertEquals(updatedItemDto.getAmountInStock(), 60);

    }

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