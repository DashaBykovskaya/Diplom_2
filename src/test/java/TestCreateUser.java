import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestCreateUser {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("User creation")
    public void createUserTest(){
        CreateUser createUser = new CreateUser("ololo@ololo.lololo", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Creating a user that already exists")
    public void createUserSimpleEmailTest(){
        String expected = "User already exists";
        CreateUser createUser = new CreateUser("test@test.test", "Qwerty", "test");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("User creation with out email")
    public void createUserWithOutEmail(){
        CreateUser createUser = new CreateUser("", "Qwerty", "test");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String expected = "Email, password and name are required fields";
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("User creation with out password")
    public void createUserWithOutPassword(){
        CreateUser createUser = new CreateUser("ololo@ololo.lololo", "", "test");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String expected = "Email, password and name are required fields";
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("User creation with out name")
    public void createUserWithOutName(){
        CreateUser createUser = new CreateUser("ololo@ololo.lololo", "Qwerty", "");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String expected = "Email, password and name are required fields";
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

}
