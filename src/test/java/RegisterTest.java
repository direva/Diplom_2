import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegisterTest {
    private static User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    public void createUniqueUser() {
        Requests.sendRegisterRequest(user)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void cannotCreateTwoSameUsers() {
        Requests.sendRegisterRequest(user);
        Requests.sendRegisterRequest(user)
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
        Requests.sendRegisterRequest(user)
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
        Requests.sendRegisterRequest(user)
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
        Requests.sendRegisterRequest(user)
                .then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @AfterClass
    public static void tearDown() {
        String accessToken = Requests.sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
        Requests.sendDeleteRequest(accessToken);
    }
}
