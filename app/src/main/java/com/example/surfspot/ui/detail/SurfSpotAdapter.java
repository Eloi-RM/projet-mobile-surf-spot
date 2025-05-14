package com.example.surfspot.ui.detail;

import static com.example.surfspot.ui.detail.SpotDetailFragment.displayDifficulty;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;

import java.util.ArrayList;
import java.util.List;

public class SurfSpotAdapter extends RecyclerView.Adapter<SurfSpotAdapter.ViewHolder> {
    private static final String TAG = "SurfSpotAdapter";
    private final Context context;
    private List<SurfSpot> spots;
    private final List<SurfSpot> fullSpots; // Liste complète pour filtrage
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int spotId);
    }

    public SurfSpotAdapter(Context context, List<SurfSpot> spots) {
        this.context = context;
        this.spots = new ArrayList<>(spots);
        this.fullSpots = new ArrayList<>(spots);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_surf_spot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SurfSpot spot = spots.get(position);

        Log.d(TAG, "Binding spot: " + spot.getName() + ", ID: " + spot.getId());

        holder.nameTextView.setText(spot.getName());

        if (spot.getAddress() != null && !spot.getAddress().isEmpty()) {
            holder.locationTextView.setText(spot.getAddress());
            holder.locationTextView.setVisibility(View.VISIBLE);
        } else {
            holder.locationTextView.setVisibility(View.GONE);
        }

        if (spot.getDifficulty() != 0) {
            String diff = displayDifficulty(spot.getDifficulty());
            holder.difficultyTextView.setText(diff);
            holder.difficultyTextView.setVisibility(View.VISIBLE);
        } else {
            holder.difficultyTextView.setVisibility(View.GONE);
        }

        String imageUrl = spot.getPhotoUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Chargement de l'image dans l'adaptateur: " + imageUrl);

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.placeholder_surf)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(holder.imageView);

            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            Log.w(TAG, "Pas d'URL d'image pour le spot: " + spot.getName());
            holder.imageView.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(spot.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return spots != null ? spots.size() : 0;
    }

    /**
     * Met à jour la liste affichée (filtrée)
     */
    public void updateList(List<SurfSpot> newSpots) {
        this.spots.clear();
        this.spots.addAll(newSpots);
        notifyDataSetChanged();
        Log.d(TAG, "Données mises à jour, nombre d'éléments: " + newSpots.size());
    }
    public void setFullList(List<SurfSpot> newList) {
        fullSpots.clear();
        fullSpots.addAll(newList);
        updateList(newList); // met aussi à jour ce qui est affiché
    }
    /**
     * Filtre la liste des spots selon le texte entré
     */
    public void filter(String text) {
        List<SurfSpot> filteredList = new ArrayList<>();
        for (SurfSpot spot : fullSpots) {
            if (spot.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(spot);
            }
        }
        updateList(filteredList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final ImageView imageView;
        final TextView nameTextView;
        final TextView locationTextView;
        final TextView difficultyTextView;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            imageView = view.findViewById(R.id.spot_image);
            nameTextView = view.findViewById(R.id.spot_name);
            locationTextView = view.findViewById(R.id.spot_location);
            difficultyTextView = view.findViewById(R.id.spot_difficulty);
        }
    }
}
