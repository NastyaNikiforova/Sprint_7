import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        courierClient = new CourierClient();
        courier = new Courier("spider", "1234", "Alex");
    }

    @Step("Send POST request to /api/v1/courier to create courier")
    public Response sendPostRequestToCreateCourier() {
        Response response = courierClient.create(courier);
        return response;
    }
    @Step("Check 201 status code and compare the response body")
    public void check201StatusCodeAndCompareResponseBody(Response response) {
        response.then().statusCode(201)
                .and().assertThat().body("ok", equalTo(true));
    }
    @Step("Check 409 status code and compare the response body")
    public void check409StatusCodeAndCompareResponseBody(Response responseSecond) {
        responseSecond.then().statusCode(409)
                .and().assertThat().body("message", equalTo("Этот логин уже используется"));
    }
    @Step("Check 400 status code and compare the response body")
    public void check400StatusCodeAndCompareResponseBody(Response response) {
        response.then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Create new courier and check 201 status code")
    public void createNewCourierAndCheck201StatusCode() {
        Response response = sendPostRequestToCreateCourier();
        check201StatusCodeAndCompareResponseBody(response);
    }

    @Test
    @DisplayName("Create two couriers with same data and check 409 status code")
    public void createTwoCouriersWithSameDataAndCheck409StatusCode() {
        Response response = sendPostRequestToCreateCourier();
        check201StatusCodeAndCompareResponseBody(response);

        Response responseSecond = sendPostRequestToCreateCourier();
        check409StatusCodeAndCompareResponseBody(responseSecond);
    }

    @Test
    @DisplayName("Create courier without login and check 400 status code")
    public void createCourierWithoutLoginAndCheck400StatusCode() {
        courier = new Courier("", "1234", "Alex");
        Response response = sendPostRequestToCreateCourier();
        check400StatusCodeAndCompareResponseBody(response);
    }

    @After
    public void deleteCourier() {
        Response loginResponse = courierClient.login(courier);
        if (loginResponse.getStatusCode() == 200) {
            int courierId = loginResponse.then().extract().path("id");
            System.out.println(courierId);

            if (courierId != 0) {
                given()
                        .header("Content-type", "application/json")
                        .delete("/api/v1/courier/{courierId}", courierId)
                        .then().statusCode(200);
            }
        }
    }
}
