import api.UserApi;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class TestCreateUser {

    @Before
    public void setUp(){
        CreateUser createUser = new CreateUser("test@test.test", "Qwerty", "test");
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Response userId =
                given().header("Content-type", "application/json").and().body(createUser).when().post("/api/auth/register");
    }

    @Test
    public void createUserTest(){
        CreateUser createUser = new CreateUser("ololo@lol.lo", "12345","Tuta");
        UserApi userApi = new UserApi();
        ValidatableResponse createUserResponse = UserApi.postCreateUser(createUser);
        createUserResponse.assertThat().statusCode(200);

    }

}
