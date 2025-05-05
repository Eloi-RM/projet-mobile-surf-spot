package com.example.surfspot.model;

public class SurfSpot {
    private long id;
    private String surfBreak;
    private String photos;
    private String address;

    public SurfSpot() {
        // Constructeur vide requis pour Firebase/JSON parsing
    }

    public SurfSpot(long id, String surfBreak, String photos, String address) {
        this.id = id;
        this.surfBreak = surfBreak;
        this.photos = photos;
        this.address = address;
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
}