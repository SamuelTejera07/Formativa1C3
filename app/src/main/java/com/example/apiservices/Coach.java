package com.example.apiservices;

public class Coach {
    private String id;
    private String name;
    private String dateOfBirth;
    private String nationality;

    public Coach(String id, String name, String dateOfBirth, String nationality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }
} 