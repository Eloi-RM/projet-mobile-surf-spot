package com.example.surfspot.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.viewmodel.SpotDetailViewModel;

public class SpotDetailFragment extends Fragment {
    private static final String TAG = "SpotDetailFragment";
    private static final String ARG_SPOT_ID = "spot_id";

    private int spotId;
    private SpotDetailViewModel viewModel;

    private ImageView spotImageView;
    private TextView nameTextView;
    private TextView locationTextView;
    private TextView difficultyTextView;
    private TextView seasonTextView;

    private TextView seasonEndTextView;

    public static SpotDetailFragment newInstance(int spotId) {
        SpotDetailFragment fragment = new SpotDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SPOT_ID, spotId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            spotId = getArguments().getInt(ARG_SPOT_ID);
            Log.d(TAG, "Spot ID reçu: " + spotId);
        }
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
        difficultyTextView = view.findViewById(R.id.detail_difficulty);
        seasonTextView = view.findViewById(R.id.detail_season);
        seasonEndTextView = view.findViewById(R.id.detail_end_season);

        // Obtenir le ViewModel de l'activité parente
        viewModel = new ViewModelProvider(requireActivity()).get(SpotDetailViewModel.class);

        // Observer les changements de données
        viewModel.getSelectedSpot().observe(getViewLifecycleOwner(), this::updateUI);

        // Charger les détails du spot si on a l'ID
        if (spotId != 0) {
            viewModel.loadSurfSpot(spotId);
        }

        return view;
    }

    private void updateUI(SurfSpot spot) {
        if (spot != null) {
            Log.d(TAG, "Mise à jour de l'UI avec le spot: " + spot.getName());

            // Mettre à jour les champs texte
            nameTextView.setText(spot.getName());

            if (spot.getAddress() != null && !spot.getAddress().isEmpty()) {
                locationTextView.setText(spot.getAddress());
                locationTextView.setVisibility(View.VISIBLE);
            } else {
                locationTextView.setVisibility(View.GONE);
            }

            // Mettre à jour les autres champs si disponibles
            if (spot.getDifficulty() != 0) {
                String diff = displayDifficulty(spot.getDifficulty());
                difficultyTextView.setText("Niveau de difficulté : " + diff);
                difficultyTextView.setVisibility(View.VISIBLE);
            } else {
                difficultyTextView.setVisibility(View.GONE);
            }

            if (spot.getSeasonStart() != null) {
                seasonTextView.setText("Début de la saison : " + spot.getSeasonStart());
                seasonTextView.setVisibility(View.VISIBLE);
            } else {
                seasonTextView.setVisibility(View.GONE);
            }

            if (spot.getSeasonEnd() != null) {
                seasonEndTextView.setText("Fin de la saison : " + spot.getSeasonEnd());
                seasonEndTextView.setVisibility(View.VISIBLE);
            } else {
                seasonEndTextView.setVisibility(View.GONE);
            }



            // Charger l'image
            String imageUrl = spot.getPhotoUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Log.d(TAG, "Chargement de l'image: " + imageUrl);

                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.placeholder_surf) // Créez un placeholder dans res/drawable
                        .diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(requireContext())
                        .load(imageUrl)
                        .apply(requestOptions)
                        .into(spotImageView);

                spotImageView.setVisibility(View.VISIBLE);
            } else {
                Log.w(TAG, "Pas d'URL d'image pour ce spot");
                spotImageView.setVisibility(View.GONE);
                // Ou afficher une image par défaut
                // spotImageView.setImageResource(R.drawable.no_image);
            }
        } else {
            Log.e(TAG, "Le spot est null, impossible de mettre à jour l'UI");
            Toast.makeText(requireContext(), "Impossible de charger les détails du spot", Toast.LENGTH_SHORT).show();
        }
    }

    public static String displayDifficulty(int difficulty) {

        String myString = "";
        for(int i=0; i < 5; i++) {
            if(i < difficulty) {
                myString += "\uD83C\uDF0A";
            }
            else {
                myString += "\u25EF";
            }
            myString += " ";
        }
        return myString;
    }
}