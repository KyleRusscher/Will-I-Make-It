package com.example.semesterproject.select_gas_station;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.example.semesterproject.StationItem;
import com.example.semesterproject.UrlConstants;
import com.example.semesterproject.readyMap;
import com.example.semesterproject.select_gas_station.dummy.DummyContent;
import com.example.semesterproject.select_vehicle_files.ItemRecyclerViewAdapter;
import com.example.semesterproject.select_vehicle_files.MakeSelection;
import com.example.semesterproject.select_vehicle_files.YearSelection;
import com.example.semesterproject.select_vehicle_files.dummy.YearContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.semesterproject.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GasStations extends AppCompatActivity implements StationFragment.OnListFragmentInteractionListener {

    public static MystationRecyclerViewAdapter adapter = null;

    double currLat;
    double currLon;

    double destLat;
    double destLon;

    double distanceCanGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_stations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        currLat = intent.getDoubleExtra("currLat", 0);
        currLon = intent.getDoubleExtra("currLon", 0);
        destLat = intent.getDoubleExtra("destLat", 0);
        destLon = intent.getDoubleExtra("destLon", 0);
        distanceCanGo = intent.getDoubleExtra("distance", 0);
        System.out.println(distanceCanGo);



        getLocations(currLat, currLon, distanceCanGo);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }




    private void getLocations(double lat, double lon, double radius){
        String url = UrlConstants.get_gas_stations(Double.toString(lat), Double.toString(lon), radius);
        OkHttpClient client = new OkHttpClient();
        get(url, client, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    populateModel(data);
                } else {
                    System.out.println("inside else");
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("Request Failed");
                e.printStackTrace();
            }
        });
    }

    public void populateModel(String response){
        List<StationItem> items = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray gasStations = jsonObject.getJSONArray("results");
            for(int i = 0; i < gasStations.length(); i++){
                JSONObject data = gasStations.getJSONObject(i).getJSONObject("geometry");
                String name = gasStations.getJSONObject(i).getString("name");
                double lat = data.getJSONObject("location").getDouble("lat");
                double lon = data.getJSONObject("location").getDouble("lng");
                float[] results = new float[10];
                Location.distanceBetween(currLat, currLon, lat, lon, results);
                double startToGas = (results[0] / 1609.344);

                float[] resultss = new float[10];
                Location.distanceBetween(lat, lon, destLat, destLon, resultss);
                double gasToDestination = (results[0] / 1609.344);

                float[] resultsss = new float[10];
                Location.distanceBetween(destLat, destLon, currLat, currLon, resultsss);
                double totalDistance = (results[0] / 1609.344);

                StationItem station = new StationItem(name, lat, lon, startToGas + gasToDestination - totalDistance);
                items.add(station);
            }
            Collections.sort(items);
            DummyContent.addItems(items);

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    GasStations.adapter.notifyDataSetChanged();
                }
            });

        }catch (JSONException err){
            System.out.println("err");
        }
    }

    Call get(String url, OkHttpClient client, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    public void onListFragmentInteraction(StationItem item) {
        DummyContent.clearItems();
        GasStations.adapter.notifyDataSetChanged();
        Intent intent = new Intent(this, readyMap.class);
        intent.putExtra("currLat", currLat);
        intent.putExtra("currLon", currLon);
        intent.putExtra("destLat", item.lat);
        intent.putExtra("destLon", item.lon);
        startActivity(intent);
        finish();
    }
}
