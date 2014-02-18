package com.example.Milestone1.Classes;

/**
 * Created by Руслан on 07.11.13.
 */
public class Response<T> {
    T Item;
    Integer StatusCode;

    public T getItem() {
        return Item;
    }

    public void setItem(T item) {
        Item = item;
    }

    public Integer getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(Integer statusCode) {
        StatusCode = statusCode;
    }
}
