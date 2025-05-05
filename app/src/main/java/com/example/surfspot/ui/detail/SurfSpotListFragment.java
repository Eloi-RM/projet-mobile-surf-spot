package com.example.surfspot.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;
import com.example.surfspot.repository.SurfSpotRepository;

import java.util.List;

public class SurfSpotListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SurfSpotAdapter adapter;
    private SurfSpotRepository repository;
    private OnSpotSelectedListener callback;

    public SurfSpotListFragment() {
        // Constructeur vide requis pour Fragment
    }

    // Interface pour communiquer avec l'activité
    public interface OnSpotSelectedListener {
        void onSpotSelected(long spotId);
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

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Utiliser requireContext() car à ce stade, le fragment est bien attaché
        repository = SurfSpotRepository.getInstance(requireContext());
        List<SurfSpot> surfSpots = repository.getAllSurfSpots();

        adapter = new SurfSpotAdapter(requireContext(), surfSpots);
        adapter.setOnItemClickListener(this::onSpotClick);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void onSpotClick(long spotId) {
        callback.onSpotSelected(spotId);
    }
}
