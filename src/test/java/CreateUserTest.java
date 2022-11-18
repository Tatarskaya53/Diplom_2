import io.qameta.allure.Step;
import user.User;
import user.UserClient;
import user.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    User user;
    UserClient userClient = new UserClient();
    private String token;

    @Before
    @Step("Созданиие рандомного пользователя")
    public void setup() {
        user = User.createRandomUser();
    }

    @After
    @Step("Удаление пользователя")
    public void teardown() {
        try {
            UserCredentials credentials = UserCredentials.from(user);
            token = userClient.loginUser(credentials)
                    .then()
                    .extract().path("accessToken");
            userClient.deleteUser(user, token);
            System.out.println("User removed");
        } catch (IllegalArgumentException e) {
            System.out.println("User did not create");
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Step("Создание пользователя")
    public void createUserTest() {
        userClient.createUniqueUser(user)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Step("Создание пользователя")
    public void createIdenticalUserTest() {
        userClient.createUniqueUser(user);
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя с пустым полем email")
    @Step("Создание пользователя")
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Step("Создание пользователя")
    public void createUserWithoutPassTest() {
        user.setPassword("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Step("Создание пользователя")
    public void createUserWithoutNameTest() {
        user.setName("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }
}