import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
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
    String email = "lo@olol.lolol";
    String password = "12345";
    String name = "Tuta";
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Login")
    public void loginUserTest(){
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        LoginUser loginUser = new LoginUser(email,password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Login with out email")
    public void loginUserWrongEmail(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        LoginUser loginUser = new LoginUser("111","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Login with out password")
    public void loginUserWrongPassword(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        LoginUser loginUser = new LoginUser(email,"1234567");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Login with invalid data: email and password")
    public void loginUserWrongPasswordAndEmail(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        LoginUser loginUser = new LoginUser("ololo","1234567");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Login with empty password")
    public void loginUserEmptyPassword(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        LoginUser loginUser = new LoginUser(email,"");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Login with empty email")
    public void loginUserEmptyEmail(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        LoginUser loginUser = new LoginUser("","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Login with empty email and password")
    public void loginUserEmptyEmailAndPassword(){
        String expected = "email or password are incorrect";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        LoginUser loginUser = new LoginUser("","");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }
}
