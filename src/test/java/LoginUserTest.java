import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {
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
    @DisplayName("Логин пользователя")
    @Step("Логин под существующим пользователем")
    public void validLoginUserTest() {
        UserCredentials credentials = UserCredentials.from(user);
        userClient.loginUser(credentials)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Логин пользователя")
    @Step("Логин  с неверным логином и паролем")
    public void invalidLoginUserTest() {
        UserCredentials credentials = UserCredentials.from(user);
        credentials.setEmail("wrong");
        credentials.setPassword("wrong");
        userClient.loginUser(credentials)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }
}