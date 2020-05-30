package com.gujja.ajay.coronovirus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.gujja.ajay.coronovirus.MainActivity.COUNTRY_NAME;

public class Deploy_Test extends AppCompatActivity {


    private static final String TAG = "TAG";
    TextView clickedCountryDeath, clickCountryName, clickedCountryCases,clickedCountryRecovered, clickedCountryActive, clickedCountrySerious;
    CircleImageView clickedCountryFlag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy__test);

        clickCountryName = findViewById(R.id.ClickedCountryName);
        clickedCountryDeath = findViewById(R.id.ClickedCountryDeath);
        clickedCountryCases = findViewById(R.id.ClickedCountryCases);
        clickedCountryFlag = findViewById(R.id.ClickedCountryFlag);
        clickedCountryActive = findViewById(R.id.ClickedCountryActive);
        clickedCountryRecovered = findViewById(R.id.clickedCountryRecovered);
        clickedCountrySerious = findViewById(R.id.ClickedCountrySerious);

//        CovidCountry covidCountry = new CovidCountry()

        Intent intent = getIntent();
        String countryName = intent.getStringExtra(COUNTRY_NAME);

        Log.e(TAG, "onCreate: " + countryName);


        getClickedCountry(countryName);

    }

    private void getClickedCountry(final String countryName) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String URL = "https://disease.sh/v2/countries?yesterday=true&sort=cases&allowNull=false";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        if (data.getString("country").toLowerCase().contains(countryName.toLowerCase())) {

                            DecimalFormat decimalFormat = new DecimalFormat("##,##,##0");

                            clickCountryName.setText(data.getString("country"));
                            Log.e(TAG, "onResponse: 00"+ clickCountryName );
                            clickedCountryCases.setText(decimalFormat.format(Integer.parseInt(data.getString("cases"))));
                            clickedCountryDeath.setText(decimalFormat.format(Integer.parseInt(data.getString("deaths"))));
                            clickedCountryActive.setText(decimalFormat.format(Integer.parseInt(data.getString("active"))));
                            clickedCountryRecovered.setText(decimalFormat.format(Integer.parseInt(data.getString("recovered"))));
                            clickedCountrySerious.setText(decimalFormat.format(Integer.parseInt(data.getString("critical"))));

                            JSONObject imgUrl = data.getJSONObject("countryInfo");
                            String countryFlagUrl = imgUrl.getString("flag");

                            Picasso.get().load(countryFlagUrl).fit().centerInside().into(clickedCountryFlag);


                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: "+ error );
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }
}
