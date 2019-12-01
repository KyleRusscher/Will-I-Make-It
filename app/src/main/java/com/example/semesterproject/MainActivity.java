package com.example.semesterproject;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.semesterproject.select_vehicle_files.TrimSelection;
import com.example.semesterproject.select_vehicle_files.YearSelection;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    SeekBar gasBar;
    TextView ValueText;
    public static final int selection = 1;
    private boolean mLocationPermissionGranted = false;

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
