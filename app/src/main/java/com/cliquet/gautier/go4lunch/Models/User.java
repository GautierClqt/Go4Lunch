package com.cliquet.gautier.go4lunch.Models;

public class User {

    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userUrlPicture;

    public User() {}

    public User(String userId, String userFirstName, String userLastName, String userEmail, String userUrlPicture) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userUrlPicture = userUrlPicture;
    }

    public String getUserId() { return userId; }
    public String getUserFirstName() { return userFirstName; }
    public String getUserLastName() { return userLastName; }
    public String getUserEmail() { return userEmail; }
    public String getUserUrlPicture() { return userUrlPicture; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setUserFirstName(String userFirstName) { this.userFirstName = userFirstName; }
    public void setUserLastName(String userLastName) { this.userLastName = userLastName; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setUserUrlPicture(String userUrlPicture) { this.userUrlPicture = userUrlPicture; }
}
