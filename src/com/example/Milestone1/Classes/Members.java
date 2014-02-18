package com.example.Milestone1.Classes;

import java.util.UUID;

public class Members {
    public UUID Id;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
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
}
