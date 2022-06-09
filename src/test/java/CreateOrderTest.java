import api.OrderApi;
import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import model.CreateUser;
import model.Ingredients;
import model.LoginUser;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class CreateOrderTest {

    String email = "b@b.b";
    String password = "12345";
    String name = "Tuta";
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Creation of an order by an authorized user")
    public void createOrderAuthorizedUserTest(){
        List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
        LoginUser loginUser = new LoginUser(email,password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        Ingredients ingredients = new Ingredients(ingredientsList);
        OrderApi orderApi = new OrderApi();
        ValidatableResponse createOrderResponse = orderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(200);
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Creation of an order by an authorized user with invalid ingredient values")
    public void createOrderAuthorizedUserInvalidIngredientsTest(){
        List<String> ingredientsList = List.of("61c0c5a71d1f8200", "61c0c5a71d");
        LoginUser loginUser = new LoginUser(email,password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        Ingredients ingredients = new Ingredients(ingredientsList);
        ValidatableResponse createOrderResponse = OrderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(500);
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }
}
