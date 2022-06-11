package com.example.btl_nhom4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_nhom4.adapter.ViewPager2Adapter;
import com.example.btl_nhom4.api.ApiWeather;

import com.example.btl_nhom4.model.weather.OpenWeatherMap;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static double lat = 0, lon = 0;
    private static String API_KEY = "a1daceff8dd48f26306afa051bf0ae81";

    LocationManager locationManager;

    BottomNavigationView bottomNavigation;
    ViewPager2 viewPager2;
    TextView weather_temp, weather_city,windSpeed,humidity,weatherMain,weatherNation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.view_pager2);

        // set adapter cho viewpager2
        ViewPager2Adapter adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);

        //khi vuốt qua lại giữa các fragment thì các tab bottom navigation thay đổi tương ứng
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigation.getMenu().findItem(R.id.bottom_navigation_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigation.getMenu().findItem(R.id.bottom_navigation_project).setChecked(true);
                        break;
                    case 2:
                        bottomNavigation.getMenu().findItem(R.id.bottom_navigation_workplace).setChecked(true);
                        break;
                    case 3:
                        bottomNavigation.getMenu().findItem(R.id.bottom_navigation_account).setChecked(true);
                        break;
                }
            }
        });

        // khi click vào item tab bottom navigation thì call fragment tương ứng
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_navigation_home:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.bottom_navigation_project:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.bottom_navigation_workplace:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.bottom_navigation_account:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }

        getLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                CallAPiWeather();

            }
        },100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                TextView textView = findViewById(R.id.test);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleActivity();
                    }
                });
            }
        });


    }
    private void handleActivity(){
        Intent intent = new Intent(getApplicationContext(), WorkspaceActivityAdmin.class);
        startActivity(intent);
    }


    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,5, MainActivity.this);

        }catch (Exception e){
           e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }



    private  void CallAPiWeather(){

        ApiWeather.apiWeather.openWeatherMap(lat,lon,API_KEY).enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                OpenWeatherMap openWeatherMap = response.body();

                if(openWeatherMap != null){

                    ImageView imgWeather = findViewById(R.id.weather_icon);
                    weather_city = findViewById(R.id.weather_city);
                    weather_temp = findViewById(R.id.weather_temp);
                    weatherMain  = findViewById(R.id.weather_main);
                    weatherNation = findViewById(R.id.weather_nation);
                    windSpeed = findViewById(R.id.windSpeed);
                    humidity = findViewById(R.id.humidity);

                    Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/"+openWeatherMap.getWeather().get(0).getIcon()+"@2x.png").into(imgWeather);
                    weather_city.setText(openWeatherMap.getName());
                    weather_temp.setText(String.format("%.0f°C",(openWeatherMap.getMain().getTemp()- 273.15)));
                    weatherMain.setText(String.format("%s",openWeatherMap.getWeather().get(0).getDescription()));
                    humidity.setText(String.format("%s",openWeatherMap.getMain().getHumidity())+"%");
                    windSpeed.setText(String.format("%.0f km/h",(openWeatherMap.getWind().getSpeed()*1.852)));
                    weatherNation.setText(String.format("%s",openWeatherMap.getSys().getCountry()));


                }
            }
            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {
                Toast.makeText(MainActivity.this,"call api fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}