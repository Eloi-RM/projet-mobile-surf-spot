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
    private List<SurfSpot> surfSpots;
    private Context context;

    // Interface fonctionnelle pour les clics
    public interface OnItemClickListener {
        void onItemClick(long spotId);
    }

    private OnItemClickListener listener;

    // Méthode pour définir le listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SurfSpotAdapter(Context context, List<SurfSpot> surfSpots) {
        this.context = context;
        this.surfSpots = surfSpots;
    }

    @NonNull
    @Override
    public SurfSpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_surf_spot, parent, false);
        return new SurfSpotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurfSpotViewHolder holder, int position) {
        SurfSpot spot = surfSpots.get(position);
        holder.tvBreakType.setText(spot.getSurfBreak());
        holder.tvAddress.setText(spot.getAddress());

        Glide.with(context)
                .load(spot.getPhotos())
                .placeholder(R.drawable.placeholder_surf)
                .into(holder.ivPhoto);

        // Configurer le clic sur l'élément
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(spot.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return surfSpots != null ? surfSpots.size() : 0;
    }

    public void updateData(List<SurfSpot> newSpots) {
        this.surfSpots = newSpots;
        notifyDataSetChanged();
    }

    static class SurfSpotViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvBreakType;
        TextView tvAddress;

        SurfSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvBreakType = itemView.findViewById(R.id.tv_break_type);
            tvAddress = itemView.findViewById(R.id.tv_address);
        }
    }
}