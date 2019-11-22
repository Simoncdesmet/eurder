package com.simon.eurder.api.shipment;

import com.simon.eurder.api.Application;
import com.simon.eurder.api.RestAssuredTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
class ShipmentControllerTest extends RestAssuredTest {

    @Value("${server.port}")
    private int port;

    @Test
    void whenGettingShipmentOverview_OverViewIsReturned() {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .port(8922)
                .get("api/v1/shipments")
                .then()
                .statusCode(HttpStatus.OK.value());

    }
}