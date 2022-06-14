import api.OrderApi;
import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.Ingredients;
import model.LoginUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersTest {
    List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
    String email = "Smelova@lol.by";
    String password = "12345";
    String name = "Tuta";
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        CreateUser createUser = new CreateUser(email, password, name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        UserApi.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Receiving an order by an authorized user")
    public void getOrdersAuthorizedUserTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(200);
        Ingredients ingredients = new Ingredients(ingredientsList);
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(200);
        ValidatableResponse getOrdersResponse = OrderApi.getOrders(accessToken);
        getOrdersResponse.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Receiving an order by an unauthorized user")
    public void getOrdersUnauthorizedUserTest() {
        String expected = "You should be authorised";
        Ingredients ingredients = new Ingredients(ingredientsList);
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, "");
        createOrderResponse.assertThat().statusCode(200);
        ValidatableResponse getOrdersResponse = OrderApi.getOrders("");
        getOrdersResponse.assertThat().statusCode(401).body("message", equalTo(expected));
    }
}
