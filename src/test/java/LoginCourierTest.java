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
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        courierClient = new CourierClient();
        courier = new Courier("pancake", "cake01", "Alex");

        courierClient.create(courier);
    }

    @Step("Send POST request to /api/v1/courier/login to login courier")
    public Response sendPostRequestToLoginCourier() {
        Response loginResponse = courierClient.login(courier);
        return loginResponse;
    }
    @Step("Check 200 status code and compare the response body")
    public void check200StatusCodeAndCompareResponseBody(Response loginResponse) {
        loginResponse.then().statusCode(200)
                .and().assertThat().body("id", notNullValue());
    }
    @Step("Check 404 status code and compare the response body")
    public void check404StatusCodeAndCompareResponseBody(Response loginResponse) {
        loginResponse.then().statusCode(404)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @Step("Check 400 status code and compare the response body")
    public void check400StatusCodeAndCompareResponseBody(Response loginResponse) {
        loginResponse.then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login courier and check 200 status code")
    public void loginCourierAndCheck200StatusCode() {
        Response loginResponse = sendPostRequestToLoginCourier();
        check200StatusCodeAndCompareResponseBody(loginResponse);
    }
    @Test
    @DisplayName("Login courier with non-existent data and check 404 status code")
    public void loginCourierWithNonExistentDataAndCheck404StatusCode() {
        courier = new Courier("pancakes", "cake33", "");
        Response loginResponse = sendPostRequestToLoginCourier();
        check404StatusCodeAndCompareResponseBody(loginResponse);
    }

    @Test
    @DisplayName("Login courier without password and check 400 status code")
    public void loginCourierWithoutPasswordAndCheck400StatusCode() {
        courier = new Courier("pancake", "", "");
        Response loginResponse = sendPostRequestToLoginCourier();
        check400StatusCodeAndCompareResponseBody(loginResponse);
    }
    @After
    public void deleteCourier() {
       Response loginResponse = courierClient.login(courier);
        if (loginResponse.getStatusCode() == 200) {
            int courierId = courierClient.login(courier)
                    .then().extract().path("id");
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
