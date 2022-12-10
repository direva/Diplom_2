import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
        sendRegisterRequest(user).then().assertThat().statusCode(200);
    }

    @Test
    public void updateNameWithAuthorization() {
        User userPatch = new User();
        userPatch.setName("qa_reva1");
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass");
        String accessToken = sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
        sendUpdateRequest(userPatch, accessToken.replace("Bearer ", ""))
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void updatePasswordWithAuthorization() {
        User userPatch = new User();
        userPatch.setPassword("qa_reva_pass");
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass");
        String accessToken = sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
        sendUpdateRequest(userPatch, accessToken.replace("Bearer ", ""))
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void updateEmailWithAuthorization() {
        User userPatch = new User();
        userPatch.setEmail("qa_reva@yandex.ru");
        User user = new User("qa_reva@yandex.ru", "qa_reva_pass");
        String accessToken = sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
        sendUpdateRequest(userPatch, accessToken.replace("Bearer ", ""))
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void updateNameWithoutAuthorization() {
        User userPatch = new User();
        userPatch.setName("qa_reva1");
        sendUpdateRequest(userPatch, "")
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void updatePasswordWithoutAuthorization() {
        User userPatch = new User();
        userPatch.setPassword("qa_reva_pass");
        sendUpdateRequest(userPatch, "")
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void updateEmailWithoutAuthorization() {
        User userPatch = new User();
        userPatch.setEmail("qa_reva@yandex.ru");
        sendUpdateRequest(userPatch, "")
                .then().assertThat().body("success", equalTo(false))
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

    private Response sendUpdateRequest(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user");
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
