package com.example.surfspot.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
        // Constructeur vide requis
    }

    public interface OnSpotSelectedListener {
        void onSpotSelected(String spotId);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callback = (OnSpotSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " doit implémenter OnSpotSelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // nécessaire pour ajouter un menu (SearchView)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_surf_spot_list, container, false);

        requireActivity().setTitle(R.string.app_name);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new SurfSpotAdapter(requireContext(), new ArrayList<>());
        adapter.setOnItemClickListener(this::onSpotClick);
        recyclerView.setAdapter(adapter);

        repository = SurfSpotRepository.getInstance(requireContext());

        // Rafraîchissement manuel
        swipeRefreshLayout.setOnRefreshListener(() -> repository.refreshData());

        // Observer les données
        repository.getSurfSpotsLiveData().observe(getViewLifecycleOwner(), surfSpots -> {
            adapter.setFullList(surfSpots);    // stocker toutes les données
            adapter.updateList(surfSpots);     // afficher tout par défaut
        });

        repository.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            swipeRefreshLayout.setRefreshing(isLoading);
        });

        return view;
    }

    private void onSpotClick(String spotId) {
        callback.onSpotSelected(spotId);
    }

    // Menu avec SearchView
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Rechercher un spot...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query); // filtrer à la soumission
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // filtrer au fur et à mesure
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
