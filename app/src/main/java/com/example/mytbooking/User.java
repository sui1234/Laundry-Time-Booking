package com.example.mytbooking;

public class User {
    String name;
    String address;
    String email;


    public User(String email, String name, String address) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }
}
