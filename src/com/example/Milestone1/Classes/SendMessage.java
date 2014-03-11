package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 18.10.13.
 */
public class SendMessage {
    public UUID Token;
    public UUID GroupID;
    public String Message;

    public UUID getGroupID() {
        return GroupID;
    }

    public void setGroupID(UUID groupID) {
        GroupID = groupID;
    }

    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
