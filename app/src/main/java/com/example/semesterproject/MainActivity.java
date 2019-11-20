package com.example.semesterproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.semesterproject.select_vehicle_files.YearSelection;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final int selection = 1;

    String year_selected;
    String make_selected;
    String model_selected;
    String trim_selected;
    JSONObject vehicle_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button x = (Button)findViewById(R.id.selectVehicle);


        Intent intent = getIntent();
        year_selected = intent.getStringExtra("year_selected");
        make_selected = intent.getStringExtra("make_selected");
        model_selected = intent.getStringExtra("model_selected");
        trim_selected = intent.getStringExtra("trim_selected");

        System.out.println(year_selected);
        System.out.println(make_selected);
        System.out.println(model_selected);
        System.out.println(trim_selected);

        x.setOnClickListener(e -> {
            System.out.println("Button was clicked here");
            Intent yearIntent = new Intent(MainActivity.this, YearSelection.class);
            startActivityForResult(yearIntent, selection);
        });

    }


}
