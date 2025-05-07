package com.example.surfspot.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.repository.SurfSpotRepository;

import java.util.ArrayList;
import java.util.List;

public class SurfSpotListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SurfSpotAdapter adapter;
    private SurfSpotRepository repository;
    private OnSpotSelectedListener callback;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SurfSpotListFragment() {
        // Constructeur vide requis pour Fragment
    }

    // Interface pour communiquer avec l'activité
    public interface OnSpotSelectedListener {
        void onSpotSelected(String spotId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnSpotSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " doit implémenter OnSpotSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surf_spot_list, container, false);

        requireActivity().setTitle(R.string.app_name);

        // Initialiser les vues
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialiser l'adaptateur avec une liste vide
        adapter = new SurfSpotAdapter(requireContext(), new ArrayList<>());
        adapter.setOnItemClickListener(this::onSpotClick);
        recyclerView.setAdapter(adapter);

        // Configurer le refresh layout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            repository.refreshData();
        });

        // Utiliser requireContext() car à ce stade, le fragment est bien attaché
        repository = SurfSpotRepository.getInstance(requireContext());

        // Observer les changements de données
        repository.getSurfSpotsLiveData().observe(getViewLifecycleOwner(), surfSpots -> {
            adapter.updateData(surfSpots);
        });

        // Observer l'état de chargement
        repository.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (!isLoading) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void onSpotClick(String spotId) {
        callback.onSpotSelected(spotId);
    }
}