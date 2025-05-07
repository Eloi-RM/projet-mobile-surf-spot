package com.example.surfspot.repository;

import android.content.Context;

import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SurfSpotRepository {
    private static SurfSpotRepository instance;
    private final List<SurfSpot> surfSpots = new ArrayList<>();
    private final Context context;

    // Constructeur privé
    private SurfSpotRepository(Context context) {
        this.context = context.getApplicationContext(); // Pour éviter les memory leaks
        loadMockData();
    }

    // Singleton avec passage de contexte
    public static SurfSpotRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SurfSpotRepository(context);
        }
        return instance;
    }

    private void loadMockData() {
        try {
            // Lecture du fichier JSON dans /res/raw/surfspots.json
            InputStream inputStream = context.getResources().openRawResource(R.raw.surfspots);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            inputStream.close();

            String jsonData = builder.toString();

            // Parsing JSON
            JSONObject root = new JSONObject(jsonData);
            JSONArray records = root.getJSONArray("records");

            for (int i = 0; i < records.length(); i++) {
                JSONObject record = records.getJSONObject(i);
                JSONObject fields = record.getJSONObject("fields");

                // Récupération du nom du spot
                String surfBreak = "";
                if (fields.has("Surf Break")) {
                    JSONArray breakArray = fields.getJSONArray("Surf Break");
                    if (breakArray.length() > 0) {
                        surfBreak = breakArray.getString(0);
                    }
                }

                // Récupération de l'adresse (Destination)
                String address = fields.optString("Destination", "Adresse inconnue");

                // Récupération de l'URL de la première photo
                String photoUrl = "";
                if (fields.has("Photos")) {
                    JSONArray photos = fields.getJSONArray("Photos");
                    if (photos.length() > 0) {
                        JSONObject photoObj = photos.getJSONObject(0);
                        String rawUrl = photoObj.optString("url", "");
                        photoUrl = rawUrl.replace("<", "").replace(">;", "");
                    }
                }

                // Création de l'objet SurfSpot
                SurfSpot spot = new SurfSpot(i, surfBreak, photoUrl, address, "destination", "state", 7, "seasonStart", "seasonEnd");
                surfSpots.add(spot);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace(); // Tu peux aussi logguer avec Log.e() pour Android
        }
    }

    public List<SurfSpot> getAllSurfSpots() {
        return new ArrayList<>(surfSpots); // protection contre modification externe
    }

    public SurfSpot getSurfSpotById(long id) {
        for (SurfSpot spot : surfSpots) {
            if (spot.getId() == id) {
                return spot;
            }
        }
        return null;
    }

    public SurfSpot getSurfSpotById(String id) {
        try {
            long longId = Long.parseLong(id);
            return getSurfSpotById(longId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
