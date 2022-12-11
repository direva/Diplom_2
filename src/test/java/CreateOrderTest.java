import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    private static String[] ingredients = {"61c0c5a71d1f82001bdaaa6d"};
    private static String[] incorrectIngredients = {"60d3b41abdacab0026a7"};
    private static User user = new User("qa_reva@yandex.ru", "qa_reva_pass", "qa_reva");
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Requests.sendRegisterRequest(user);
        accessToken = Requests.sendLoginRequest(user).body().as(LoggedInUser.class).getAccessToken();
    }

    @Test
    public void createOrderWithoutAuthorization() {
        Order order = new Order(ingredients);
        Requests.sendNotAuthCreateOrderRequest(order)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void createOrderWithAuthorization() {
        Order order = new Order(ingredients);
        Requests.sendAuthCreateOrderRequest(order, accessToken)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    public void createOrderWithoutIngredients() {
        Order order = new Order();
        Requests.sendNotAuthCreateOrderRequest(order)
                .then().assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test
    public void createOrderWithIncorrectIngredientsHash() {
        Order order = new Order(incorrectIngredients);
        Requests.sendNotAuthCreateOrderRequest(order).then().statusCode(500);
    }

    @AfterClass
    public static void tearDown() {
        Requests.sendDeleteRequest(accessToken);
    }
}
