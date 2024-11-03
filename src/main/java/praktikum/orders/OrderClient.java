package praktikum.orders;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static praktikum.StellarBurgersUrls.INGREDIENTS;
import static praktikum.StellarBurgersUrls.ORDER;

public class OrderClient {

    @Step("Получение ингредиентов")
    public Ingredients getIngredient() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .get(INGREDIENTS)
                .body().as(Ingredients.class);
    }

    @Step("Создание заказа с авторизацией пользователя")
    public static Response createOrderWithAuthorization(Order order, String token) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body(order)
                .when()
                .post(ORDER);
    }

    @Step("Создание заказа без авторизации пользователя")
    public static Response createOrderWithoutAuthorization(Order order) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDER);
    }

    @Step("Получение заказов с авторизацией")
    public Response getOrdersWithAuthorization(String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .when()
                .get(ORDER);
    }

    @Step("Получение заказов без авторизации")
    public Response getOrdersWithoutAuthorization() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER);
    }
}
