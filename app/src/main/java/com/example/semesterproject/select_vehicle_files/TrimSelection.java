package com.example.semesterproject.select_vehicle_files;

import android.content.Intent;
import android.os.Bundle;

import com.example.semesterproject.MainActivity;
import com.example.semesterproject.UrlConstants;
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
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrimSelection extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    String year_selected;
    String make_selected;
    String model_selected;

    HashMap<String, String> trimToId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        year_selected = intent.getStringExtra("year_selected");
        make_selected = intent.getStringExtra("make_selected");
        model_selected = intent.getStringExtra("model_selected");

        getTrims(UrlConstants.trim_url(make_selected, model_selected, year_selected));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onListFragmentInteraction(YearContent.YearItem item) {
        YearContent.clearItems();
        YearSelection.adapter.notifyDataSetChanged();
        Intent returnToMain = new Intent(this, MainActivity.class);
        returnToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        submit(returnToMain, UrlConstants.sumbit_url(trimToId.get(item.year)));

    }

    private void submit(Intent returnToMain, String url){
        OkHttpClient client = new OkHttpClient();
        get(url, client, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responses = response.body().string();
                    String str = responses.substring(2, responses.length() - 2);
                    System.out.println(str);
//                    returnToMain.putExtra("data", str);
                    try {
                        JSONArray obj = new JSONArray(str);
                        JSONObject dataOBJ = obj.getJSONObject(0);
                        returnToMain.putExtra("make_id", dataOBJ.getString("model_make_id"));
                        returnToMain.putExtra("name", dataOBJ.getString("model_name"));
                        returnToMain.putExtra("trim", dataOBJ.getString("model_trim"));
                        returnToMain.putExtra("year", dataOBJ.getString("model_year"));
                        returnToMain.putExtra("mpg", dataOBJ.getString("model_mpg_hwy"));
                        returnToMain.putExtra("capacity", dataOBJ.getString("model_fuel_cap_g"));
                    } catch (JSONException e) {
                        System.out.println(e);
                    }
                    startActivity(returnToMain);
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

    private void getTrims(String url){
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
        try {

            String str = response.substring(2, response.length() - 2);
            JSONObject jsonObject = new JSONObject(str);
            JSONArray makes = jsonObject.getJSONArray("Trims");
            for(int i = 0; i < makes.length(); i++){
                String make = makes.getJSONObject(i).getString("model_trim");
                String make_id = makes.getJSONObject(i).getString("model_id");
                if(make.isEmpty()){
                    make = "Base";
                }
                trimToId.put(make, make_id);
                YearContent.addItem(make);
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    YearSelection.adapter.notifyDataSetChanged();
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
}
