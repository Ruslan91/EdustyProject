package com.example.Milestone1.Classes;

import java.util.UUID;

public class Groups {
    public UUID Id;
    public Integer GroupLevel;
    public String Name;
    public Integer Type;
    public UUID Parent;
    public Integer Count;
    public String Description;
    public String WebSite;
    public UUID PictureID;
    public Boolean IsMember;
    public Boolean Free;
    public Boolean IsOwner;

    public Boolean getIsOwner() {
        return IsOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        IsOwner = isOwner;
    }

    public Boolean getIsMember() {
        return IsMember;
    }

    public void setIsMember(Boolean isMember) {
        IsMember = isMember;
    }

    public Boolean getFree() {
        return Free;
    }

    public void setFree(Boolean free) {
        Free = free;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public Integer getGroupLevel() {
        return GroupLevel;
    }

    public void setGroupLevel(Integer groupLevel) {
        GroupLevel = groupLevel;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public UUID getParent() {
        return Parent;
    }

    public void setParent(UUID parent) {
        Parent = parent;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
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

    public UUID getPictureID() {
        return PictureID;
    }

    public void setPictureID(UUID pictureID) {
        PictureID = pictureID;
    }

    public Boolean getMember() {
        return IsMember;
    }

    public void setMember(Boolean member) {
        IsMember = member;
    }
}
