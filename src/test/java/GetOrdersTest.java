import api.OrderApi;
import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.Ingredients;
import model.LoginUser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetOrdersTest {
    List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
    String email = "b@b.bb";
    String password = "12345";
    String name = "Tuta";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        CreateUser createUser = new CreateUser(email, password, name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Receiving an order by an authorized user")
    public void getOrdersAuthorizedUserTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        Ingredients ingredients = new Ingredients(ingredientsList);
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(200);
        ValidatableResponse getOrdersResponse = OrderApi.getOrders(accessToken);
        getOrdersResponse.assertThat().statusCode(200);
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }
}
