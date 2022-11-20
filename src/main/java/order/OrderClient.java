package order;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderClient extends BaseClient {
    private final String ORDER = "/orders";
    private final String INGREDIENTS = "/ingredients";

    @Step("Создание заказа")
    public Response createOrder(Order order, String token) {
        return getSpecToken(token)
                .body(order)
                .when()
                .post(ORDER);
    }

    @Step("Получение заказа")
    public Response getOrders(String token) {
        return getSpecToken(token)
                .get(ORDER);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return getSpec()
                .get(INGREDIENTS);
    }
}