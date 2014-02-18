package com.example.Milestone1.Classes;

import java.util.UUID;

/**
 * Created by Руслан on 06.12.13.
 */
public class Journals {
    UUID Id;
    String GroupName;
    String CourseName;
    String OwnerName;
    String Title;
    UUID CourseID;
    UUID GroupID;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
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
