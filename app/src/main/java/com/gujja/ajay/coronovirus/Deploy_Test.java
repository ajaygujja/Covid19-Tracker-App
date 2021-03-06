package com.gujja.ajay.coronovirus;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.gujja.ajay.coronovirus.MainActivity.COUNTRY_NAME;

public class Deploy_Test extends AppCompatActivity {


    private static final String TAG = "TAG";
    TextView clickedCountryDeath, clickCountryName, clickedCountryCases,
            clickedCountryRecovered, clickedCountryActive, clickedCountrySerious,
            clickedCountryNewCases, clickedCountryNewDeaths, updatedDate_Time ;
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
        clickedCountryNewCases = findViewById(R.id.ClickedCountryNewCases);
        clickedCountryNewDeaths = findViewById(R.id.ClickedCountryNewDeath);
        updatedDate_Time = findViewById(R.id.updateTime);

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
                            clickedCountryCases.setText(decimalFormat.format(Integer.parseInt(data.getString("cases"))));
                            clickedCountryDeath.setText(decimalFormat.format(Integer.parseInt(data.getString("deaths"))));
                            clickedCountryActive.setText(decimalFormat.format(Integer.parseInt(data.getString("active"))));
                            clickedCountryNewCases.setText(decimalFormat.format(Integer.parseInt(data.getString("todayCases"))));
                            clickedCountryNewDeaths.setText(decimalFormat.format(Integer.parseInt(data.getString("todayDeaths"))));
                            clickedCountryRecovered.setText(decimalFormat.format(Integer.parseInt(data.getString("recovered"))));
                            clickedCountrySerious.setText(decimalFormat.format(Integer.parseInt(data.getString("critical"))));

                            String updateTime = data.getString("updated").substring(0,10);

                            Log.e(TAG, "onResponse: " + updateTime );
                            String updatedAtText ="Updated at: "+  new SimpleDateFormat("hh:mm a dd-MMM ", Locale.ENGLISH).format(new Date(Long.parseLong(updateTime)*1000L));

                            updatedDate_Time.setText(updatedAtText);

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
