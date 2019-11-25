package com.example.semesterproject.select_vehicle_files;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.semesterproject.R;
import com.example.semesterproject.select_vehicle_files.dummy.YearContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import static com.example.semesterproject.UrlConstants.YEARS_URL;

public class YearSelection extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    public static ItemRecyclerViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_selection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        YearSelection.adapter.notifyDataSetChanged();
                    }
                });
                System.out.println(adapter.getItemCount());
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YearContent.clearItems();
        adapter.notifyDataSetChanged();
        getData(YEARS_URL);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListFragmentInteraction(YearContent.YearItem item) {
        YearContent.clearItems();
        adapter.notifyDataSetChanged();
        Intent toModel = new Intent(this, MakeSelection.class);
        toModel.putExtra("year_selected", item.year);
        startActivity(toModel);
    }


    private void getData(String url){
        OkHttpClient client = new OkHttpClient();
        get(url, client, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    populateModel(data);
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

    public void populateModel(String response){
        try {

            String str = response.substring(2, response.length() - 2);
            JSONObject jsonObject = new JSONObject(str);
            int startYear = Integer.parseInt(jsonObject.getJSONObject("Years").getString("min_year"));
            int endYear = Integer.parseInt(jsonObject.getJSONObject("Years").getString("max_year"));
            for(int i = endYear; i >= startYear; --i){
                YearContent.addItem(Integer.toString(i));
            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    YearSelection.adapter.notifyDataSetChanged();
                }
            });
            
        }catch (JSONException err){
            System.out.println("err");
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
}
