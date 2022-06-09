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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class ChangeDataUserTest {

    String email = "test_test@olol.ru";
    String password = "12345";
    String name = "Tuta";
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        CreateUser createUser = new CreateUser(email, password, name);
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Editing a user's email")
    public void updateDataUserEmailTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        DataUserUpdate dataUserUpdate = new DataUserUpdate("olol@ololo.com", password, name);
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Editing a user's password")
    public void updateDataUserPasswordTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        DataUserUpdate dataUserUpdate = new DataUserUpdate(email, "Qwerty", name);
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    @DisplayName("Editing a user's name")
    public void updateDataUserNameTest() {
        LoginUser loginUser = new LoginUser(email, password);
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        DataUserUpdate dataUserUpdate = new DataUserUpdate(email, password, "Test Testovich");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);
        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }
}
