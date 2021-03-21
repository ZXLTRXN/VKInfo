package com.example.vkinfo;

import android.content.Context;

import com.example.vkinfo.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class User {
    String id;
    String firstName;
    String lastName;
    int notify = 0;


    public User(String id, String firstName, String lastName, int notify) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.notify = notify;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \"" + id + '\"' +
                ", \"firstName\": \"" + firstName + '\"' +
                ", \"lastName\": \"" + lastName + '\"' +
                ", \"notify\": \"" + notify + '\"' +
                '}';
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }
}
