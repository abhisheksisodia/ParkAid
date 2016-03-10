package com.parkaid.app.model;

import java.text.DateFormat;

/**
 * Created by abhisheksisodia on 16-03-09.
 */
public class GaitData {

    //private variables
    int id;
    String eventType;
    String eventLocation;
    String eventDate;

    // Empty constructor
    public GaitData(){

    }

    public GaitData(int id, String eventType, String eventLocation, String eventDate){
        this.id = id;
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
    }

    public GaitData(String eventType, String eventLocation, String eventDate){
        this.eventType = eventType;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
