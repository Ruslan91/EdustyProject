package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 19.10.13.
 */
public class GroupJoin {
    UUID Token;
    UUID GroupID;

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
}
