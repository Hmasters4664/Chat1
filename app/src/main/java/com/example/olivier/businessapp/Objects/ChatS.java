package com.example.olivier.businessapp.Objects;

public class ChatS {
    private String user1;
    private String user2;
    private String ChatID;

    public ChatS() {

    }

    public ChatS(String u1, String u2, String ChatID) {
        this.user1 = u1;
        this.user2 = u2;
        this.ChatID = ChatID;
    }

    public String getUser1() {
        return this.user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return this.user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getChatID() {
        return this.ChatID;
    }

    public void setChatID(String ChatID) {
        this.ChatID = ChatID;
    }
}
