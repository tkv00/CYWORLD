package org.Utility;

public class UserSession {
    private static UserSession instance;
    private String userId;

    private UserSession() {}
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isLoggedIn() {
        return userId != null && !userId.isEmpty();
    }
}
