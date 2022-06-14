import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class TestCreateUser {

    String email = "Email@lol.ru";
    String password = "12345";
    String name = "Tuta";
    String accessToken;
    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @After
    public void cleanUp() {
        if(accessToken != null) {
            UserApi.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("User creation")
    public void createUserTest(){
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
    }

    @Test
    @DisplayName("Creating a user that already exists")
    public void createUserSimpleEmailTest(){
        String expected = "User already exists";
        CreateUser createUser = new CreateUser(email, password,name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        CreateUser createUserSimple = new CreateUser(email, password,name);
        ValidatableResponse createUserResponseSimple = UserApi.postCreateUser(createUserSimple);
        createUserResponseSimple.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("User creation with out email")
    public void createUserWithOutEmail(){
        CreateUser createUser = new CreateUser("", "Qwerty", "test");
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String expected = "Email, password and name are required fields";
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("User creation with out password")
    public void createUserWithOutPassword(){
        CreateUser createUser = new CreateUser("ololo@ololo.lololo", "", "test");
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String expected = "Email, password and name are required fields";
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

    @Test
    @DisplayName("User creation with out name")
    public void createUserWithOutName(){
        CreateUser createUser = new CreateUser("ololo@ololo.lololo", "Qwerty", "");
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String expected = "Email, password and name are required fields";
        createUserResponse.assertThat().statusCode(403).and().body("message", equalTo(expected));
    }

}
