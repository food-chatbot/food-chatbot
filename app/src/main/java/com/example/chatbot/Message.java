package com.example.chatbot;

public class Message {
    private String message;
    private boolean isReceived;
    long timestamp;
    public Message(String message, boolean isReceived){
        this.message = message;
        this.isReceived = isReceived;
    }

    public String getMessage(){return message;}

    public void setMessage(String message) {this.message = message;}

    public boolean getIsReceived() {return isReceived;}

    public void setIsReceived(boolean isReceived) {this.isReceived = isReceived;}

    public long getTimeStamp() {
        return timestamp;
    }
}
