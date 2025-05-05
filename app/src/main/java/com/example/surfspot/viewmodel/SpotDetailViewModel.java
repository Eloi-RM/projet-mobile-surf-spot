package com.example.surfspot.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.repository.SurfSpotRepository;

public class SpotDetailViewModel extends AndroidViewModel {
    private final MutableLiveData<SurfSpot> selectedSpot = new MutableLiveData<>();
    private final SurfSpotRepository repository;

    public SpotDetailViewModel(@NonNull Application application) {
        super(application);
        repository = SurfSpotRepository.getInstance(application);
    }

    public void loadSurfSpot(Long spotId) {
        SurfSpot spot = repository.getSurfSpotById(spotId);
        selectedSpot.setValue(spot);
    }

    public LiveData<SurfSpot> getSelectedSpot() {
        return selectedSpot;
    }
}
