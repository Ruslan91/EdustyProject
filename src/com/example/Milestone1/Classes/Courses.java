package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class Courses {
    UUID Id;
    String Title;
    Boolean IsOwner;

    public Boolean getIsOwner() {
        return IsOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        IsOwner = isOwner;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        this.Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
