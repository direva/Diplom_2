import io.restassured.RestAssured;
import org.junit.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersTest {
    private static String[] ingredients = {"61c0c5a71d1f82001bdaaa6d"};
    private static Order order = new Order(ingredients);
    private static User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Requests.sendRegisterRequest(user);
        accessToken = Requests.sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
        Requests.sendAuthCreateOrderRequest(order, accessToken);
    }

    @Test
    public void validateAuthUserOrders() {
        Requests.sendAuthGetOrdersRequest(accessToken)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void validateNotAuthUserOrders() {
        Requests.sendNotAuthGetOrdersRequest()
                .then().assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @AfterClass
    public static void tearDown() {
        Requests.sendDeleteRequest(accessToken);
    }
}
