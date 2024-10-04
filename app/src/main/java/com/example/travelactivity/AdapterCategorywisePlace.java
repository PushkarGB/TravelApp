package com.example.travelactivity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterCategorywisePlace extends BaseAdapter {

    List<POJOCategorywisePlace> list;
    Activity activity;

    public AdapterCategorywisePlace(List<POJOCategorywisePlace> pojoCategorywisePlaceList, Activity activity) {
        this.list = pojoCategorywisePlaceList;
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
        final POJOCategorywisePlace obj = list.get(position);

        holder.tvCategorywisePlacename.setText(obj.getPlacename());
        holder.tvCategorywisePlaceRating.setText(obj.getPlacerating());
        holder.tvCategorywisePlaceDescription.setText(obj.getPlacediscription());

        Glide.with(activity)
                .load("http://192.168.0.107:80/TravelAPI/images/"+obj.getPlaceimage())
                .skipMemoryCache(true)
                .error(R.drawable.image_not_found)
                .into(holder.ivCategorywisePlaceImage);

        return view;

    }


    class ViewHolder{
        ImageView ivCategorywisePlaceImage;
        TextView tvCategorywisePlacename,tvCategorywisePlaceRating,tvCategorywisePlaceDescription;
    }

}
