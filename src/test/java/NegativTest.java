import api.OrderApi;
import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.DataUserUpdate;
import model.Ingredients;
import model.LoginUser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class NegativTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Receiving an order by an unauthorized user")
    public void getOrdersUnauthorizedUserTest() {
        List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
        String expected = "You should be authorised";
        Ingredients ingredients = new Ingredients(ingredientsList);
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, "");
        createOrderResponse.assertThat().statusCode(200);
        ValidatableResponse getOrdersResponse = OrderApi.getOrders("");
        getOrdersResponse.assertThat().statusCode(401).body("message", equalTo(expected));
    }

    @Test
    @DisplayName("Creation of an order by an authorized user without ingredients")
    public void createOrderAuthorizedUserWithOutIngredientsTest(){
        List<String> ingredientsList = List.of();
        String expected = "Ingredient ids must be provided";
        CreateUser createUser = new CreateUser("ci@cc.cc", "123456","Tri");
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("ci@cc.cc","123456");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        Ingredients ingredients = new Ingredients(ingredientsList);
        OrderApi orderApi = new OrderApi();
        ValidatableResponse createOrderResponse = orderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(400).body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }
}
