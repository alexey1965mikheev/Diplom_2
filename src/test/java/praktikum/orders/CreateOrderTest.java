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

import java.util.ArrayList;
import java.util.List;

import static praktikum.StellarBurgersUrls.BASE_URI;

public class CreateOrderTest {

    private final String email = "email" + RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
    private final String password = "password" + RandomStringUtils.randomAlphanumeric(8);
    private final String name = "name" + RandomStringUtils.randomAlphanumeric(6);

    private User user;
    private Order order;
    private List<String> ingredient;

    private UserClient userClient;
    private OrderClient orderClient;
    private OrderChecks orderChecks;

    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        userClient = new UserClient();
        orderClient = new OrderClient();
        orderChecks = new OrderChecks();

        user = new User(email, password, name);
        accessToken = UserClient.createUser(user).then().extract().path("accessToken");

        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией.")
    @Description("Успешное создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthorizationTest() {
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());

        Response createResponse = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderChecks.checkUnSuccessOrderCreation(createResponse, name, email);
    }

    @Test
    @DisplayName("Создание заказа без авторизации.")
    @Description("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());

        Response response = OrderClient.createOrderWithoutAuthorization(order);
        orderChecks.checksOrderCreationWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов с авторизацией")
    @Description("Проверка создания заказа без ингредиентов с авторизацией")
    public void createEmptyOrderWithAuthorization() {
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderChecks.checkOrderWithoutIngredients(response);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с неверным хешем ингредиентов")
    @Description("Проверка создания заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHashTest() {
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id() + "khilunjlknjkbyg9876");
        ingredient.add(ingredients.getData().get(2).get_id() + "op9iuojuigtyfjkuhh");

        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderChecks.checkOrderWithIncorrectHash(response);
    }

    @After
    public void tearDown() {
        // Удаление созданного пользователя
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}

