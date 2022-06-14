import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.LoginUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestLoginUser {
    String email = "lggto@olol.lolol";
    String password = "12345";
    String name = "Tuta";
    String accessToken;
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        UserApi.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Login")
    public void loginUserTest(){
        LoginUser loginUser = new LoginUser(email,password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(200);
        assertThat(accessToken, notNullValue());
    }

    @Test
    @DisplayName("Login with out email")
    public void loginUserWrongEmail(){
        String expected = "email or password are incorrect";
        LoginUser loginUser = new LoginUser("111",password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("Login with out password")
    public void loginUserWrongPassword(){
        String expected = "email or password are incorrect";
        LoginUser loginUser = new LoginUser(email,"1234567");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("Login with invalid data: email and password")
    public void loginUserWrongPasswordAndEmail(){
        String expected = "email or password are incorrect";
        LoginUser loginUser = new LoginUser("ololo","1234567");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("Login with empty password")
    public void loginUserEmptyPassword(){
        String expected = "email or password are incorrect";
        LoginUser loginUser = new LoginUser(email,"");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("Login with empty email")
    public void loginUserEmptyEmail(){
        String expected = "email or password are incorrect";
        LoginUser loginUser = new LoginUser("",password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("Login with empty email and password")
    public void loginUserEmptyEmailAndPassword(){
        String expected = "email or password are incorrect";
        LoginUser loginUser = new LoginUser("","");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(401).and().body("message", equalTo(expected));
    }
}
