package com.example.semesterproject;

public final class UrlConstants {
    public static final String YEARS_URL = "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getYears";

    public static String make_url(String year){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getMakes&year=" + year + "&sold_in_us=1";
    }

    public static String model_url(String make, String year){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModels&make=" + make + "&year=" + year + "&sold_in_us=1";
    }

    public static String trim_url(String make, String model, String year){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getTrims&make=" + make + "&year=" + year + "&model=" + model + "&sold_in_us=1&full_results=0";
    }

    public static String sumbit_url(String model_id){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModel&model=" + model_id;
    }

    public static String get_coordinates(String location){
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyDFPQRVi8iAfiA12WIxWv6Jj8Wt7knoKmM";
    }

    public static String get_gas_stations(String lat, String lon, double radius){
        String inMeters = Double.toString(radius * 1609.34);
        return  String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                "%s,%s&radius=%s&types=gas_station&key=AIzaSyDFPQRVi8iAfiA12WIxWv6Jj8Wt7knoKmM&rankBy=distance", lat, lon, inMeters);
    }

    public static String get_directions(double currLat, double currLon, double destLat, double destLon){
        return "https://maps.googleapis.com/maps/api/directions/json?origin="+ currLat + "," + currLon +
                "&destination=" + destLat + "," + destLon + "&key=AIzaSyDFPQRVi8iAfiA12WIxWv6Jj8Wt7knoKmM";
    }



}