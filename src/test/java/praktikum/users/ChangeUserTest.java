package praktikum.users;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static praktikum.StellarBurgersUrls.BASE_URI;

public class ChangeUserTest {

    private final String email = "email" + RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
    private final String password = "password" + RandomStringUtils.randomAlphanumeric(8);
    private final String name = "name" + RandomStringUtils.randomAlphanumeric(6);
    private User user;
    private UserClient userClient;
    private UserChecks userCheck;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        userClient = new UserClient();
        userCheck = new UserChecks();
        user = new User(email, password, name);
        accessToken = UserClient.createUser(user).then().extract().path("accessToken");
    }


    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Успешное изменение имени для авторизованного пользователя")
    public void changeUserNameWithAuthorizationTest() {
        user.setName("new" + name);
        Response response = userClient.changeCredentialsWithAuthorization(user, accessToken);
        userCheck.checkSuccessfulChangeCredentialsWithAuth(response);
    }

    @Test
    @DisplayName("Изменение логина пользователя с авторизацией")
    @Description("Успешное изменение email для авторизованного пользователя")
    public void changeUserEmailWithAuthorizationTest() {
        user.setEmail("new" + email);
        Response response = userClient.changeCredentialsWithAuthorization(user, accessToken);
        userCheck.checkSuccessfulChangeCredentialsWithAuth(response);
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    @Description("Успешное изменение пароля для авторизованного пользователя")
    public void changeUserPasswordWithAuthorizationTest() {
        user.setPassword("new" + password);
        Response response = userClient.changeCredentialsWithAuthorization(user, accessToken);
        userCheck.checkSuccessfulChangeCredentialsWithAuth(response);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Неуспешное изменение имени для неавторизованного пользователя")
    public void changeUserNameWithoutAuthorizationTest() {
        user.setName("new" + name);
        Response response = userClient.changeCredentialsWithAuthorization(user, "abc");
        userCheck.checkUnsuccessfulChangeCredentialsWithoutAuth(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
