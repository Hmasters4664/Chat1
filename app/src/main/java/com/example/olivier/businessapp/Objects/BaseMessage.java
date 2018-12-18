package com.example.olivier.businessapp.Objects;

import java.text.SimpleDateFormat;

public class BaseMessage {

    private String messageText;
    private String file_url;
    private String sender,displayname,date;
    private boolean myMessage;
    private boolean hasfile;
    private long time;

    public BaseMessage()
    {

    }

    public BaseMessage(String messageText, String sender, String file_url, boolean hasfile, String display,String date, long time){
        this.messageText=messageText;
        this.file_url=file_url;
        this.sender=sender;
        //this.myMessage=myMessage;
        this.hasfile=hasfile;
        this.displayname=display;
        this.date=date;
        this.time=time;
    }
    public BaseMessage(String messageText, String sender, String file_url,boolean myMessage , boolean hasfile, String display, String date, long time){
        this.messageText=messageText;
        this.file_url=file_url;
        this.sender=sender;
        this.myMessage=myMessage;
        this.hasfile=hasfile;
        this.displayname=display;
        this.date=date;
        this.time=time;
    }

    public String getFile_url() {
        return file_url;
    }

    public String getDisplayname(){return displayname;}

    public boolean getHasfile() { return hasfile;}

    public long getTime() {
        return time;
    }

    public String getDate(){return date;}

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

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

