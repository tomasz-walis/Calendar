package org.com.calendar;

//event helper class
public class Event {

    private String id;
    private String title;
    private String date;
    private String time;
    private String details;

    public Event(){}

    public Event(String title, String date, String time, String details) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDetails() {
        return details;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}


