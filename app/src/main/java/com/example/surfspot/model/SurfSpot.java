package com.example.surfspot.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SurfSpot {
    private String id;

    @SerializedName("Destination")
    private String name;

    @SerializedName("Address")
    private String location;

    @SerializedName("Destination State/Country")
    private String description;

    @SerializedName("Difficulty Level")
    private String difficulty;

    @SerializedName("Peak Surf Season Begins")
    private String season;

    @SerializedName("Photos")
    private List<Image> photos;

    // Classe interne pour représenter les images d'Airtable
    public static class Image {
        @SerializedName("url")
        private String url;

        @SerializedName("filename")
        private String filename;

        @SerializedName("id")
        private String id;

        @SerializedName("size")
        private long size;

        @SerializedName("type")
        private String type;

        @SerializedName("thumbnails")
        private Thumbnails thumbnails;

        public String getUrl() {
            return url;
        }

        public String getFilename() {
            return filename;
        }

        public String getId() {
            return id;
        }

        public long getSize() {
            return size;
        }

        public String getType() {
            return type;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }
    }

    // Classe pour les miniatures
    public static class Thumbnails {
        @SerializedName("small")
        private ThumbnailSize small;

        @SerializedName("large")
        private ThumbnailSize large;

        @SerializedName("full")
        private ThumbnailSize full;

        public ThumbnailSize getSmall() {
            return small;
        }

        public ThumbnailSize getLarge() {
            return large;
        }

        public ThumbnailSize getFull() {
            return full;
        }
    }

    public static class ThumbnailSize {
        @SerializedName("url")
        private String url;

        @SerializedName("width")
        private int width;

        @SerializedName("height")
        private int height;

        public String getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name != null ? name : "Sans nom";
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

    public String getSeason() {
        return season;
    }

    public List<Image> getPhotos() {
        return photos;
    }

    // Méthode utilitaire pour obtenir la première URL d'image
    public String getFirstImageUrl() {
        if (photos != null && !photos.isEmpty() && photos.get(0) != null) {
            return photos.get(0).getUrl();
        }
        return null;
    }

    // Méthode pour obtenir une miniature de taille large de la première photo
    public String getFirstImageLargeUrl() {
        if (photos != null && !photos.isEmpty() && photos.get(0) != null &&
                photos.get(0).getThumbnails() != null &&
                photos.get(0).getThumbnails().getLarge() != null) {
            return photos.get(0).getThumbnails().getLarge().getUrl();
        }
        return getFirstImageUrl(); // Retourne l'URL originale si la miniature n'est pas disponible
    }
}