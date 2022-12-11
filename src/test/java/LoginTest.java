import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void loginAsExistingUser() {
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
        Requests.sendRegisterRequest(user);
        Response response = Requests.sendLoginRequest(user);

        response.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        String accessToken = response.body().as(LoggedInUser.class).getAccessToken();
        Requests.sendDeleteRequest(accessToken);
    }

    @Test
    public void loginWithIncorrectData() {
        User user = new User("qa_reva1@yandex.ru", "qa_reva_pass1");
        Requests.sendLoginRequest(user)
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}
