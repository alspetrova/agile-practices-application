package com.acme.dbo;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;


public class RestAssuredTests {
    private RequestSpecification request;

    @BeforeEach
    public void init() {
        request = given()
                .baseUri("http://localhost")
                .port(8080)
                .basePath("/dbo/api/")
                .header("X-API-VERSION", 1)
                .contentType(JSON);
    }

    @Test
    @DisplayName("GET client by Exist id")
    public void shouldGetClientByExistId() {
        request
                .when()
                .get("/client/{id}", 2)
                .then()
                .statusCode(SC_OK)
                .body("id", is(2), "login", is("account@acme.com"));
    }

    @Test
    @DisplayName("DELETE client by Login")
    public void shouldDeleteClientByLogin() {
        request
                .when()
                .body("{\n" +
                        " \"login\": \"deleted@email.com\",\n" +
                        " \"salt\": \"somesalt\",\n" +
                        " \"secret\": \"749f09bade8aca749f09bade8aca7556\"\n" +
                        "}")
                .post("/client")
                .then().statusCode(SC_CREATED);

        request
                .when()
                .delete("/client/login/{login}", "deleted@email.com")
                .then()
                .statusCode(SC_OK);
    }
}


