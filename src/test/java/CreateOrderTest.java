import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    Gson gson = new Gson();
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] getOrderData() {
        return new Object[][] {
                {"Иван", "Синичка", "Москва, Луговая, 4", 4, "+79008005566", 3, "2024-04-15", "Перезвоните", new String[]{"BLACK"}},
                {"Мария", "Облако", "Москва, Волгоградский проспект", 15, "89003005050", 2, "2024-07-23", "", new String[]{"GREY"}},
                {"Виктор", "Комод", "Москва, Козицкий переулок, 2", 48, "+78005007000", 5, "2024-03-25", "", new String[]{"BLACK", "GREY"}},
                {"Пётр", "Иванов", "Москва, Волгоградский проспект", 15, "89003005050", 2, "2024-07-23", "", new String[]{}}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Send POST request to /api/v1/orders to create new order")
    public Response sendPostRequestToCreateNewOrder(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(json)
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
        Response response = sendPostRequestToCreateNewOrder(gson.toJson(getOrderData()));
        check201StatusCodeAndCompareResponseBody(response);
        int orderTrack = response.then().extract().path("track");
        System.out.println(orderTrack);
    }
}
