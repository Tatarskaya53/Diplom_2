import io.qameta.allure.junit4.DisplayName;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;

public class ReceivingUserOrdersTest {

    private String token;
    User user;
    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();

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
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getAuthorizedUserOrdersTest() {
        orderClient.getOrders(token)
                .then().log().all()
                .statusCode(SC_OK);

    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getNonAuthorizedUserOrdersTest() {
        orderClient.getOrders("")
                .then().log().all()
                .statusCode(SC_UNAUTHORIZED);
    }
}