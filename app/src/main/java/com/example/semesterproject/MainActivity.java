package com.example.semesterproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final int selection = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button x = (Button)findViewById(R.id.selectVehicle);

        x.setOnClickListener(e -> {
            System.out.println("Button was clicked here");
            Intent intent = new Intent(MainActivity.this, SelectVehicle.class);
            startActivityForResult(intent, selection);
        });

    }


}
