package com.example.semesterproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.semesterproject.dummy.DirectionContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class readyMap extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline currentPoly;
    Button getDirection;
    EditText CurrentLoc, destinationLoc;
    Double CalculatedDistance;

    double currLat;
    double currLon;

    double destLat;
    double destLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_map);
        getDirection = findViewById(R.id.directionButton);

        Intent intent = getIntent();
        currLat = intent.getDoubleExtra("currLat", 0);
        currLon = intent.getDoubleExtra("currLon", 0);
        destLat = intent.getDoubleExtra("destLat", 0);
        destLon = intent.getDoubleExtra("destLon", 0);


        CurrentLoc = (EditText) findViewById(R.id.CurrentText);
        destinationLoc = (EditText) findViewById(R.id.DestinationText);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        place1 = new MarkerOptions().position(new LatLng(currLat, currLon)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(destLat, destLon)).title("Location 2");
        new FetchURL(readyMap.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectionContent.clearItems();
                Intent intent = new Intent(readyMap.this, Directions.class);
                intent.putExtra("currLat", currLat);
                intent.putExtra("currLon", currLon);
                intent.putExtra("destLat", destLat);
                intent.putExtra("destLon", destLon);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currLat, currLon), 12.0f));

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }




    @Override
    public void onTaskDone(Object... values) {
        if(currentPoly != null)
            currentPoly.remove();
        currentPoly = mMap.addPolyline((PolylineOptions) values[0]);
    }

}

