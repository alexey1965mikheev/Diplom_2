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

public class LoginUserTest {

    private String email;
    private String password;
    private String name;
    private UserClient userClient;
    private UserChecks userCheck;
    private User user;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        email = "email" + RandomStringUtils.randomAlphanumeric(5) + "@yandex.ru";
        password = "password" + RandomStringUtils.randomAlphanumeric(8);
        name = "name" + RandomStringUtils.randomAlphanumeric(6);
        user = new User(email, password, name);
        userClient = new UserClient();
        userCheck = new UserChecks();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Авторизация существующего пользователя с корректными данными")
    public void authorizationTest() {
        UserClient.createUser(user);
        Response response = UserClient.loginForExistingUser(user);
        userCheck.checkSuccessfulLogin(response);
    }

    @Test
    @DisplayName("Авторизация с неверным логином.")
    @Description("Авторизация пользователя c некорректным логином.")
    public void authorizationWithIncorrectEmailTest() {
        UserClient.createUser(user);
        user.setEmail("incorrect" + email);
        Response response = UserClient.loginForExistingUser(user);
        userCheck.checkUnsuccessfulLogin(response);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем.")
    @Description("Авторизация пользователя c некорректным паролем.")
    public void authorizationWithIncorrectPasswordTest() {
        UserClient.createUser(user);
        user.setPassword("incorrect" + password);
        Response response = UserClient.loginForExistingUser(user);
        userCheck.checkUnsuccessfulLogin(response);
    }

    @After
    public void tearDown() {

        String accessToken = UserClient.loginForExistingUser(user).then().extract().path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
