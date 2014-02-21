package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 21.02.14.
 */
public class SearchResult {
    UUID Id;
    String __type;

    String FirstName;
    String LastName;
    String City;
    String Country;

    String PictureID;

    String Name;
    String Description;
    String WebSite;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        this.Id = id;
    }

    public String get__type() {
        return __type;
    }

    public void set__type(String __type) {
        this.__type = __type;
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

    public String getPictureID() {
        return PictureID;
    }

    public void setPictureID(String pictureID) {
        PictureID = pictureID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getWebSite() {
        return WebSite;
    }

    public void setWebSite(String webSite) {
        WebSite = webSite;
    }
}

