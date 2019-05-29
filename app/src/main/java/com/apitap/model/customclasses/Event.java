package com.apitap.model.customclasses;

/**
 * Created by ashok-kumar on 4/8/16.
 */

public class Event {
    private int key;
    private String response;

    public Event(int key, String response) {
        this.key = key;
        this.response = response;
    }

    public int getKey() {
        return key;
    }

    public String getResponse() {
        return response;
    }
}
