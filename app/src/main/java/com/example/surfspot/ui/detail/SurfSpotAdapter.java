package com.example.surfspot.ui.detail;

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

import java.util.List;

public class SurfSpotAdapter extends RecyclerView.Adapter<SurfSpotAdapter.ViewHolder> {
    private static final String TAG = "SurfSpotAdapter";
    private final Context context;
    private List<SurfSpot> spots;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String spotId);
    }

    public SurfSpotAdapter(Context context, List<SurfSpot> spots) {
        this.context = context;
        this.spots = spots;
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

        if (spot.getLocation() != null && !spot.getLocation().isEmpty()) {
            holder.locationTextView.setText(spot.getLocation());
            holder.locationTextView.setVisibility(View.VISIBLE);
        } else {
            holder.locationTextView.setVisibility(View.GONE);
        }

        // Charger l'image avec Glide
        String imageUrl = spot.getFirstImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Chargement de l'image dans l'adaptateur: " + imageUrl);

            // Si c'est une URL Airtable, ajouter le paramètre pour une version plus petite de l'image
            if (imageUrl.contains("amazonaws.com") && !imageUrl.contains("?")) {
                imageUrl = imageUrl + "?w=400";
            }

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
            // Ou définir une image par défaut
            // holder.imageView.setImageResource(R.drawable.default_image);
        }

        // Configurer le clic sur l'élément
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

    public void updateData(List<SurfSpot> newSpots) {
        this.spots = newSpots;
        notifyDataSetChanged();
        Log.d(TAG, "Données mises à jour, nombre d'éléments: " + (newSpots != null ? newSpots.size() : 0));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final ImageView imageView;
        final TextView nameTextView;
        final TextView locationTextView;

        ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            imageView = view.findViewById(R.id.spot_image);
            nameTextView = view.findViewById(R.id.spot_name);
            locationTextView = view.findViewById(R.id.spot_location);
        }
    }
}