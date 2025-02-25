package com.example.travelactivity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


import com.example.travelactivity.Common.Urls;
import com.example.travelactivity.Models.CategoryWisePlaces;
import com.example.travelactivity.PlaceDetails;
import com.example.travelactivity.R;

public class PopularPlacesAdapter extends RecyclerView.Adapter<PopularPlacesAdapter.ViewHolder> {
    
    List<CategoryWisePlaces> placesList;
    private Context context;


    public PopularPlacesAdapter(List<CategoryWisePlaces> popularPlaces,Context context) {
        this.context = context;
        this.placesList = popularPlaces;
    
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lv_categorywise_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryWisePlaces place = placesList.get(position);
        holder.tvPlacename.setText(place.getPlaceName());
        holder.tvPlaceDescription.setText(place.getPlaceDescription());
        holder.tvPlaceRating.setText(place.getPlaceRating());
        Glide.with(context).load(Urls.imagesDirectory + place.getPlaceImage()).into(holder.ivPlaceImage);
        holder.itemView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(context, PlaceDetails.class);
                    intent.putExtra("placeName",place.getPlaceName());
                    intent.putExtra("placeDescription",place.getPlaceDescription());
                    intent.putExtra("placeRating",place.getPlaceRating());
                    intent.putExtra("placeImage",place.getPlaceImage());
                    context.startActivity(intent);
                }
        );

    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
    
        TextView tvPlaceRating , tvPlacename , tvPlaceDescription;
        ImageView ivPlaceImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlaceImage = itemView.findViewById(R.id.icCategorywiseplaceimage);
            tvPlacename = itemView.findViewById(R.id.tvCategorywisePlacePlaceName);
            tvPlaceRating = itemView.findViewById(R.id.tvCategorywisePlacePlacerating);
            tvPlaceDescription = itemView.findViewById(R.id.tvCategorywisePlacePlacediscription);
        }
    }
}
