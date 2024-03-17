import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Send GET request to /api/v1/orders to get order list")
    public Response sendGetRequestToGetOrderList() {
        Response response = given().when().get("/api/v1/orders");
        return response;
    }
    @Step("Check 200 status code and compare the response body")
    public void check200StatusCodeAndCompareResponseBody(Response response) {
        response.then().statusCode(200)
                .and().assertThat().body("orders", notNullValue());
    }
    @Test
    @DisplayName("Get order list and check 200 status code")
    public void getOrderListAndCheck200StatusCode() {
    Response response = sendGetRequestToGetOrderList();
    check200StatusCodeAndCompareResponseBody(response);
    }
}
