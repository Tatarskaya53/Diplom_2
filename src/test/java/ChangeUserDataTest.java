import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class ChangeUserDataTest {
    User user;
    UserClient userClient = new UserClient();
    private String token;

    @Before
    public void setup() {
        user = User.createRandomUser();
        token = userClient.createUniqueUser(user)
                .then()
                .extract().path("accessToken");
    }

    @After
    public void teardown() {
        userClient.deleteUser(user, token);
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    public void changeUserEmailWithAuthorizationTest() {
        user.setEmail("patchedEmail");
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    public void changeUserPasswordWithAuthorizationTest() {
        user.setPassword("patchedPass");
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void changeUserNameWithAuthorizationTest() {
        user.setName("patchedName");
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение почты пользователя без авторизации")
    public void changeUserEmailWithoutAuthorizationTest() {
        user.setEmail("patchedEmail");
        token = "";
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    public void changeUserPasswordWithoutAuthorizationTest() {
        user.setPassword("patchedPass");
        token = "";
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthorizationTest() {
        user.setName("patchedName");
        token = "";
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }

}