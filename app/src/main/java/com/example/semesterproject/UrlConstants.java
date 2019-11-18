package com.example.semesterproject;

public final class UrlConstants {
    public static final String YEARS_URL = "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getYears";

    public static String make_url(int year){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getMakes&year=" + year + "&sold_in_us=1";
    }

    public static String model_url(String make, int year){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModels&make=" + make + "&year=" + year + "&sold_in_us=1";
    }

    public static String trim_url(String make, String model, int year){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getTrims&make=" + make + "&year=" + year + "&model=" + model + "&sold_in_us=1&full_results=0";
    }

    public static String sumbit_url(String model_id){
        return "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModel&model=" + model_id;
    }


}
