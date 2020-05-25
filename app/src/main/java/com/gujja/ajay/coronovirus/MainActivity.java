package com.gujja.ajay.coronovirus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    TextView totalDeath, totalDeathNum, totalCases, totalCasesNum;
    ProgressBar progressBar;
    ArrayList<CovidCountry> covidCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalCases = findViewById(R.id.TotalCases);
        totalCasesNum = findViewById(R.id.TotalCasesData);
        totalDeath = findViewById(R.id.TotalDeath);
        totalDeathNum = findViewById(R.id.TotalDeathData);
        progressBar = findViewById(R.id.Progressloader);

        getData();
        detDataFromServer();
    }

    private void detDataFromServer() {

        String url = "https://disease.sh/v2/countries?yesterday=false&sort=cases&allowNull=false";

        covidCountries = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                if (response != null) {

                    Log.e(TAG, "onResponse: " + response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String cases = data.getString("cases");
                            String country = data.getString("country");
                            covidCountries.add(new CovidCountry(country, cases));
                        }

                        RecyclerView WorldRecyclerView = findViewById(R.id.RecycleView);
                        WorldRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        WorldDataAdapter worldDataAdapter = new WorldDataAdapter(MainActivity.this, covidCountries);
                        WorldRecyclerView.setAdapter(worldDataAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onErrorResponse: " + error);

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void getData() {
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://corona.lmao.ninja/v2/all";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    totalCasesNum.setText(jsonObject.getString("cases"));
                    totalDeathNum.setText(jsonObject.getString("deaths"));

                    Log.i("TAG", "onResponse: " + jsonObject.getString("cases") + "Death: "
                            + jsonObject.getString("deaths"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.d("Error response", error.toString());

            }
        });
        queue.add(stringRequest);


    }


}
