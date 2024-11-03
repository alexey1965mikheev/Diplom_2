package praktikum.orders;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.Locale;

import static java.net.HttpURLConnection.*;

public class OrderChecks {

    @Step("Проверка ответа сервера при заказе без ингредиентов")
    public void checkUnSuccessOrderCreation(Response response, String name, String email) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class))
                .and().body("order.ingredients", Matchers.notNullValue())
                .and().body("order._id", Matchers.notNullValue())
                .and().body("order.owner.name", Matchers.is(name))
                .and().body("order.owner.email", Matchers.is(email.toLowerCase(Locale.ROOT)))
                .and().body("order.status", Matchers.is("done"))
                .and().body("order.name", Matchers.notNullValue())
                .and().body("order.price", Matchers.notNullValue());
    }

    @Step("Проверка ответа сервера при заказе без авторизации")
    public void checksOrderCreationWithoutAuthorization(Response response) {
        response.then().log().all()
                .assertThat().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class))
                .and().statusCode(HTTP_OK);
    }

    @Step("Проверка ответа сервера при заказе без ингредиентов")
    public void checkOrderWithoutIngredients(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Step("Проверка ответа сервера при заказе без ингредиентов.")
    public void checkOrderWithIncorrectHash(Response response) {
        response.then().log().all()
                .statusCode(HTTP_INTERNAL_ERROR);
    }

    @Step("Проверка ответа сервера при получении заказов с авторизацией")
    public void checkGetOrderWithAuthorization(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }

    @Step("Проверка ответа сервера при получении заказов без авторизации")
    public void checkGetOrderWithoutAuthorization(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
}
