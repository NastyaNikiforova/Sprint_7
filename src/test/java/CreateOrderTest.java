import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private Order order;

    public CreateOrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[] getOrderData() {
        return new Object[][] {
                {new Order("Иван", "Синичка", "Москва, Луговая, 4", 4, "+79008005566", 3, "2024-04-15", "Перезвоните", new String[]{"BLACK"})},
                {new Order("Мария", "Облако", "Москва, Волгоградский проспект", 15, "89003005050", 2, "2024-07-23", "", new String[]{"GREY"})},
                {new Order("Виктор", "Комод", "Москва, Козицкий переулок, 2", 48, "+78005007000", 5, "2024-03-25", "", new String[]{"BLACK", "GREY"})},
                {new Order("Пётр", "Иванов", "Москва, Волгоградский проспект", 15, "89003005050", 2, "2024-07-23", "", new String[]{})}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Send POST request to /api/v1/orders to create new order")
    public Response sendPostRequestToCreateNewOrder() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
        return response;
    }
    @Step("Check 201 status code and compare the response body")
    public void check201StatusCodeAndCompareResponseBody(Response response) {
        response.then().statusCode(201)
                .and().assertThat().body("track", notNullValue());
    }

    @Test
    @DisplayName("Create new order and check status code")
    public void createNewOrderAndCheck201StatusCode() {
        Response response = sendPostRequestToCreateNewOrder();
        check201StatusCodeAndCompareResponseBody(response);
        int orderTrack = response.then().extract().path("track");
        System.out.println(orderTrack);
    }
}
