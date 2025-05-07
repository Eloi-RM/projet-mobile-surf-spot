package com.example.surfspot.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SurfSpot {
    private String id; // Pour stocker l'ID record d'Airtable

    @SerializedName("Name")
    private String name;

    @SerializedName("Location")
    private String location;

    @SerializedName("Description")
    private String description;

    @SerializedName("Difficulty")
    private String difficulty;

    @SerializedName("Best Season")
    private String bestSeason;

    @SerializedName("Images")
    private List<ImageAttachment> images;

    // Classe pour gérer les pièces jointes d'Airtable
    public static class ImageAttachment {
        private String id;
        private String url;
        private String filename;
        private long size;
        private String type;

        public String getId() { return id; }
        public String getUrl() { return url; }
        public String getFilename() { return filename; }
        public long getSize() { return size; }
        public String getType() { return type; }
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public List<ImageAttachment> getImages() {
        return images;
    }

    // Méthode utilitaire pour obtenir l'URL de la première image (ou null)
    public String getFirstImageUrl() {
        if (images != null && !images.isEmpty()) {
            return images.get(0).url;
        }
        return null;
    }
}