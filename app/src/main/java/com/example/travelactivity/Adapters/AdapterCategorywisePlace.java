package com.example.travelactivity.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelactivity.Common.Urls;
import com.example.travelactivity.Models.CategoryWisePlaces;
import com.example.travelactivity.PlaceDetails;
import com.example.travelactivity.R;

import java.util.List;

public class AdapterCategorywisePlace extends BaseAdapter {

    List<CategoryWisePlaces> list;
    Activity activity;

    public AdapterCategorywisePlace(List<CategoryWisePlaces> categoryWisePlacesList, Activity activity) {
        this.list = categoryWisePlacesList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        final LayoutInflater inflater = (LayoutInflater)  activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null)
        {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_categorywise_place,null);

            holder.ivCategorywisePlaceImage = view.findViewById(R.id.icCategorywiseplaceimage);
            holder.tvCategorywisePlacename = view.findViewById(R.id.tvCategorywisePlacePlaceName);
            holder.tvCategorywisePlaceRating = view.findViewById(R.id.tvCategorywisePlacePlacerating);
            holder.tvCategorywisePlaceDescription = view.findViewById(R.id.tvCategorywisePlacePlacediscription);

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        final CategoryWisePlaces obj = list.get(position);

        holder.tvCategorywisePlacename.setText(obj.getPlaceName());
        holder.tvCategorywisePlaceRating.setText(obj.getPlaceRating());
        holder.tvCategorywisePlaceDescription.setText(obj.getPlaceDescription());

        Glide.with(activity)
                .load(Urls.imagesDirectory +obj.getPlaceImage())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivCategorywisePlaceImage);

        holder.ivCategorywisePlaceImage.setOnClickListener(
                v -> {
                    Intent intent = new Intent(activity, PlaceDetails.class);
                    intent.putExtra("placeName",obj.getPlaceName());
                    intent.putExtra("placeDescription",obj.getPlaceDescription());
                    intent.putExtra("placeRating",obj.getPlaceRating());
                    intent.putExtra("placeImage",obj.getPlaceImage());
                    activity.startActivity(intent);
                }
        );
        return view;

    }


    class ViewHolder{
        ImageView ivCategorywisePlaceImage;
        TextView tvCategorywisePlacename,tvCategorywisePlaceRating,tvCategorywisePlaceDescription;
    }

}
