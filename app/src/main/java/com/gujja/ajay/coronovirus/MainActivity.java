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
//import android.icu.text.NumberFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView title, totalDeath, totalDeathNum, totalCases, totalCasesNum, totalRecovered, totalRecoveredData
            , activePatient, activePatientData;
    ProgressBar progressBar;
    EditText editText;

    ArrayList<CovidCountry> covidCountries;
    WorldDataAdapter mAdapter;

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

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Deploy_Test.class);
                MainActivity.this.startActivity(intent);
            }
        });

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

    private void filter(String country) {

        ArrayList<CovidCountry> filteredCountries = new ArrayList<>();

        for (CovidCountry item : covidCountries){
            if(item.getmCovidCountry().toLowerCase().contains(country.toLowerCase())){
                filteredCountries.add(item);
            }
        }

        RecyclerView WorldRecyclerView = findViewById(R.id.RecycleView);
        WorldRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        WorldDataAdapter worldDataAdapter = new WorldDataAdapter(MainActivity.this, covidCountries);
        WorldRecyclerView.setAdapter(worldDataAdapter);

        worldDataAdapter.filteredList(filteredCountries);

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
                            DecimalFormat decimalFormat = new DecimalFormat("##,##,##0");

//                            String recovered = data.getString("recovered");
//                            String active = data.getString("active");

                            JSONObject imgUrl  = data.getJSONObject("countryInfo");
                            Log.i(TAG, "onResponse: ajay" + imgUrl);

                            String cases = decimalFormat.format(Integer.parseInt(data.getString("cases")));
                            String country = data.getString("country");
                            String countryFlagURL = imgUrl.getString("flag");
                            Log.e(TAG, "onResponse: ajay"+ countryFlagURL );
                            String deaths = decimalFormat.format(Integer.parseInt(data.getString("deaths")));
                            covidCountries.add(new CovidCountry(country, cases,deaths,countryFlagURL));
                        }

                        Log.e(TAG, "onResponse: "+ covidCountries );
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

                    DecimalFormat decimalFormat = new DecimalFormat("##,##,##0");

                    String cases = decimalFormat.format(Integer.parseInt(jsonObject.getString("cases")));
                    String deaths = decimalFormat.format(Integer.parseInt(jsonObject.getString("deaths")));
                    String recovered = decimalFormat.format(Integer.parseInt(jsonObject.getString("recovered")));
                    String active = decimalFormat.format(Integer.parseInt(jsonObject.getString("active")));

/*                    String casesData = NumberFormat.getInstance().format(Integer.parseInt(jsonObject.getString("cases")));
                    String deaths = NumberFormat.getInstance().format(Integer.parseInt(jsonObject.getString("deaths")));
                    String recovered = NumberFormat.getInstance().format(Integer.parseInt(jsonObject.getString("recovered")));
                    String active = NumberFormat.getInstance().format(Integer.parseInt(jsonObject.getString("active")));*/
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

}
