package com.example.surfspot.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.surfspot.R;
import com.example.surfspot.model.SurfSpot;

import java.util.List;

public class SurfSpotAdapter extends RecyclerView.Adapter<SurfSpotAdapter.SurfSpotViewHolder> {

    private final Context context;
    private final List<SurfSpot> surfSpots;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String spotId);
    }

    public SurfSpotAdapter(Context context, List<SurfSpot> surfSpots) {
        this.context = context;
        this.surfSpots = surfSpots;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SurfSpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_surf_spot, parent, false);
        return new SurfSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurfSpotViewHolder holder, int position) {
        SurfSpot spot = surfSpots.get(position);
        holder.bind(spot, listener);
    }

    @Override
    public int getItemCount() {
        return surfSpots.size();
    }

    // Méthode pour mettre à jour les données
    public void updateData(List<SurfSpot> newSpots) {
        surfSpots.clear();
        surfSpots.addAll(newSpots);
        notifyDataSetChanged();
    }

    static class SurfSpotViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView locationTextView;
        private final TextView difficultyTextView;
        private final ImageView spotImageView;

        public SurfSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.spot_name);
            locationTextView = itemView.findViewById(R.id.spot_location);
            difficultyTextView = itemView.findViewById(R.id.spot_difficulty);
            spotImageView = itemView.findViewById(R.id.spot_image);
        }

        public void bind(SurfSpot spot, OnItemClickListener listener) {
            nameTextView.setText(spot.getName());
            locationTextView.setText(spot.getLocation());
            difficultyTextView.setText(spot.getDifficulty());

            // Charger l'image avec Glide
            String imageUrl = spot.getFirstImageUrl();
            if (imageUrl != null) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)

                        .into(spotImageView);
            } else {
            }

            // Définir le clic sur l'élément
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(spot.getId());
                }
            });
        }
    }
}