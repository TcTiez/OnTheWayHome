package com.tctiez.onthewayhome.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Eugene J. Jeon on 2015-08-20.
 */
public class GPS implements LocationListener {
    private final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;     // 1 minute
    private final long MIN_DISTANCE_CHANGE_FR_UPDATES = 10;      // 10 miter

    private static GPS mInstance = null;
    public static GPS getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GPS(context);
        }
        return mInstance;
    }

    private Context         mContext            = null;
    private LocationManager mLocationManager    = null;

    private boolean         mIsNetworkEnabled   = false;
    private boolean         mIsGPSEnabled       = false;

    private Location        mLocation           = null;
    private double          mLatitude           = 0.0;
    private double          mLongitude          = 0.0;

    private GPS(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isGPSEnabled() {
        mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return mIsGPSEnabled;
    }

    public boolean isNetworkEnabled() {
        mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return mIsNetworkEnabled;
    }

    public Location getLocation() {
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {
                mLocationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FR_UPDATES, this);
                Location tLocation = mLocationManager.getLastKnownLocation(provider);
                if (tLocation == null) {
                    continue;
                }
                if (mLocation == null || tLocation.getAccuracy() < mLocation.getAccuracy()) {
                    mLocation = tLocation;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopGPS();
        }

        return mLocation;
    }

    /**
     * GPS 종료
     */
    public void stopGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(GPS.this);
        }
    }

    /**
     * 최근 위도 가져오기
     */
    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    /**
     * 최근 경도 가져오기
     */
    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }
        return mLongitude;
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}
