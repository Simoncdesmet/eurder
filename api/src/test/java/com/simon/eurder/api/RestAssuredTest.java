package com.simon.eurder.api;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static java.lang.Integer.parseInt;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application.class)
public abstract class RestAssuredTest {

    @Autowired
    private Environment env;

    public RequestSpecification givenRequestForUser(String username, String password) {
        String encodedString = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        return given()
                .header("Authorization", "Basic " + encodedString)
                .baseUri("http://localhost")
                .port(parseInt(env.getProperty("server.port")));
    }
}

