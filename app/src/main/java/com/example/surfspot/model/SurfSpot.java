package com.example.surfspot.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class SurfSpot {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("adress") //prankex
    private String address;

    @SerializedName("difficultyLevel")
    private int difficulty;

    @SerializedName("seasonStart")
    private Date seasonStart;

    @SerializedName("seasonEnd")
    private Date seasonEnd;

    @SerializedName("photoUrl")
    private String photoUrl;

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

    public Date getSeasonStart() {
        return seasonStart;
    }

    public Date getSeasonEnd() {
        return seasonEnd;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}