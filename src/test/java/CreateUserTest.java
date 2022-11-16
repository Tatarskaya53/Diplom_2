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
            System.out.println("User removed");
        } catch (IllegalArgumentException e) {
            System.out.println("User did not create");
        }
    }

    @Test
    @DisplayName("Creating a user")
    public void createUserTest() {
        userClient.createUniqueUser(user)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Creating two identical users")
    public void createIdenticalUserTest() {
        userClient.createUniqueUser(user);
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN)
                .assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));

        //хардкод
    }

    @Test
    @DisplayName("Creating a user with an empty email")
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Creating a user with an empty password")
    public void createUserWithoutPassTest() {
        user.setPassword("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Creating a user with an empty name")
    public void createUserWithoutNameTest() {
        user.setName("");
        userClient.createUniqueUser(user)
                .then().log().all()
                .statusCode(SC_FORBIDDEN);
    }
}
