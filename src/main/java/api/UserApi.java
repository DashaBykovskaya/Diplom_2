package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserApi {

    @Step("User create")
    public static ValidatableResponse postCreateUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .and().body(body)
                .when().post("/api/auth/register/")
                .then();
    }
}
