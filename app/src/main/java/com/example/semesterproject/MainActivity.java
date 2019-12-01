package com.example.semesterproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.semesterproject.select_vehicle_files.TrimSelection;
import com.example.semesterproject.select_vehicle_files.YearSelection;
import com.example.semesterproject.select_vehicle_files.dummy.YearContent;

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

public class MainActivity extends AppCompatActivity {
    SeekBar gasBar;
    TextView ValueText;

    EditText current;
    EditText destination;

    public static final int selection = 1;
    private boolean mLocationPermissionGranted = false;

    double currLat;
    double currLon;

    double destLat;
    double destLon;

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
        Button x = (Button)findViewById(R.id.selectVehicle);
        Button y = (Button)findViewById(R.id.SubmitButton);


        carname = findViewById(R.id.carName);
        carTrim = findViewById(R.id.carTrim);

        current = findViewById(R.id.CurrentText);
        destination = findViewById(R.id.DestinationText);

        current.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                getCoordinates(current.getText().toString(), true);
            }
        });
        destination.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                getCoordinates(destination.getText().toString(), false);
            }
        });

        gasBar = (SeekBar) findViewById(R.id.seekBar);
        ValueText = (TextView) findViewById(R.id.ValueText);
        gasBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar gasBar, int progress, boolean fromUser) {
                ValueText.setText(String.valueOf(progress));

                if(progress > 50){
                    y.setOnClickListener(e -> {
                        System.out.println("Button was clicked for map");
                        Intent intent = new Intent(MainActivity.this, readyMap.class);
                        intent.putExtra("currLat", currLat);
                        intent.putExtra("currLon", currLon);
                        intent.putExtra("destLat", destLat);
                        intent.putExtra("destLon", destLon);
                        startActivityForResult(intent, selection);
                    });
                }
                if (progress < 49){
                    System.out.println("Failure");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar gasBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar gasBar) {
            }
        });

        x.setOnClickListener(e -> {
            System.out.println("Button was clicked here");
            Intent yearIntent = new Intent(MainActivity.this, YearSelection.class);
            startActivityForResult(yearIntent, selection);
        });

        //        y.setOnClickListener(e -> {
//            System.out.println("Button was clicked for map");
//            Intent intent = new Intent(MainActivity.this, ReadyMap.class);
//            startActivityForResult(intent, selection);
//        });

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
