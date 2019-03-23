package com.example.olivier.businessapp.Objects;

public class User {

    private String UserID;
    private String DisplayName;
    private String email;
    private String profile;
    private String age;
    private String profession;
    private String JobPosition;
    private String Interest1;
    private String Interest2;
    private String Interest3;
    private String Interest4;
    private String Interest5;
    public User() {
    }

    public User(String userId, String displayName, String email) {
        this.DisplayName = displayName;
        this.UserID = userId;
        this.email = email;
    }

    public User(String userId, String displayName, String email,String age,String profile,String profession, String JobPosition, String Interest1,String Interest2,String Interest13,String Interest4,String Interest5 ) {
        this.DisplayName = displayName;
        this.UserID = userId;
        this.email = email;
        this.age=age;
        this.profile=profile;
        this.profession=profession;
        this.JobPosition=JobPosition;
        this.Interest1=Interest1;
        this.Interest2=Interest2;
        this.Interest3=Interest13;
        this.Interest4=Interest4;
        this.Interest5=Interest5;
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
    public String getProfile() {
        return profile;
    }
    public void setProfile(String image) {
        this.profile = image;
    }
    public String getAge() {
        return age;
    }
    public String getInterest1() {
        return Interest1;
    }
    public String getInterest2() {
        return Interest2;
    }
    public String getInterest3() {
        return Interest3;
    }
    public String getInterest4() {
        return Interest4;
    }
    public String getInterest5() {
        return Interest5;
    }
    public String getJobPosition() {
        return JobPosition;
    }
    public String getProfession() {
        return profession;
    }
    public String getUserID() {
        return UserID;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public void setInterest1(String interest1) {
        Interest1 = interest1;
    }
    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }
    public void setInterest2(String interest2) {
        Interest2 = interest2;
    }
    public void setInterest3(String interest3) {
        Interest3 = interest3;
    }
    public void setInterest4(String interest4) {
        Interest4 = interest4;
    }
    public void setInterest5(String interest5) {
        Interest5 = interest5;
    }
    public void setJobPosition(String jobPosition) {
        JobPosition = jobPosition;
    }
    public void setProfession(String profession) {
        this.profession = profession;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }
}
