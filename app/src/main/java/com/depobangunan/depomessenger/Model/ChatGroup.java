package com.depobangunan.depomessenger.Model;

public class ChatGroup {
    private String sender, message, username;

    public ChatGroup(String sender, String message, String username) {
        this.sender = sender;
        this.message = message;
        this.username = username;
    }

    public ChatGroup() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
