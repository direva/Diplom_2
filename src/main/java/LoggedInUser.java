public class LoggedInUser {
    private boolean success;
    private String accessToken;
    private String refreshToken;

    public LoggedInUser() {}

    public LoggedInUser(boolean success, String accessToken, String refreshToken) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
