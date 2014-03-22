package com.example.Milestone1.Classes;

import android.graphics.Bitmap;

import java.util.UUID;

public class File {
    String fileName;
    UUID Token;
    String containerName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UUID getToken() {
        return Token;
    }

    public void setToken(UUID token) {
        Token = token;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
}
