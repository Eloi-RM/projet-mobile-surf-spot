package com.example.surfspot.model;

import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import mil.nga.crs.common.DateTime;

public class SurfSpot {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

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