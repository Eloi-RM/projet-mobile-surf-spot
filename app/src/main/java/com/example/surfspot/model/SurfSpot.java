package com.example.surfspot.model;

import com.example.surfspot.Enum.SurfBreak;
import com.google.gson.annotations.SerializedName;

public class SurfSpot {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("surfBreak")
    private SurfBreak surfBreak;

    @SerializedName("address") //prankex
    private String address;

    @SerializedName("difficultyLevel")
    private int difficulty;

    @SerializedName("seasonStart")
    private String seasonStart;

    @SerializedName("seasonEnd")
    private String seasonEnd;

    @SerializedName("photoUrl")
    private String photoUrl;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "Sans nom";
    }

    public String getSurfBreak() {
        return surfBreak.getValue();
    }

    public String getAddress() {
        return address;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getSeasonStart() {
        return seasonStart;
    }

    public String getSeasonEnd() {
        return seasonEnd;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}