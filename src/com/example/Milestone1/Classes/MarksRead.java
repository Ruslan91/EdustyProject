package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 13.12.13.
 */
public class MarksRead {
    public UUID Id;
    public Integer Mark;
    public Integer Type;
    public String Description;
    public UUID UserID;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public Integer getMark() {
        return Mark;
    }

    public void setMark(Integer mark) {
        Mark = mark;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        UserID = userID;
    }
}
