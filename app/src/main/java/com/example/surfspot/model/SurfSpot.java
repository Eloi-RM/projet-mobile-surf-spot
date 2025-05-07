package com.example.surfspot.model;

import java.util.Date;

public class SurfSpot {
    private long id;
    private String surfBreak;
    private String photos;
    private String address;
    private String destination;
    private String state;
    private int difficulty;
    private String seasonStart;
    private String seasonEnd;

    public SurfSpot() {
        // Constructeur vide requis pour Firebase/JSON parsing
    }

    public SurfSpot(long id, String surfBreak, String photos, String address, String destination, String state, int difficulty, String seasonStart, String seasonEnd) {
        this.id = id;
        this.surfBreak = surfBreak;
        this.photos = photos;
        this.address = address;
        this.destination = destination;
        this.state = state;
        this.difficulty = difficulty;
        this.seasonStart = seasonStart;
        this.seasonEnd = seasonEnd;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSurfBreak() {
        return surfBreak;
    }

    public void setSurfBreak(String surfBreak) {
        this.surfBreak = surfBreak;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getSeasonStart() {
        return seasonStart;
    }

    public void setSeasonStart(String seasonStart) {
        this.seasonStart = seasonStart;
    }

    public String getSeasonEnd() {
        return seasonEnd;
    }

    public void setSeasonEnd(String seasonEnd) {
        this.seasonEnd = seasonEnd;
    }
}