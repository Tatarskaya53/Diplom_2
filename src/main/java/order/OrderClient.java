package order;

import config.BaseClient;
import io.restassured.response.Response;

public class OrderClient extends BaseClient {
    private final String ORDER = "/orders";
    private final String INGREDIENTS = "/ingredients";
    public Response createOrder(Order order, String token) {
        return getSpecToken(token)
                .body(order)
                .when()
                .post(ORDER);
    }

    public Response getOrders(String token) {
        return getSpecToken(token)
                .get(ORDER);
    }

    public Response getIngredients() {
        return getSpec()
                .get(INGREDIENTS);
    }
}