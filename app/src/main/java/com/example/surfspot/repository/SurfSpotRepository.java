package com.example.surfspot.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.surfspot.MainActivity;
import com.example.surfspot.model.SurfSpot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class SurfSpotRepository {
    private static SurfSpotRepository instance;
    private final List<SurfSpot> surfSpots = new ArrayList<>();
    private final Context context;
    private final MutableLiveData<List<SurfSpot>> surfSpotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private interface AirtableAPI {
        @GET("/v0/appLzFwKNna0K8m27/Surf%20Destinations")
        Call<AirtableResponse> getAllSpots();
    }

    private static class AirtableResponse {
        List<AirtableRecord> records;

        public static class AirtableRecord {
            String id;
            SurfSpot fields;
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
                        SurfSpot spot = record.fields;
                        spot.setId(record.id); // S'assurer que l'ID est défini
                        surfSpots.add(spot);
                    }
                    // Mettre à jour LiveData
                    surfSpotsLiveData.setValue(new ArrayList<>(surfSpots));
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<AirtableResponse> call, Throwable t) {
                // Gestion d'erreur
                isLoading.setValue(false);
            }
        });
    }

    public List<SurfSpot> getAllSurfSpots() {
        return new ArrayList<>(surfSpots); // protection contre modification externe
    }

    public LiveData<List<SurfSpot>> getSurfSpotsLiveData() {
        return surfSpotsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public SurfSpot getSurfSpotById(String id) {
        for (SurfSpot spot : surfSpots) {
            if (spot.getId().equals(id)) {
                return spot;
            }
        }
        return null;
    }

    // Méthode pour forcer le rechargement des données
    public void refreshData() {
        loadData();
    }
}