package com.example.mytbooking;

public class Booking {
    String name;
    String date;
    String time;


    public void Booking(){}

    public Booking(String name, String date,String time) {
        this.name = name;
        this.date = date;
        this.time = time;


    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
