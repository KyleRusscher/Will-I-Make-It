package com.example.semesterproject;

public class StationItem {
    public final String name;
    public final Double distance;
    public final Double lat;
    public final Double lon;

    public StationItem(String name, Double lat, Double lon, Double distance) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return name;
    }
}