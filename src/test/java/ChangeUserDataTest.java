import io.qameta.allure.Step;
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
    @Step("Созданиие рандомного пользователя")
    public void setup() {
        user = User.createRandomUser();
        token = userClient.createUniqueUser(user)
                .then()
                .extract().path("accessToken");
        System.out.println("User created");
    }

    @After
    @Step("Удаление пользователя")
    public void teardown() {
        userClient.deleteUser(user, token);
        System.out.println("User removed");
    }

    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    @Step("Изменение данных пользователя")
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
    @Step("Изменение данных пользователя")
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
    @Step("Изменение данных пользователя")
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
    @Step("Изменение данных пользователя")
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
    @Step("Изменение данных пользователя")
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
    @Step("Изменение данных пользователя")
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