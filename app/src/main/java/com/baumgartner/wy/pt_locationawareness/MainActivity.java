package com.baumgartner.wy.pt_locationawareness;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baumgartner.wy.pt_locationawareness.location.LocationTracker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 255;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mLocation;

    @BindView(R.id.latitudeLabel)
    TextView mLatitude;
    @BindView(R.id.longitudeLabel)
    TextView mLongitude;
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
        if (!hasPermission()) {
            requestPermissions();
        } else {

            LocationTracker locationTracker = new LocationTracker(this);
            mLocation = locationTracker.getLocation();
            if (mLocation == null) {
                mLatitude.setText("-1");
                mLongitude.setText("-1");
            } else {
                mLatitude.setText(mLocation.getLatitude() + "");
                mLongitude.setText(mLocation.getLongitude() + "");
            }
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET},
                PERMISSION_REQUEST_CODE);
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }
}
