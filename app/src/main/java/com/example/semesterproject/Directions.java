package com.example.semesterproject;

import android.content.Intent;
import android.os.Bundle;

import com.example.semesterproject.dummy.DirectionContent;
import com.example.semesterproject.select_gas_station.GasStations;
import com.example.semesterproject.select_gas_station.MystationRecyclerViewAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.semesterproject.dummy.DirectionContent.*;

public class Directions extends AppCompatActivity implements directionFragment.OnListFragmentInteractionListener {

    public static MydirectionRecyclerViewAdapter adapter = null;

    double currLat;
    double currLon;

    double destLat;
    double destLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        currLat = intent.getDoubleExtra("currLat", 0);
        currLon = intent.getDoubleExtra("currLon", 0);
        destLat = intent.getDoubleExtra("destLat", 0);
        destLon = intent.getDoubleExtra("destLon", 0);

        getDirections(currLat, currLon, destLat, destLon);
    }



    private void getDirections(double currLat, double currLon, double destLat, double destLon) {
        String url = UrlConstants.get_directions(currLat, currLon, destLat, destLon);
        OkHttpClient client = new OkHttpClient();
        get(url, client, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    populateDirections(data);
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

    public void populateDirections(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray directions = jsonObject.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            System.out.println("xd");
            for(int i = 0; i < directions.length(); i++){
                String distance = directions.getJSONObject(i).getJSONObject("distance").getString("text");
                String instructions = directions.getJSONObject(i).getString("html_instructions");
                String html = "<.>";
                String html2 = "<..>";
                instructions=instructions.replaceAll(html," ");
                instructions=instructions.replaceAll(html2," ");
                addItem(distance, instructions);
            }
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }catch (JSONException err){
            System.out.println("this was not a valid location");
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
    public void onListFragmentInteraction(DirectionItem item) {

    }
}
