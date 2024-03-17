import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Order;
import org.example.OrderClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private OrderClient orderClient;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

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

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        orderClient = new OrderClient(); }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {"Иван", "Синичка", "Москва, Луговая, 4", 8, "+79008005566", 3, "2024-04-15", "Перезвоните", new String[]{"BLACK"}},
                {"Мария", "Облако", "Москва, Волгоградский проспект", 15, "89003005050", 2, "2024-07-23", "", new String[]{"GREY"}},
                {"Виктор", "Комод", "Москва, Козицкий переулок, 2", 48, "+78005007000", 5, "2024-03-25", "", new String[]{"BLACK", "GREY"}},
                {"Мария", "Облако", "Москва, Волгоградский проспект", 15, "89003005050", 2, "2024-07-23", "", new String[]{}}
        };
    }

    @Step("Send POST request to /api/v1/orders to create new order")
    public Response sendPostRequestToCreateNewOrder(Order order) {
        Response response = orderClient.create(order);
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
        Order order = new Order();
        Response response = sendPostRequestToCreateNewOrder(order);
        check201StatusCodeAndCompareResponseBody(response);
        int orderTrack = response.then().extract().path("track");
        System.out.println(orderTrack);
    }
}
