package praktikum.orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.users.User;
import praktikum.users.UserClient;

import static praktikum.StellarBurgersUrls.BASE_URI;

public class GetOrderTest {

    private final String email = "email" + RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
    private final String password = "password" + RandomStringUtils.randomAlphanumeric(8);
    private final String name = "name" + RandomStringUtils.randomAlphanumeric(6);
    private UserClient userClient;
    private OrderClient orderClient;
    private OrderChecks orderChecks;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        userClient = new UserClient();
        orderClient = new OrderClient();
        orderChecks = new OrderChecks();
        user = new User(email, password, name);
        accessToken = UserClient.createUser(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Получение списка заказов авторизованный пользователь.")
    @Description("Успешная проверка получение списка заказов авторизованного пользователя.")
    public void getOrderWithAuthorizationTest() {
        Response response = orderClient.getOrdersWithAuthorization(accessToken);
        orderChecks.checkGetOrderWithAuthorization(response);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации.")
    @Description("Неуспешная проверка получение списка заказов без авторизации.")
    public void getOrderWithoutAuthorizationTest() {
        Response response = orderClient.getOrdersWithoutAuthorization();
        orderChecks.checkGetOrderWithoutAuthorization(response);
    }

    @After
    public void tearDown() {

        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
