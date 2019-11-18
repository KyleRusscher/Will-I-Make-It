package com.example.semesterproject;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.semesterproject.UrlConstants.*;

public class SelectVehicle extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);


        Spinner make_spinner = (Spinner) findViewById(R.id.model_data);
        Spinner year_spinner = (Spinner) findViewById(R.id.year_data);

        populateSpinners(YEARS_URL, year_spinner);

        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int year_selected = (int) adapterView.getItemAtPosition(position);
                populateSpinners(make_url(year_selected), make_spinner);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }





    private void populateSpinners(String url, Spinner spinner){
        get(url, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if(url == YEARS_URL){
                        setYearResult(response.body().string(), spinner);
                    } else {
                        setSpinner(response.body().string(), spinner);
                    }

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

    private void setSpinner(String response, Spinner spinner){
        List<String> spinnerArray =  new ArrayList<>();
        try{
            String str = response.substring(2, response.length() - 2);
            JSONArray jsonArr = new JSONObject(str).getJSONArray("Makes");
            for(int i = 0; i < jsonArr.length(); i++){
                JSONObject obj = (JSONObject)jsonArr.get(i);
                spinnerArray.add(obj.getString("make_id"));
            }
        } catch (JSONException err){
            System.out.println("err");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Spinner sItems = (Spinner) findViewById(R.id.year_data);
                spinner.setAdapter(adapter);
            }
        });

    }

    private void setYearResult(String response, Spinner spinner){
        int startYear = 1980;
        int endYear = 2019;
        try {
            String str = response.substring(2, response.length() - 2);
            JSONObject jsonObject = new JSONObject(str);
            startYear = Integer.parseInt(jsonObject.getJSONObject("Years").getString("min_year"));
            endYear = Integer.parseInt(jsonObject.getJSONObject("Years").getString("max_year"));
        }catch (JSONException err){
            System.out.println("err");
        }
        List<Integer> spinnerArray =  new ArrayList<>();
        for(int i = endYear; i >= startYear; --i){
            spinnerArray.add(i);
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Spinner sItems = (Spinner) findViewById(R.id.year_data);
                spinner.setAdapter(adapter);
            }
        });
    }

    Call get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }




}
