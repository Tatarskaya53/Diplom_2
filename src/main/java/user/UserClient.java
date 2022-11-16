package user;

import config.BaseClient;
import io.restassured.response.Response;

public class UserClient extends BaseClient {
    private final String ROOT = "/auth";
    private final String REGISTER = ROOT + "/register";
    private final String LOGIN = ROOT + "/login";
    private final String USER = ROOT + "/user";

    public Response createUniqueUser(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(REGISTER);
    }
    public Response deleteUser(User user, String token) {
        return getSpecToken(token)
                .body(user)
                .when()
                .delete(USER);
    }

    public Response loginUser(UserCredentials credentials) {
        return getSpec()
                .body(credentials)
                .when()
                .post(LOGIN);
    }

    public Response patchUser(User user, String token) {
        return getSpecToken(token)
                .body(user)
                .when()
                .patch(USER);
    }
}


