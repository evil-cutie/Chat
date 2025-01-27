package org.example.chat;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {

    private String username;
    private String message;
    private boolean online;
    private Message messageType;
    private ArrayList<String> onlineUsers;

    enum Message {
        SYSTEM,
        USER
    }

    public UserData(String username, String message, boolean isOnline, Message messageType, ArrayList<String> onlineUsers) {
        this.username = username;
        this.message = message;
        this.online = isOnline;
        this.messageType = messageType;
        this.onlineUsers = onlineUsers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean status) {
        this.online = status;
    }

    public Message getMessageType() {
        return messageType;
    }

    public void setMessageType(Message messageType) {
        this.messageType = messageType;
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(ArrayList<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}
