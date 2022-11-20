package user;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseClient {
    private final String ROOT = "/auth";
    private final String REGISTER = ROOT + "/register";
    private final String LOGIN = ROOT + "/login";
    private final String USER = ROOT + "/user";

    @Step("Создание уникального пользователя")
    public Response createUniqueUser(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(REGISTER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(User user, String token) {
        return getSpecToken(token)
                .body(user)
                .when()
                .delete(USER);
    }

    @Step("Логин пользователя")
    public Response loginUser(UserCredentials credentials) {
        return getSpec()
                .body(credentials)
                .when()
                .post(LOGIN);
    }

    @Step("Изменение данных пользователя")
    public Response patchUser(User user, String token) {
        return getSpecToken(token)
                .body(user)
                .when()
                .patch(USER);
    }
}