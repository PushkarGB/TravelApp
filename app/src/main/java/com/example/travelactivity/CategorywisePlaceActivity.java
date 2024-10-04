package com.example.travelactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelactivity.comman.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategorywisePlaceActivity extends AppCompatActivity {


    SearchView searchCategorywisePlace;
    ListView lvCategorywisePlace;
    TextView tvNoPlaceFound;
    String strCategoryName;
    List<POJOCategorywisePlace> pojoCategorywisePlaceList;
    AdapterCategorywisePlace adapterCategorywisePlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorywise_place);
        searchCategorywisePlace = findViewById(R.id.svCategorywisePlaceSearchPlace);
        lvCategorywisePlace = findViewById(R.id.lvCategorywisePlacelistofplace);
        tvNoPlaceFound = findViewById(R.id.tvCategorywisePlaceNoPlaceisFound);

        pojoCategorywisePlaceList = new ArrayList<>();

        strCategoryName =getIntent().getStringExtra("categoryname");

        getCategorywisePlaceList();

        searchCategorywisePlace.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPlacebyCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchPlacebyCategory(query);
                return false;
            }

            private void searchPlacebyCategory(String query) {
                List<POJOCategorywisePlace> templist = new ArrayList<>();
                templist.clear();

                for (POJOCategorywisePlace obj:pojoCategorywisePlaceList) {
                    if (obj.getCategoryname().toUpperCase().contains(query.toUpperCase()) ||
                            obj.getPlacename().toUpperCase().contains(query.toUpperCase()) ||
                            obj.getPlacerating().toUpperCase().contains(query.toUpperCase()) ||
                            obj.getPlacediscription().toUpperCase().contains(query.toUpperCase())) {
                        templist.add(obj);
                    }
                }

                    adapterCategorywisePlace = new AdapterCategorywisePlace(templist,this);
                    lvCategorywisePlace.setAdapter(adapterCategorywisePlace);

            }
        });
    }

    private void getCategorywisePlaceList() {
        //classname objectname = new Constructorname();
        AsyncHttpClient client = new AsyncHttpClient();//client - server communication
        RequestParams params = new RequestParams();//put the data

        params.put("categoryname",strCategoryName);

        client.post(Urls.getCategorywisePlace,
                params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("getCategorywisePlace");
                            if (jsonArray.isNull(0))
                            {
                                lvCategorywisePlace.setVisibility(View.GONE);
                                tvNoPlaceFound.setVisibility(View.VISIBLE);
                            }
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid = jsonObject.getString("id");
                                String strcategoryname = jsonObject.getString("categoryname");
                                String strplacename = jsonObject.getString("placename");
                                String strplaceimage = jsonObject.getString("placeimage");
                                String strplacerating = jsonObject.getString("placerating");
                                String strplaceoffer = jsonObject.getString("placeoffer");
                                String strplacediscription = jsonObject.getString("placediscription");

                                pojoCategorywisePlaceList.add(new POJOCategorywisePlace(strid,strcategoryname,strplacename,
                                        strplaceimage,strplacerating,strplaceoffer,strplacediscription));


                            }
                            adapterCategorywisePlace = new
                                    AdapterCategorywisePlace(pojoCategorywisePlaceList,
                                    CategorywisePlaceActivity.this);
                            lvCategorywisePlace.setAdapter(adapterCategorywisePlace);


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(CategorywisePlaceActivity.this,"Server Error",
                                Toast.LENGTH_SHORT).show();
                    }
                }


        );

    }
}