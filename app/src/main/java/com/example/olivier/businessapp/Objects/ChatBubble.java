package com.example.olivier.businessapp.Objects;

public class ChatBubble {

    private String content;
    private boolean myMessage;

    public ChatBubble(String content, Boolean myMessage) {
        this.content = content;
        this.myMessage = myMessage;
    }

    public String getContent() {
        return content;
    }

    public boolean myMessage() {
        return myMessage;
    }
}
