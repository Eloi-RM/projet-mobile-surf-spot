package com.example.surfspot.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.viewmodel.SpotDetailViewModel;

public class SpotDetailFragment extends Fragment {
    // Correction: suppression de la déclaration dupliquée
    public static final String ARG_SPOT_ID = "arg_spot_id";

    private long spotId;
    private SpotDetailViewModel viewModel;
    private TextView tvBreakType;
    private TextView tvAddress;
    private ImageView ivPhoto;

    // Ajout de la méthode factory pour créer une instance avec les arguments
    public static SpotDetailFragment newInstance(long spotId) {
        SpotDetailFragment fragment = new SpotDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SPOT_ID, spotId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Récupérer l'ID du spot depuis les arguments
        if (getArguments() != null) {
            spotId = getArguments().getLong(ARG_SPOT_ID, -1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spot_detail, container, false);

        // Configurer la barre d'action pour afficher le bouton retour
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.spot_detail_title);
        }

        // Initialiser les vues
        tvBreakType = view.findViewById(R.id.tv_break_type);
        tvAddress = view.findViewById(R.id.tv_address);
        ivPhoto = view.findViewById(R.id.iv_photo);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialiser le ViewModel
        viewModel = new ViewModelProvider(this).get(SpotDetailViewModel.class);

        // Correction: Utilisation de l'ID récupéré dans onCreate
        if (spotId != -1) {
            // Correction: passage d'un long au lieu d'un String
            viewModel.loadSurfSpot(spotId);

            // Observer les changements de données
            viewModel.getSelectedSpot().observe(getViewLifecycleOwner(), this::updateUI);
        }
    }

    private void updateUI(SurfSpot spot) {
        if (spot != null) {
            tvBreakType.setText(spot.getSurfBreak());
            tvAddress.setText(spot.getAddress());

            // Utiliser Glide pour charger l'image
            Glide.with(this)
                    .load(spot.getPhotos())
                    .placeholder(R.drawable.placeholder_surf)
                    .into(ivPhoto);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Réinitialiser le bouton retour quand on quitte ce fragment
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
}