package com.example.Milestone1.Classes;

import java.util.UUID;

public class User {
    public String FirstName;
    public String LastName;
    public String MiddleName;
    public String BirthDate;
    public String EMail;
    public String Phone;
    public String Country;
    public String City;
    public Boolean IsFollowing;
    public UUID PictureID;

    public UUID getPictureID() {
        return PictureID;
    }

    public void setPictureID(UUID pictureID) {
        PictureID = pictureID;
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

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public Boolean getFollowing() {
        return IsFollowing;
    }

    public void setFollowing(Boolean following) {
        IsFollowing = following;
    }
}
