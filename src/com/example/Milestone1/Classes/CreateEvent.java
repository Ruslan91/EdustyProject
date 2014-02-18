package com.example.Milestone1.Classes;

import java.util.UUID;

public class CreateEvent {
    UUID Token;
    UUID GroupID;
    String Description;
    String EndDate;
    String EndTime;
    String Location;
    String StartTime;
    Integer TimeInterval;
    String Title;

    public UUID getGroupID() {
        return GroupID;
    }

    public void setGroupID(UUID groupID) {
        GroupID = groupID;
    }

    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public Integer getTimeInterval() {
        return TimeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        TimeInterval = timeInterval;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
