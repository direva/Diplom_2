import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class Requests {
    public static Response sendRegisterRequest(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    public static Response sendAuthUpdateRequest(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    public static Response sendNotAuthUpdateRequest(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch("/api/auth/user");
    }

    public static Response sendLoginRequest(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    public static Response sendDeleteRequest(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .delete("/api/auth/user");
    }

    public static Response sendAuthCreateOrderRequest(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .and()
                .body(order)
                .when()
                .post("/api/orders");
    }

    public static Response sendNotAuthCreateOrderRequest(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/orders");
    }

    public static Response sendAuthGetOrdersRequest(String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .get("/api/orders");
    }

    public static Response sendNotAuthGetOrdersRequest() {
        return given()
                .get("/api/orders");
    }
}
