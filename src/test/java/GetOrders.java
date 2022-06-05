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

public class GetOrders {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    public void getOrdersAuthorizedUserTest() {
        List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");

        CreateUser createUser = new CreateUser("ololo@ololo.lololo", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);

        LoginUser loginUser = new LoginUser("ololo@ololo.lololo","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());

        Ingredients ingredients = new Ingredients(ingredientsList);
        OrderApi orderApi = new OrderApi();
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(200);

        ValidatableResponse getOrdersResponse = OrderApi.getOrders(accessToken);
        getOrdersResponse.assertThat().statusCode(200);

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    public void getOrdersUnauthorizedUserTest() {
        List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
        String expected = "You should be authorised";

        Ingredients ingredients = new Ingredients(ingredientsList);
        OrderApi orderApi = new OrderApi();
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, "");
        createOrderResponse.assertThat().statusCode(200);

        ValidatableResponse getOrdersResponse = OrderApi.getOrders("");
        getOrdersResponse.assertThat().statusCode(401).body("message", equalTo(expected));
    }
}
