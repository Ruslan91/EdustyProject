package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 13.12.13.
 */
public class EditMark {
    public MarksRead[] getItem() {
        return Item;
    }

    public void setItem(MarksRead[] item) {
        Item = item;
    }

    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    public Integer getLessonNumber() {
        return LessonNumber;
    }

    public void setLessonNumber(Integer lessonNumber) {
        LessonNumber = lessonNumber;
    }

    public UUID getJournalID() {
        return JournalID;
    }

    public void setJournalID(UUID journalID) {
        JournalID = journalID;
    }

    MarksRead[] Item;
    UUID Token;
    Integer LessonNumber;
    UUID JournalID;
}
