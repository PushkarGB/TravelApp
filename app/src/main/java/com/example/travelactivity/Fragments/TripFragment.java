package com.example.travelactivity.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelactivity.AadapterGetAllCategoryDetails;
import com.example.travelactivity.Common.TableHeaders;
import com.example.travelactivity.Common.Urls;
import com.example.travelactivity.Models.CategoryDetails;
import com.example.travelactivity.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TripFragment extends Fragment {
    SearchView searchcategory;
    ListView lvShowAllCategory;
    TextView tvNoCategoryAvailable;
    List<CategoryDetails> CategoryDetails;
    AadapterGetAllCategoryDetails aadapterGetAllCategoryDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        CategoryDetails = new ArrayList<>();
        searchcategory = view.findViewById(R.id.svcategoryfragmentsearchcategory);
        lvShowAllCategory = view.findViewById(R.id.lvCategoryFragmentShowMultipleCategory);
        tvNoCategoryAvailable = view.findViewById(R.id.tvCategoryFragmentCategoryAvailable);

        searchcategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchcategory(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String query) {
                searchcategory(query);
                return false;
            }
        });

        getAllCategory();
        return view;
    }

    private void searchcategory(String query) {
        List<CategoryDetails> tempcategory = new ArrayList<>();
        tempcategory.clear();

        for (CategoryDetails obj : CategoryDetails) {
            if (obj.getCategoryName().toUpperCase().contains(query.toUpperCase())) {
                tempcategory.add(obj);
            } else {
                tvNoCategoryAvailable.setVisibility(View.VISIBLE);
            }

            aadapterGetAllCategoryDetails = new AadapterGetAllCategoryDetails(tempcategory,
                    getActivity());
            lvShowAllCategory.setAdapter(aadapterGetAllCategoryDetails);
        }


    }

    private void getAllCategory() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.getAllCategoryDetailsWebService,
                params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("getAllCategory");
                            if (jsonArray.isNull(0)) {
                                tvNoCategoryAvailable.setVisibility(View.VISIBLE);
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid = jsonObject.getString(TableHeaders.CATEGORIES_id);
                                String strCategoryImage = jsonObject.getString(TableHeaders.CATEGORIES_image);
                                String strCategoryName = jsonObject.getString(TableHeaders.CATEGORIES_name);

                                CategoryDetails.add(new CategoryDetails(strid, strCategoryImage, strCategoryName));
                            }
                            aadapterGetAllCategoryDetails = new AadapterGetAllCategoryDetails(CategoryDetails,
                                    getActivity());

                            lvShowAllCategory.setAdapter(aadapterGetAllCategoryDetails);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
