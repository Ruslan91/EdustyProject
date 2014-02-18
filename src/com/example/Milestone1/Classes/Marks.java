package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 07.12.13.
 */
public class Marks {
    String UserName;
    UUID Id;
    UUID JournalID;
    Integer Mark;
    Integer Type;
    String Description;
    UUID UserID;
    String Date;
    Integer LessonNumber;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Integer getLessonNumber() {
        return LessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        LessonNumber = lessonNumber;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public UUID getJournalID() {
        return JournalID;
    }

    public void setJournalID(UUID journalID) {
        JournalID = journalID;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
