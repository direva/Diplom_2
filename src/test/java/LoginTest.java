import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
        sendRegisterRequest(user).then().assertThat().statusCode(200);
    }

    @Test
    public void loginAsExistingUser() {
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass");
        sendLoginRequest(user)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void loginWithIncorrectLogin() {
        User user = new User("qa_reva1@yandex.ru", "qa_reva_pass");
        sendLoginRequest(user)
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void loginWithIncorrectPassword() {
        User user = new User("qa_reva@yandex.ru", "qa_reva1_pass");
        sendLoginRequest(user)
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @After
    public void tearDown() {
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass");
        String accessToken = sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
        if(accessToken != null)
            sendDeleteRequest(accessToken.replace("Bearer ", ""));
    }

    private Response sendRegisterRequest(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    private Response sendLoginRequest(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    private Response sendDeleteRequest(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/auth/user");
    }
}
