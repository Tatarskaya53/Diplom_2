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
        System.out.println("User created");
    }

    @After
    public void teardown() {
        userClient.deleteUser(user, token);
        System.out.println("User removed");
    }

    @Test
    public void changeUserEmailWithAuthorizationTest(){
        user.setEmail("patchedEmail");
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    public void changeUserPasswordWithAuthorizationTest(){
        user.setPassword("patchedPass");
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    public void changeUserNameWithAuthorizationTest(){
        user.setName("patchedName");
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    public void changeUserEmailWithoutAuthorizationTest(){
        user.setEmail("patchedEmail");
        token = "";
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    public void changeUserPasswordWithoutAuthorizationTest(){
        user.setPassword("patchedPass");
        token = "";
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    public void changeUserNameWithoutAuthorizationTest(){
        user.setName("patchedName");
        token = "";
        userClient.patchUser(user, token)
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false));
    }

}
