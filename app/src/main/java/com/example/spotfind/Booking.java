package com.example.spotfind;

public class Booking {
    private String userId;               // ID of the user making the booking
    private String userName;             // Name of the user making the booking
    private String parkingSpaceId;       // ID of the parking space being booked
    private String ownerName;            // Name of the parking space owner
    private String ownerContact;         // Contact number of the parking space owner
    private long bookingTime;          // Time when the booking is made
    private String location;             // Location of the parking space
    private String parkingSize;          // Size of the parking space

    public Booking() {
    }

    public Booking(String userId, String userName, String parkingSpaceId,
                   String ownerName, String ownerContact, long bookingTime,
                   String location, String parkingSize) {
        this.userId = userId;
        this.userName = userName;
        this.parkingSpaceId = parkingSpaceId;
        this.ownerName = ownerName;
        this.ownerContact = ownerContact;
        this.bookingTime = bookingTime;
        this.location = location;
        this.parkingSize = parkingSize;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(String parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(long bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParkingSize() {
        return parkingSize;
    }

    public void setParkingSize(String parkingSize) {
        this.parkingSize = parkingSize;
    }
}

