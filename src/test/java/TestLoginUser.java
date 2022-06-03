import api.UserApi;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.LoginUser;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestLoginUser {
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void loginUserTest(){
        CreateUser createUser = new CreateUser("7ololo@olol.lol", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("7ololo@olol.lol","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String token = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(token, notNullValue());
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(token);
        deleteUserResponse.assertThat().statusCode(202);

    }

    @Test
    public void loginUserWrongEmail(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser("ololo@lol.lol21", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("111","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    public void loginUserWrongPassword(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser("ololo@lol.lol11", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("ololo@lol.lol11","1234567");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    public void loginUserWrongPasswordAndEmail(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser("ololo@lol.lol16", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("ololo","1234567");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    public void loginUserEmptyPassword(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser("ololo@lol.lol12", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("ololo@lol.lol12","");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    public void loginUserEmptyEmail(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser("ololo@lol.lol14", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    public void loginUserEmptyEmailAndPassword(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser("ololo@lol.lol17", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser("","");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }
}
