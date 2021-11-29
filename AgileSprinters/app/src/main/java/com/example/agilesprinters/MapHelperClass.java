package com.example.agilesprinters;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.Serializable;

/**
 * This Class helps with getting and setting the permission setting for accessing the location.
 *
 * @author  Hari Bheesetti
 */
public class MapHelperClass implements Serializable {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    /**
     * This function returns the value of locationPermissionGranted
     *
     * @return {@link Boolean}
     */
    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    /**
     * This function sets teh value passed in to locationPermissionGranted
     *
     * @param locationPermissionGranted {@link Boolean} permission setting, if granted true else false
     */
    public void setLocationPermissionGranted(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
    }

    /**
     * gets permission to the location of the user. The result  is handled by onRequestPermissionsResult.
     *
     * @param context  {@link Context} context of the activity that requested for locaation.
     * @param activity {@link Activity} activity that requested for location.
     */
    public void getLocationPermission(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            setLocationPermissionGranted(true);
            System.out.println("permissonSEt");
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * this function receives the result of permission requests
     *
     * @param requestCode  num of the  request code {@link int}
     * @param permissions  The requested permissions
     * @param grantResults The grant results for the permissions
     */
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        setLocationPermissionGranted(false);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocationPermissionGranted(true);
                }
            }
        }
    }
}
