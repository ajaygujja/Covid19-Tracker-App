package com.gujja.ajay.coronovirus;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.icu.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements WorldDataAdapter.OnItemClickListener {

    public static final String COUNTRY_NAME = "countryName";
    private static final String TAG = MainActivity.class.getSimpleName();

    TextView title, totalDeath, totalDeathNum, totalCases, totalCasesNum, totalRecovered, totalRecoveredData, activePatient, activePatientData;
    ProgressBar progressBar;
    EditText editText;

    ArrayList<CovidCountry> covidCountries;
    RecyclerView WorldRecycleView;
    WorldDataAdapter worldDataAdapter;
    RecyclerView.LayoutManager mlayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        title = findViewById(R.id.Title);
        totalCases = findViewById(R.id.TotalCases);
        totalCasesNum = findViewById(R.id.TotalCasesData);
        totalDeath = findViewById(R.id.TotalDeath);
        totalDeathNum = findViewById(R.id.TotalDeathData);
        progressBar = findViewById(R.id.Progressloader);
        totalRecovered = findViewById(R.id.TotalRecovered);
        totalRecoveredData = findViewById(R.id.TotalRecoveredData);
        activePatient = findViewById(R.id.TotalActive);
        activePatientData = findViewById(R.id.TotalActiveData);
        editText = findViewById(R.id.search_country);


        getData();
        detDataFromServer();
        buildRecycleView();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void buildRecycleView() {
        WorldRecycleView = findViewById(R.id.RecycleView);
        WorldRecycleView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(this);
        worldDataAdapter = new WorldDataAdapter(this,covidCountries);

        WorldRecycleView.setLayoutManager(mlayoutManager);
        WorldRecycleView.setAdapter(worldDataAdapter);
    }

    private void filter(String country) {
        ArrayList<CovidCountry> filteredCountries = new ArrayList<>();

        for(CovidCountry item: covidCountries){
            if (item.getmCovidCountry().toLowerCase().contains(country.toLowerCase())) {

                filteredCountries.add(item);
            }
        }

        worldDataAdapter.filteredList(filteredCountries);

    }

    private void detDataFromServer() {

        String url = "https://disease.sh/v2/countries?yesterday=true&sort=cases&allowNull=false";

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
                            DecimalFormat decimalFormat = new DecimalFormat("##,##,##0");

//                            String recovered = data.getString("recovered");
//                            String active = data.getString("active");

                            JSONObject imgUrl = data.getJSONObject("countryInfo");


                            String cases = decimalFormat.format(Integer.parseInt(data.getString("cases")));
                            String new_cases = decimalFormat.format(Integer.parseInt(data.getString("todayCases")));
                            String country = data.getString("country");
                            String countryFlagURL = imgUrl.getString("flag");

                            String deaths = decimalFormat.format(Integer.parseInt(data.getString("deaths")));

                            covidCountries.add(new CovidCountry(country, cases, deaths, countryFlagURL,new_cases));
                        }

                        Log.e(TAG, "onResponse: " + covidCountries);

                        WorldRecycleView.setAdapter(worldDataAdapter);
                        worldDataAdapter.setOnItemClickListener(MainActivity.this);

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

                    DecimalFormat decimalFormat = new DecimalFormat("##,##,##0");

                    String cases = decimalFormat.format(Integer.parseInt(jsonObject.getString("cases")));
                    String deaths = decimalFormat.format(Integer.parseInt(jsonObject.getString("deaths")));
                    String recovered = decimalFormat.format(Integer.parseInt(jsonObject.getString("recovered")));
                    String active = decimalFormat.format(Integer.parseInt(jsonObject.getString("active")));

                    totalCasesNum.setText(cases);
                    totalDeathNum.setText(deaths);
                    totalRecoveredData.setText(recovered);
                    activePatientData.setText(active);

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

    @Override
    public void onItemClick(int position) {
        Intent  detailIntent = new Intent(this, Deploy_Test.class);
        CovidCountry detailCountry = covidCountries.get(position);

        detailIntent.putExtra(COUNTRY_NAME,detailCountry.getmCovidCountry());
        Log.e(TAG, "onItemClick: " + covidCountries.get(position) );

        startActivity(detailIntent);
    }
}
