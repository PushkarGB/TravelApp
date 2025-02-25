package com.example.travelactivity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelactivity.CategorywisePlaceActivity;
import com.example.travelactivity.Common.Urls;
import com.example.travelactivity.Models.CategoryDetails;
import com.example.travelactivity.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    List<CategoryDetails> listOfCategories;
    Context context;
    public CategoriesAdapter(List<CategoryDetails> listOfCategories, Context context) {
        this.listOfCategories = listOfCategories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lv_get_all_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder holder, int position) {
        CategoryDetails category = listOfCategories.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());
        Glide.with(context).load(Urls.imagesDirectory + category.getCategoryImage()).into(holder.ivCategoryImage);

        holder.cvCategoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CategorywisePlaceActivity.class);
                i.putExtra("categoryname", category.getCategoryName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  listOfCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryImage;
        TextView tvCategoryName;
        CardView cvCategoryCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryImage = itemView.findViewById(R.id.ivcategoryimage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            cvCategoryCard = itemView.findViewById(R.id.cvCategoryList);
        }
    }
}
