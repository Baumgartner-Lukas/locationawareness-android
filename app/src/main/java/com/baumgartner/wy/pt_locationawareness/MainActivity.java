package com.baumgartner.wy.pt_locationawareness;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baumgartner.wy.pt_locationawareness.location.LocationTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 255;
    private static final String TAG = MainActivity.class.getName();

    private Location mLocation;
    private LocationTracker mLocationTracker;

    @BindView(R.id.cityEditText)
    AutoCompleteTextView mCityEditText;
    @BindView(R.id.cityLabel)
    TextView mCityLabel;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.refreshImageView)
    ImageView mRefreshImageView;

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mLocation = mLocationTracker.getLocation();
                mCityLabel.setText(convertLocation(mLocation));
                mRefreshImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        if (!hasPermission()) {
            requestPermissions();
        } else {
            mLocationTracker = new LocationTracker(this);
            mLocation = mLocationTracker.getLocation();
            if (mLocation == null) {
                Toast.makeText(this, "Bitte Standortdienste aktivieren", Toast.LENGTH_SHORT).show();
            } else {
                mCityLabel.setText(convertLocation(mLocation));
            }
        }
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
}
