package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 17.07.13.
 */
public class Event {//implements Comparable<Event>{
    UUID EventID;
    String Description;
    String StartTime;
    String EndDate;
    String EndTime;
    String Location;
    Integer TimeInterval;
    String Title;
    Boolean IsReadWrite;
    UUID GroupID;
    String GroupName;

    /*public Event(String TimeStart) {
        StartTime = TimeStart;
    }

    public int compareTo(Event event) {
        return StartTime.compareTo(event.StartTime);
    }*/


    public Boolean getIsReadWrite() {
        return IsReadWrite;
    }

    public void setIsReadWrite(Boolean isReadWrite) {
        IsReadWrite = isReadWrite;
    }

    public UUID getGroupID() {
        return GroupID;
    }

    public void setGroupID(UUID groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    UUID Token;


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Integer getTimeInterval() {

        return TimeInterval;
    }

    public void setTimeInterval(Integer timeInterval) {
        TimeInterval = timeInterval;
    }

    public String getStartTime() {

        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getLocation() {

        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public UUID getEventID() {

        return EventID;
    }

    public void setEventID(UUID eventID) {
        EventID = eventID;
    }

    public String getEndTime() {

        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getEndDate() {

        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getDescription() {

        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
