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

public class CreateUserTest {

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
    }

    @Test
    @DisplayName("Создать уникального пользователя")
    @Description("Успешная регистрация пользователя с корректными данными")
    public void createUniqueUser() {

        user = new User(email, password, name);
        Response response = UserClient.createUser(user);
        userCheck.checkSuccessfulRegistration(response);
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Ошибка при регистрации существующего пользователя")
    public void createDuplicateUser() {

        user = new User(email, password, name);
        UserClient.createUser(user);
        Response response = UserClient.createUser(user);
        userCheck.checkUnsuccessfulReRegistration(response);
    }

    @Test
    @DisplayName("Создать пользователя без логина")
    @Description("Ошибка при регистрации пользователя с незаполненным полем 'Email'")
    public void createUserWithoutEmail() {

        user = new User("", password, name);
        Response response = UserClient.createUser(user);
        userCheck.checkUnsuccessfulRegistrationWithoutRequiredField(response);
    }

    @Test
    @DisplayName("Создать пользователя без пароля")
    @Description("Ошибка при регистрации пользователя с незаполненным полем 'Password'")
    public void createUserWithoutPassword() {

        user = new User(email, "", name);
        Response response = UserClient.createUser(user);
        userCheck.checkUnsuccessfulRegistrationWithoutRequiredField(response);
    }

    @Test
    @DisplayName("Создать пользователя без имени")
    @Description("Ошибка при регистрации пользователя с незаполненным полем 'Name'")
    public void createUserWithoutName() {

        user = new User(email, password, "");
        Response response = UserClient.createUser(user);
        userCheck.checkUnsuccessfulRegistrationWithoutRequiredField(response);
    }

    @After
    public void tearDown() {
        String accessToken = UserClient.loginForExistingUser(user).then().extract().path("accessToken");
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}



