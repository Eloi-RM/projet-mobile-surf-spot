package com.example.surfspot.repository;

import android.content.Context;
import android.util.Log;

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
    private static final String TAG = "SurfSpotRepository";
    private static SurfSpotRepository instance;
    private final List<SurfSpot> surfSpots = new ArrayList<>();
    private final Context context;
    private final MutableLiveData<List<SurfSpot>> surfSpotsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<SurfSpot> selectedSpotLiveData = new MutableLiveData<>();

    private interface API {
        @GET("/api/spots")
        Call<List<SurfSpot>> getAllSpots();  // Changé à List<SurfSpot> au lieu de ApiResponse
    }

    // Classe ApiResponse supprimée car non nécessaire

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
        API api = retrofit.create(API.class);

        api.getAllSpots().enqueue(new Callback<List<SurfSpot>>() {
            @Override
            public void onResponse(Call<List<SurfSpot>> call, Response<List<SurfSpot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    surfSpots.clear();
                    List<SurfSpot> spotList = response.body();
                    for (SurfSpot spot : spotList) {
                        if (spot != null) {
                            surfSpots.add(spot);
                            Log.d(TAG, "Spot chargé: " + spot.getName() + ", ID: " + spot.getId());
                        } else {
                            Log.e(TAG, "Spot null dans la réponse");
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
            public void onFailure(Call<List<SurfSpot>> call, Throwable t) {
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

    public SurfSpot getSurfSpotById(int id) {
        // Chercher le spot dans la liste locale
        for (SurfSpot spot : surfSpots) {
            if (spot.getId() != 0 && spot.getId() == id) {
                selectedSpotLiveData.setValue(spot);
                Log.d(TAG, "Spot trouvé par ID: " + spot.getName());
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