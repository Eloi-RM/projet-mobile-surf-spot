package com.example.surfspot.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

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
    private MapView mapView;

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
        mapView = view.findViewById(R.id.map);

        Context ctx = requireContext().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(requireActivity().getPackageName());

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

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
                // C'est un java.util.Date
                // Afficher dans le TextView
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

            double lat = spot.getLatitude();
            double lng = spot.getLatitude();

            if (lat != 0 && lng != 0) {
                GeoPoint location = new GeoPoint(lat, lng);
                IMapController mapController = mapView.getController();
                mapController.setZoom(13.5);
                mapController.setCenter(location);

                Marker marker = new Marker(mapView);
                marker.setPosition(location);
                marker.setTitle(spot.getName());
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().clear(); // Nettoyer les anciens marqueurs
                mapView.getOverlays().add(marker);
            } else {
                Log.w(TAG, "Coordonnées GPS manquantes pour ce spot");
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

    @Override
    public void onResume() {
        super.onResume();
        //if (mapView != null) mapView.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //if (mapView != null) mapView.onPause();
        mapView.onPause();
    }
}