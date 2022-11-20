package config;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class BaseClient {
    protected RequestSpecification getSpec() {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL);
    }

    protected RequestSpecification getSpecToken(String token) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization",token)
                .baseUri(Config.BASE_URL);
    }
}