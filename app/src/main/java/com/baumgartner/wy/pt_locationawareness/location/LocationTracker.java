package com.baumgartner.wy.pt_locationawareness.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class LocationTracker extends Service implements LocationListener {
    protected LocationManager locationManager;
    private Location location;

    public static final int MIN_DISTANCE_FOR_UPDATES = 0;
    public static final int MIN_TIME_FOR_UPDATES = 0;

    public LocationTracker(Context context){
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    @SuppressWarnings("MissingPermission")
    public Location getLocation(){
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_FOR_UPDATES,
                    MIN_DISTANCE_FOR_UPDATES,
                    this);
            if(locationManager != null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return location;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
