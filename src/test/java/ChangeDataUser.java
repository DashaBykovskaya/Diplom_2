import api.UserApi;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.DataUserUpdate;
import model.LoginUser;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class ChangeDataUser {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    public void updateDataUserEmailTest(){
        CreateUser createUser = new CreateUser("olololo@ololo.lolo", "12345", "Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);

        LoginUser loginUser = new LoginUser("olololo@ololo.lolo", "12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());

        DataUserUpdate dataUserUpdate = new DataUserUpdate("ololol@ololo.ru", "12345", "Tuta");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    public void updateDataUserPasswordTest(){
        CreateUser createUser = new CreateUser("olololo@ololo.lolo", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);

        LoginUser loginUser = new LoginUser("olololo@ololo.lolo","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());

        DataUserUpdate dataUserUpdate = new DataUserUpdate("olololo@ololo.lolo", "Qwerty", "Tuta");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    public void updateDataUserNameTest(){
        CreateUser createUser = new CreateUser("olololo@ololo.lolo", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);

        LoginUser loginUser = new LoginUser("olololo@ololo.lolo","12345");
        ValidatableResponse loginUserResponse = UserApi.postLoginUser(loginUser);
        String accessToken = loginUserResponse.assertThat().statusCode(200).extract().path("accessToken");
        assertThat(accessToken, notNullValue());

        DataUserUpdate dataUserUpdate = new DataUserUpdate("olololo@ololo.lolo", "12345", "Test Testovich");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, accessToken);
        updateDataUser.assertThat().statusCode(200);

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }

    @Test
    public void updateDataUnauthorizedUserTest(){
        String expected = "You should be authorised";
        CreateUser createUser = new CreateUser("olololo@ololo.lolo", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        String accessToken = createUserResponse.assertThat().statusCode(200).extract().path("accessToken");

        DataUserUpdate dataUserUpdate = new DataUserUpdate("olololo@ololo.lolo", "12345", "Tuta");
        ValidatableResponse updateDataUser = UserApi.updateDataUser(dataUserUpdate, "");
        updateDataUser.assertThat().statusCode(401).body("message", equalTo(expected));

        ValidatableResponse deleteUserResponse = UserApi.deleteUser(accessToken);
        deleteUserResponse.assertThat().statusCode(202);
    }



}
