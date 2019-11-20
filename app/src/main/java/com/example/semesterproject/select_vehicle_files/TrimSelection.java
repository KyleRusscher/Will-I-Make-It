package com.example.semesterproject.select_vehicle_files;

import android.content.Intent;
import android.os.Bundle;

import com.example.semesterproject.MainActivity;
import com.example.semesterproject.select_vehicle_files.dummy.YearContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.semesterproject.R;

public class TrimSelection extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    String year_selected;
    String make_selected;
    String model_selected;

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

        System.out.println(year_selected);
        System.out.println(make_selected);
        System.out.println(model_selected);
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
        Intent returnToMain = new Intent(this, MainActivity.class);
        returnToMain.putExtra("year_selected", year_selected);
        returnToMain.putExtra("make_selected", make_selected);
        returnToMain.putExtra("model_selected", model_selected);
        returnToMain.putExtra("trim_selected", item.year);
        returnToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(returnToMain);
    }
}
