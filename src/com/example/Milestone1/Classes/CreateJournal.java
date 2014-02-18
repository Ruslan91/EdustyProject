package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class CreateJournal {
    UUID Token;
    String Title;
    UUID CourseID;
    UUID GroupID;

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

    public UUID getCourseID() {
        return CourseID;
    }

    public void setCourseID(UUID courseID) {
        CourseID = courseID;
    }

    public UUID getGroupID() {
        return GroupID;
    }

    public void setGroupID(UUID groupID) {
        GroupID = groupID;
    }
}
