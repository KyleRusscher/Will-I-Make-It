package com.example.semesterproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.location.Location;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.semesterproject.select_gas_station.GasStations;
import com.example.semesterproject.select_gas_station.dummy.GasStationContent;
import com.example.semesterproject.select_vehicle_files.YearSelection;
import com.google.android.material.snackbar.Snackbar;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    SeekBar gasBar;
    TextView ValueText;

    EditText current;
    EditText destination;

    public static final int selection = 1;

    Double currLat;
    Double currLon;

    Double destLat;
    Double destLon;

    String data;
    String make;
    String name;
    String trim;
    String year;
    String mpg;
    String capacity;
    TextView carname;
    TextView carTrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button selectVehicle = (Button) findViewById(R.id.selectVehicle);
        Button submit = (Button) findViewById(R.id.SubmitButton);


        carname = findViewById(R.id.carName);
        carTrim = findViewById(R.id.carTrim);

        current = findViewById(R.id.CurrentText);
        destination = findViewById(R.id.DestinationText);



        current.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                getCoordinates(current.getText().toString(), true);
            }
        });
        destination.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                getCoordinates(destination.getText().toString(), false);
            }
        });

        submit.setOnClickListener(e -> {
            System.out.println("Button was clicked for map");
            if(isVehicleSelected()){
                Snackbar.make(e, "Please select a vehicle", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if(locationsNotSelected()){
                Snackbar.make(e, "Not a valid location", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else if(isWillMakeIt()){
                Intent intent = new Intent(MainActivity.this, readyMap.class);
                intent.putExtra("currLat", currLat);
                intent.putExtra("currLon", currLon);
                intent.putExtra("destLat", destLat);
                intent.putExtra("destLon", destLon);
                startActivityForResult(intent, selection);
            } else {
                GasStationContent.clearItems();
                double percentInTank = gasBar.getProgress() / 100.0;
                Intent intent = new Intent(MainActivity.this, GasStations.class);
                intent.putExtra("currLat", currLat);
                intent.putExtra("currLon", currLon);
                intent.putExtra("destLat", destLat);
                intent.putExtra("destLon", destLon);
                intent.putExtra("distance", Double.parseDouble(capacity) * percentInTank * Double.parseDouble(mpg));
                startActivityForResult(intent, selection);
//                Double.parseDouble(capacity) * percentInTank * Double.parseDouble(mpg)
            }
        });



        gasBar = (SeekBar) findViewById(R.id.seekBar);
        ValueText = (TextView) findViewById(R.id.ValueText);
        gasBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar gasBar, int progress, boolean fromUser) {
                ValueText.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar gasBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar gasBar) {
            }
        });

        selectVehicle.setOnClickListener(e -> {
            System.out.println("Button was clicked here");
            Intent yearIntent = new Intent(MainActivity.this, YearSelection.class);
            startActivityForResult(yearIntent, selection);
        });

    }

    private boolean locationsNotSelected() {
        return (destLon == null || destLat == null || currLat == null || currLon == null);
    }

    private boolean isWillMakeIt() {
        float[] results = new float[10];
        Location.distanceBetween(currLat, currLon, destLat, destLon, results);
        double distCalc = (results[0] / 1609.344);
        double percentInTank = gasBar.getProgress() / 100.0;
        System.out.println(distCalc - Double.parseDouble(capacity) * percentInTank * Double.parseDouble(mpg) < 0);
        return distCalc - Double.parseDouble(capacity) * percentInTank * Double.parseDouble(mpg) < 0;
    }

    private boolean isVehicleSelected() {
        return !(capacity != null && mpg != null && trim != null);
    }


    private void getCoordinates(String location, boolean isCurrent) {
        String url = UrlConstants.get_coordinates(location);
        OkHttpClient client = new OkHttpClient();
        get(url, client, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    populateLatLon(data, isCurrent);
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

    public void populateLatLon(String response, boolean isCurrent){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject location = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            if(isCurrent){
                currLat = location.getDouble("lat");
                currLon = location.getDouble("lng");
            } else {
                destLat = location.getDouble("lat");
                destLon = location.getDouble("lng");
            }


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
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        make = intent.getStringExtra("make_id");
        name = intent.getStringExtra("name");
        trim = intent.getStringExtra("trim");
        year = intent.getStringExtra("year");
        mpg = intent.getStringExtra("mpg");
        capacity = intent.getStringExtra("capacity");

        carname.setText(String.format("%s %s", year, name));
        carTrim.setText(trim);

        System.out.println(capacity);
    }
}