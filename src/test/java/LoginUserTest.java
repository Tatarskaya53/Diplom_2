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
    @DisplayName("Логин пользователя")
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