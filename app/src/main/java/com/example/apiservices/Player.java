package com.example.apiservices;

public class Player {
    private String id;
    private String name;
    private String position;
    private String dateOfBirth;
    private String nationality;
    private String shirtNumber;

    public Player(String id, String name, String position, String dateOfBirth, String nationality, String shirtNumber) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.shirtNumber = shirtNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getShirtNumber() {
        return shirtNumber;
    }
} 