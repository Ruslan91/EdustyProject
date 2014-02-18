package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class CreateCourse {
    public UUID Token;
    public String Title;

    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
