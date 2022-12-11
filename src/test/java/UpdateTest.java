import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateTest {
    private static User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
    private static String accessToken;

    @BeforeClass
    public static void createUser() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Requests.sendRegisterRequest(user);
        accessToken = Requests.sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
    }

    @Test
    public void updateNameWithAuthorization() {
        User userPatch = new User();
        userPatch.setName("qa_reva1");
        Requests.sendAuthUpdateRequest(userPatch, accessToken)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void updatePasswordWithAuthorization() {
        User userPatch = new User();
        userPatch.setPassword("qa_reva_pass");
        Requests.sendAuthUpdateRequest(userPatch, accessToken)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void updateEmailWithAuthorization() {
        User userPatch = new User();
        userPatch.setEmail("qa_reva@yandex.ru");
        Requests.sendAuthUpdateRequest(userPatch, accessToken)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void updateNameWithoutAuthorization() {
        User userPatch = new User();
        userPatch.setName("qa_reva1");
        Requests.sendNotAuthUpdateRequest(userPatch)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void updatePasswordWithoutAuthorization() {
        User userPatch = new User();
        userPatch.setPassword("qa_reva_pass");
        Requests.sendNotAuthUpdateRequest(userPatch)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    public void updateEmailWithoutAuthorization() {
        User userPatch = new User();
        userPatch.setEmail("qa_reva@yandex.ru");
        Requests.sendNotAuthUpdateRequest(userPatch)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @AfterClass
    public static void tearDown() {
        Requests.sendDeleteRequest(accessToken);
    }
}
