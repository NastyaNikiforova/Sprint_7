package org.example;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    //метод, возвращающий ответ при POST-запросе на ручку /api/v1/courier
    public Response create(Courier courier) {
     return given()
             .header("Content-type", "application/json")
             .body(courier)
             .when()
             .post("/api/v1/courier");
    }

    //метод, возвращающий ответ при POST-запросе на ручку /api/v1/courier/login
    public Response login(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }
}
