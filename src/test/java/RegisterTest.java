import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class RegisterTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void createUniqueUser() {
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
        sendRegisterRequest(user)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void cannotCreateTwoSameUsers() {
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
        sendRegisterRequest(user);
        sendRegisterRequest(user)
                .then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    public void cannotCreateUserWithEmailOnly() {
        User user = new User();
        user.setEmail("qa_reva@yandex.ru");
        sendRegisterRequest(user)
                .then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    public void cannotCreateUserWithPasswordOnly() {
        User user = new User();
        user.setPassword("qa_reva_pass");
        sendRegisterRequest(user)
                .then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    public void cannotCreateUserWithNameOnly() {
        User user = new User();
        user.setName("qa_reva");
        sendRegisterRequest(user)
                .then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
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
