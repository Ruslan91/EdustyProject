package com.example.Milestone1.Classes;

import java.util.UUID;

public class EventDelete {
    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    UUID Token;

    public UUID getEventID() {
        return EventID;
    }

    public void setEventID(UUID eventID) {
        EventID = eventID;
    }

    UUID EventID;
}
