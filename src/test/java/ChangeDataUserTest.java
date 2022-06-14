import api.UserApi;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.DataUserUpdate;
import model.LoginUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeDataUserTest {

    String email = "Email@lolo.ru";
    String password = "12345";
    String name = "Tuta";
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        CreateUser createUser = new CreateUser(email, password, name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");
    }

    @After
    public void cleanUp() {
            UserApi.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Editing a user's email")
    public void updateDataUserEmailTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(200);
        DataUserUpdate dataUserUpdate = new DataUserUpdate("Email@lol.com", password, name);
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Editing a user's password")
    public void updateDataUserPasswordTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(200);
        DataUserUpdate dataUserUpdate = new DataUserUpdate(email, "Qwerty", name);
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Editing a user's name")
    public void updateDataUserNameTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        loginUserResponse.assertThat().statusCode(200);
        DataUserUpdate dataUserUpdate = new DataUserUpdate(email, password, "Test Testovich");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Editing the data of an unauthorized user")
    public void updateDataUnauthorizedUserTest() {
        String expected = "You should be authorised";
        DataUserUpdate dataUserUpdate = new DataUserUpdate("email@o.oo", "password", "name");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, "");
        updateDataUser.assertThat().statusCode(401).body("message", equalTo(expected));
    }
}
