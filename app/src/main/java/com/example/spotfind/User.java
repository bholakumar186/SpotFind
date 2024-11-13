package com.example.spotfind;

public class User {
    public String name, email, phone, address, pin;
    public double latitude, longitude;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email, String phone, String address, String pin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.pin = pin;
    }
    public User(String name, String email, String phone, String address, String pin, double latitude, double longitude) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.pin = pin;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
