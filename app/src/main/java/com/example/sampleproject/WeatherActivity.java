package com.example.sampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class WeatherActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    String tempUrl2 = "";

    private final String url = "http://api.openweathermap.org/geo/1.0/direct";
    private final String url2 = "https://api.openweathermap.org/data/3.0/onecall?lat=";
    private final String appid = "16a7a39c759ca1d9fdeefae608527d15"; //API KEY NOT WORKING ON ONECALL API - NEEDS PAYMENT TO WORK!!!
    private static final String TAG = "WeatherActivity";
    private static RequestQueue requestQueue;
    String pattern = "#.##";
    DecimalFormat df = new DecimalFormat(pattern);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_weather);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);

        // swapping between windows
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.bottom_weather)
                return true;
            else if (item.getItemId() == R.id.bottom_calories) {
                startActivity(new Intent(getApplicationContext(), CaloriesActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_database) {
                startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_training) {
                startActivity(new Intent(getApplicationContext(), TrainingActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("") || country.equals("")) {
            tvResult.setText("Fields cannot be empty");
        } else {
            tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
        }
        MakeFirstAPIcall(tempUrl); // first API call to receive LAT and LON for location.
        MakeSecondAPIcall(url2);
    }

    private void MakeFirstAPIcall(String tempUrl) {
        StringRequest stringRequest = new StringRequest(tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                double latitude = 0;
                double longitude = 0;
                try {
                    // Parse the JSON array
                    JSONArray jsonArray = new JSONArray(response);
                    // Assuming there's at least one result in the array
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        latitude = jsonObject.getDouble("lat");
                        longitude = jsonObject.getDouble("lon");
                        Log.d("Latitude", String.valueOf(latitude));
                        Log.d("Longitude", String.valueOf(longitude));
                        tempUrl2 = url2 + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + appid;
                        MakeSecondAPIcall(tempUrl2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvResult.setText("Error parsing JSON");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    String errorMsg = new String(error.networkResponse.data);
                    Log.e(TAG, "Error: Status Code " + statusCode + " - " + errorMsg);
                } else {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }

        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void MakeSecondAPIcall(String tempUrl) {
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("WeatherResponse", response);
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelslike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonObjectSys.getString("city");
                    tvResult.setTextColor(Color.rgb(68,134,199));
                    output += "Current weather of : " + cityName + " (" + countryName + ")"
                            +"\n Temp : " + df.format(temp) + " C"
                            +"\n Feels like : " + df.format(feelslike) + " C"
                            +"\n Humidity : " + humidity + "%"
                            +"\n Description : " + description
                            +"\n Wind Speed : " + wind + "m/s"
                            +"\n Cloudiness : " + clouds + "%"
                            +"\n Pressure : " + pressure + "hPa";
                    tvResult.setText(output);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null) {
                    String statusCode = String.valueOf(error.networkResponse.statusCode);
                    String errorMsg = new String(error.networkResponse.data);
                    Log.e(TAG, "Error: Status Code " + statusCode + " - " + errorMsg);
                } else {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest1);
    }
}