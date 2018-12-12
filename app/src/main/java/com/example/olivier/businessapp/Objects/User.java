package com.example.olivier.businessapp.Objects;

public class User {

    private String UserID;
    private String DisplayName;
    private String email;
    private String image;

    public User() {
    }

    public User(String userId, String displayName, String email) {
        this.DisplayName = displayName;
        this.UserID = userId;
        this.email = email;
    }

    public String getUserId() {
        return UserID;
    }



    public String getDisplayName() {
        return DisplayName;
    }


    public String getEmail() {
        return email;
    }
    public String getLast()
    {
        return "oops";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setdisplayName(String displayName) {
        this.DisplayName = displayName;
    }

    public void setUserId(String userId) {
        this.UserID = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
