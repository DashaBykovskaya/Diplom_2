package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.Ingredients;

import static io.restassured.RestAssured.given;

public class OrderApi {

    @Step("Create order")
    public static ValidatableResponse postCreateOrder(Ingredients ingredients, String accessToken){
        return  given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then();
    }

    @Step("Get orders")
    public static ValidatableResponse getOrders(String accessToken) {
        return  given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders/")
                .then();
    }
}
