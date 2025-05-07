package com.example.surfspot.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.surfspot.MainActivity;
import com.example.surfspot.model.SurfSpot;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class SurfSpotRepository {
    private static final String TAG = "SurfSpotRepository";
    private static SurfSpotRepository instance;
    private final List<SurfSpot> surfSpots = new ArrayList<>();
    private final Context context;
    private final MutableLiveData<List<SurfSpot>> surfSpotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<SurfSpot> selectedSpotLiveData = new MutableLiveData<>();

    private interface AirtableAPI {
        @GET("/v0/appLzFwKNna0K8m27/Surf%20Destinations")
        Call<AirtableResponse> getAllSpots();
    }

    public static class AirtableResponse {
        @SerializedName("records")
        public List<AirtableRecord> records;

        public static class AirtableRecord {
            @SerializedName("id")
            public String id;

            @SerializedName("fields")
            public SurfSpot fields;
        }
    }

    // Constructeur privé
    private SurfSpotRepository(Context context) {
        this.context = context.getApplicationContext(); // Pour éviter les memory leaks
        loadData();
    }

    // Singleton avec passage de contexte
    public static SurfSpotRepository getInstance(Context context) {
        if (instance == null) {
            instance = new SurfSpotRepository(context);
        }
        return instance;
    }

    private void loadData() {
        isLoading.setValue(true);

        // Utiliser la méthode getRetrofitInstance de MainActivity
        Retrofit retrofit = MainActivity.getRetrofitInstance();
        AirtableAPI api = retrofit.create(AirtableAPI.class);

        api.getAllSpots().enqueue(new Callback<AirtableResponse>() {
            @Override
            public void onResponse(Call<AirtableResponse> call, Response<AirtableResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    surfSpots.clear();
                    for (AirtableResponse.AirtableRecord record : response.body().records) {
                        if (record.fields != null) {
                            SurfSpot spot = record.fields;
                            spot.setId(record.id); // S'assurer que l'ID est défini
                            surfSpots.add(spot);
                            Log.d(TAG, "Spot chargé: " + spot.getName() + ", ID: " + spot.getId() +
                                    ", Image: " + (spot.getFirstImageUrl() != null ? spot.getFirstImageUrl() : "aucune"));
                        } else {
                            Log.e(TAG, "Champ fields null pour l'enregistrement: " + record.id);
                        }
                    }
                    // Mettre à jour LiveData
                    surfSpotsLiveData.setValue(new ArrayList<>(surfSpots));
                } else {
                    Log.e(TAG, "Erreur lors du chargement des spots: " +
                            (response.errorBody() != null ? response.errorBody().toString() : "Inconnu") +
                            ", Code: " + response.code());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<AirtableResponse> call, Throwable t) {
                Log.e(TAG, "Échec du chargement des spots", t);
                isLoading.setValue(false);
            }
        });
    }

    public LiveData<List<SurfSpot>> getSurfSpotsLiveData() {
        return surfSpotsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<SurfSpot> getSelectedSpotLiveData() {
        return selectedSpotLiveData;
    }

    public SurfSpot getSurfSpotById(String id) {
        // Chercher le spot dans la liste locale
        for (SurfSpot spot : surfSpots) {
            if (spot.getId() != null && spot.getId().equals(id)) {
                selectedSpotLiveData.setValue(spot);
                Log.d(TAG, "Spot trouvé par ID: " + spot.getName() + ", Image URL: " +
                        (spot.getFirstImageUrl() != null ? spot.getFirstImageUrl() : "aucune"));
                return spot;
            }
        }
        Log.e(TAG, "Spot non trouvé pour l'ID: " + id);
        return null;
    }

    // Méthode pour forcer le rechargement des données
    public void refreshData() {
        loadData();
    }
}