package com.baumgartner.wy.pt_locationawareness;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baumgartner.wy.pt_locationawareness.adapter.DayAdapter;
import com.baumgartner.wy.pt_locationawareness.fragments.ViewPagerFragment;
import com.baumgartner.wy.pt_locationawareness.location.LocationTracker;
import com.baumgartner.wy.pt_locationawareness.weather.Current;
import com.baumgartner.wy.pt_locationawareness.weather.Day;
import com.baumgartner.wy.pt_locationawareness.weather.Forecast;
import com.baumgartner.wy.pt_locationawareness.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 255;
    private static final String TAG = MainActivity.class.getName();
    private static final String API_KEY = "3b1fc12bc308575b8d39aa31530b3131";
    private static final String FORECAST_URL = "https://api.darksky.net/forecast/";

    private Location mLocation;
    private LocationTracker mLocationTracker;
    private Forecast mForecast;
    private Day[] mDays;
    private Hour[] mHours;

    @BindView(R.id.cityLabel)
    TextView mCityLabel;
    @BindView(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @BindView(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @BindView(R.id.summaryLabel)
    TextView mSummaryLabel;
    @BindView(R.id.iconImageView)
    ImageView mIconImageView;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    @Override
    @SuppressWarnings("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mLocationTracker = new LocationTracker(this);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.setHasFixedSize(true);

        while (!hasPermission()) {
            requestPermissions();
        }


        try {
            if (isNetworkAvailable()) {
                mLocation = mLocationTracker.getLocation();
                getForecast(mLocation);
                mCityLabel.setText(convertLocation(mLocation));
            } else {
                Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (isNetworkAvailable()) {
                        mLocation = mLocationTracker.getLocation();
                        getForecast(mLocation);
                        mCityLabel.setText(convertLocation(mLocation));
                    } else {
                        Toast.makeText(MainActivity.this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getForecast(Location mLocation) {
        String url = FORECAST_URL + API_KEY + "/" +
                mLocation.getLatitude() +
                "," + mLocation.getLongitude() +
                "?lang=" + Locale.getDefault().getLanguage();
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //toggleRefresh();
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + "");
        mSummaryLabel.setText(current.getSummary());

        Drawable drawable = ContextCompat.getDrawable(this, current.getIconId());
        mIconImageView.setImageDrawable(drawable);

        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.placeholder, viewPagerFragment);
        fragmentTransaction.commit();

//        DayAdapter adapter = new DayAdapter(this, mDays);
//        mRecyclerView.setAdapter(adapter);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));

        return forecast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        mHours = new Hour[data.length()];

        for(int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);

            mHours[i] = hour;
        }
        return mHours;
    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");
        mDays = new Day[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);

            mDays[i] = day;
        }

        return mDays;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPercipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());
        return current;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private String convertLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.GERMANY);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = addresses.get(0).getLocality();
        String subAdminArea = addresses.get(0).getSubAdminArea();

        if (city == null) {
            return subAdminArea;
        }
        return city;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();

        return info != null && info.isConnected() && lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermission()) {
            requestPermissions();
        } else {
            mLocationTracker.getLocation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationTracker.stop();
        Log.i(TAG, "onStop works");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationTracker.stop();
        Log.i(TAG, "onPause works");
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET},
                PERMISSION_REQUEST_CODE);
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public Day[] getDays() {
        return mDays;
    }

    public Hour[] getHours(){
        return mHours;
    }
}
