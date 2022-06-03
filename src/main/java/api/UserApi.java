package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
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

    @Step("User login")
    public static ValidatableResponse postLoginUser(Object body){
        return given()
                .header("Content-type", "application/json")
                .and().body(body)
                .when().post("/api/auth/login/")
                .then();
    }

    @Step("Delete user")
    public static ValidatableResponse deleteUser(String token){
        return given()
                .header("Content-type", "application/json")
                .and().body(token)
                .when().delete("api/auth/user/")
                .then();
    }
}
