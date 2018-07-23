package com.baumgartner.wy.pt_locationawareness;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST_CODE = 255;

    private FusedLocationProviderClient mFusedLocationClient;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkPermission()){
            requestPermissions();
        }else{
            retrieveLastLocation();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void retrieveLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            mLocation = task.getResult();

                            mLatitude.setText("" + mLocation.getLatitude());
                            mLongitude.setText("" + mLocation.getLongitude());
                        }else{
                            return;
                        }
                    }
                });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }
}
