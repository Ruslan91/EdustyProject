package com.example.Milestone1.Classes;

import java.util.UUID;

public class GroupFeedRead {
    UUID Id;
    Boolean IsMessageOwner;
    public String Message;
    public String Time;
    public UUID UserID;
    public String FirstName;
    public String LastName;
    public String MiddleName;
    public UUID PictureID;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public Boolean getIsMessageOwner() {
        return IsMessageOwner;
    }

    public void setIsMessageOwner(Boolean isMessageOwner) {
        IsMessageOwner = isMessageOwner;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public UUID getPictureID() {
        return PictureID;
    }

    public void setPictureID(UUID pictureID) {
        PictureID = pictureID;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public UUID getUserID() {
        return UserID;
    }

    public void setUserID(UUID userID) {
        this.UserID = userID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
