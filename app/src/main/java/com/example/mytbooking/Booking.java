package com.example.mytbooking;

public class Booking {

    String date;
    String time;
    String id;

    public void Booking(){}

    public Booking(String id, String date,String time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
