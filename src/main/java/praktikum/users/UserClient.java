package praktikum.users;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static praktikum.StellarBurgersUrls.*;

public class UserClient {

    @Step("Создание нового пользователя.")
    public static Response createUser(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Логин под существующим пользователем.")
    public static Response loginForExistingUser(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Изменение данных авторизованного пользователя")
    public Response changeCredentialsWithAuthorization(User user, String token) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("authorization", token)
                .body(user)
                .when()
                .patch(CREDENTIALS);
    }

    @Step("Изменение данных неавторизованного пользователя")
    public Response changeCredentialsWithoutAuthorization(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(CREDENTIALS);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(CREDENTIALS);
    }
}
