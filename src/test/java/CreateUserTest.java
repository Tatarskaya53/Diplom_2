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
    public void setup() {
        user = User.createRandomUser();
    }

    @After
    public void teardown() {
        try {
            UserCredentials credentials = UserCredentials.from(user);
            token = userClient.loginUser(credentials)
                    .then()
                    .extract().path("accessToken");
            userClient.deleteUser(user, token);
        } catch (IllegalArgumentException e) {
            System.out.println("User did not create");
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserTest() {
        userClient.createUniqueUser(user)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
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
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPassTest() {
        user.setPassword("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest() {
        user.setName("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }
}