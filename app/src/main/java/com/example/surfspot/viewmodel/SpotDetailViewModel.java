package com.example.surfspot.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.repository.SurfSpotRepository;

public class SpotDetailViewModel extends AndroidViewModel {
    private static final String TAG = "SpotDetailViewModel";
    private final MutableLiveData<SurfSpot> selectedSpot = new MutableLiveData<>();
    private final SurfSpotRepository repository;

    public SpotDetailViewModel(@NonNull Application application) {
        super(application);
        repository = SurfSpotRepository.getInstance(application);

        // Observer les changements du spot sélectionné dans le repository
        repository.getSelectedSpotLiveData().observeForever(spot -> {
            if (spot != null) {
                selectedSpot.setValue(spot);
                Log.d(TAG, "Spot mis à jour dans ViewModel: " + spot.getName());
            }
        });
    }

    public void loadSurfSpot(String spotId) {
        if (spotId == null || spotId.isEmpty()) {
            Log.e(TAG, "Erreur: spotId est null ou vide");
            return;
        }

        Log.d(TAG, "Chargement du spot: " + spotId);
        SurfSpot spot = repository.getSurfSpotById(spotId);

        if (spot != null) {
            selectedSpot.setValue(spot);
            Log.d(TAG, "Spot chargé: " + spot.getName() + ", Image: " +
                    (spot.getFirstImageUrl() != null ? spot.getFirstImageUrl() : "aucune"));
        } else {
            Log.e(TAG, "Impossible de trouver le spot avec l'ID: " + spotId);
        }
    }

    public LiveData<SurfSpot> getSelectedSpot() {
        return selectedSpot;
    }
}