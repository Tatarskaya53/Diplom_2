import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import order.Order;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    private String token;
    private List<String> data;
    User user;
    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();

    @Before
    @Step("Созданиие рандомного пользователя, получение списка ингредиентов")
    public void setup() {
        user = User.createRandomUser();
        token = userClient.createUniqueUser(user)
                .then()
                .extract().path("accessToken");
        System.out.println("User created");
        data = orderClient.getIngredients()
                .then().log().all()
                .extract().path("data._id");
    }

    @After
    @Step("Удаление пользователя")
    public void teardown() {
        userClient.deleteUser(user, token);
        System.out.println("User removed");
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Step("Создание заказа")
    public void createOrderWithAuthorizationTest() {
        Order order = new Order(data);
        orderClient.createOrder(order, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Step("Создание заказа")
    public void createOrderWithoutAuthorizationTest() {
        Order order = new Order(data);
        token = "";
        orderClient.createOrder(order, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Step("Создание заказа")
    public void createOrderWithIngredientsTest() {
        Order order = new Order(data);
        orderClient.createOrder(order, token)
                .then().log().all()
                .statusCode(SC_OK)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Step("Создание заказа")
    public void createOrderWithoutIngredientsTest() {
        data = List.of();
        Order order = new Order(data);
        orderClient.createOrder(order, token)
                .then().log().all()
                .statusCode(SC_BAD_REQUEST)
                .assertThat()
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Step("Создание заказа")
    public void createOrderWithInvalidHashTest() {
        data = List.of("invalidHash", "invalidHash");
        Order order = new Order(data);
        orderClient.createOrder(order, token)
                .then().log().all()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}