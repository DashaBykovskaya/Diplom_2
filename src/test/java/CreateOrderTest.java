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

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Creation of an order by an authorized user")
    public void createOrderAuthorizedUserTest(){
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
        ValidatableResponse createOrderResponse = orderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(200);

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Creation of an order by an authorized user with invalid ingredient values")
    public void createOrderAuthorizedUserInvalidIngredientsTest(){
        List<String> ingredientsList = List.of("123", "456");

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
        ValidatableResponse createOrderResponse = orderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(500);

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Creation of an order by an authorized user without ingredients")
    public void createOrderAuthorizedUserWithOutIngredientsTest(){
        List<String> ingredientsList = List.of();
        String expected = "Ingredient ids must be provided";

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
        ValidatableResponse createOrderResponse = orderApi.postCreateOrder(ingredients, accessToken);
        createOrderResponse.assertThat().statusCode(400).body("message", equalTo(expected));

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Creation of an order by an unauthorized user")
    public void createOrderUnauthorizedUserTest(){
        List<String> ingredientsList = List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa71");
        //String expected = "You should be authorised";

        Ingredients ingredients = new Ingredients(ingredientsList);
        OrderApi orderApi = new OrderApi();
        ValidatableResponse createOrderResponse = orderApi.postCreateOrder(ingredients, "");
        createOrderResponse.assertThat().statusCode(200);
    }


}
