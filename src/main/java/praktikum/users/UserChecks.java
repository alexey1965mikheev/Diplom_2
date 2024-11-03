package praktikum.users;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.Locale;

import static java.net.HttpURLConnection.*;

public class UserChecks {

    //Проверка успешного создания нового пользователя
    @Step("Проверка статуса и структуры ответа сервера при успешной регистрации")
    public void checkSuccessfulRegistration(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK) // Проверка статус-кода 200
                .and().body("success", Matchers.is(true)) // Проверка структуры ответа
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Step("Проверка статуса и структуры ответа сервера при неуспешной регистрации c незаполненным обязательным полем")
    public void checkUnsuccessfulRegistrationWithoutRequiredField(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN) // Проверка статус-кода 403
                .and().body("success", Matchers.is(false)) // Проверка структуры ответа
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Step("Проверка статуса и структуры ответа сервера при неуспешной регистрации существующего пользователя")
    public void checkUnsuccessfulReRegistration(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_FORBIDDEN) // Проверка статус-кода 403
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @Step("Проверка статуса и структуры ответа сервера при успешной авторизации существующего пользователя")
    public void checkSuccessfulLogin(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Step("Проверка статуса и структуры ответа сервера при неуспешной авторизации")
    public void checkUnsuccessfulLogin(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED) // Проверка статус-кода 401
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @Step("Проверка статуса и структуры ответ сервера при изменении данных авторизованного пользователя")
    public void checkSuccessfulChangeCredentialsWithAuth(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_OK) // Проверка статус-кода 200
                .body("success", Matchers.is(true));
                //.and().body("user.email", Matchers.is(email.toLowerCase(Locale.ROOT)))
                //.and().body("user.name", Matchers.is(name));
    }

    @Step("Проверка статуса и структуры ответа сервера при изменении данных неавторизованного пользователя")
    public void checkUnsuccessfulChangeCredentialsWithoutAuth(Response response) {
        response.then().log().all()
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED) // Проверка статус-кода 401
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
}