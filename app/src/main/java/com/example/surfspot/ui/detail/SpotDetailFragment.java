package com.example.surfspot.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.repository.SurfSpotRepository;

public class SpotDetailFragment extends Fragment {

    private static final String ARG_SPOT_ID = "spot_id";

    private String spotId;
    private SurfSpotRepository repository;

    private ImageView spotImageView;
    private TextView nameTextView;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private TextView difficultyTextView;
    private TextView seasonTextView;

    public static SpotDetailFragment newInstance(String spotId) {
        SpotDetailFragment fragment = new SpotDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SPOT_ID, spotId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotId = getArguments().getString(ARG_SPOT_ID);
        }

        repository = SurfSpotRepository.getInstance(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spot_detail, container, false);

        // Initialiser les vues
        spotImageView = view.findViewById(R.id.detail_image);
        nameTextView = view.findViewById(R.id.detail_name);
        locationTextView = view.findViewById(R.id.detail_location);
        descriptionTextView = view.findViewById(R.id.detail_description);
        difficultyTextView = view.findViewById(R.id.detail_difficulty);
        seasonTextView = view.findViewById(R.id.detail_season);

        // Observer les données
        repository.getSurfSpotsLiveData().observe(getViewLifecycleOwner(), surfSpots -> {
            loadSpotDetails();
        });

        // Charger les détails immédiatement si les données sont déjà disponibles
        loadSpotDetails();

        return view;
    }

    private void loadSpotDetails() {
        SurfSpot spot = repository.getSurfSpotById(spotId);
        if (spot != null) {
            nameTextView.setText(spot.getName());
            locationTextView.setText(spot.getLocation());
            descriptionTextView.setText(spot.getDescription());


            // Charger l'image
            String imageUrl = spot.getFirstImageUrl();
            if (imageUrl != null) {
                Glide.with(this)
                        .load(imageUrl)

                        .into(spotImageView);
            } else {

            }
        }
    }
}