package org.example;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    public Response create(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}
