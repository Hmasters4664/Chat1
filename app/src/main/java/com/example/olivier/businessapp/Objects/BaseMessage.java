package com.example.olivier.businessapp.Objects;

public class BaseMessage {

    private String messageText;
    private String file_url;
    private String sender,displayname;
    private boolean myMessage;
    private boolean hasfile;

    public BaseMessage()
    {

    }

    public BaseMessage(String messageText, String sender, String file_url, boolean hasfile, String display){
        this.messageText=messageText;
        this.file_url=file_url;
        this.sender=sender;
        //this.myMessage=myMessage;
        this.hasfile=hasfile;
        this.displayname=display;
    }
    public BaseMessage(String messageText, String sender, String file_url,boolean myMessage , boolean hasfile, String display){
        this.messageText=messageText;
        this.file_url=file_url;
        this.sender=sender;
        this.myMessage=myMessage;
        this.hasfile=hasfile;
        this.displayname=display;
    }

    public String getFile_url() {
        return file_url;
    }

    public String getDisplayname(){return displayname;}

    public boolean getHasfile() { return hasfile;}

    public void setHasfile(boolean hasfile){this.hasfile=hasfile;}

    public void setMyMessage(boolean myMessage) {
        this.myMessage = myMessage;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSender() {
        return sender;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setDisplayname(String display){this.displayname=display;}

    public boolean isMyMessage() {
        return myMessage;
    }
}

