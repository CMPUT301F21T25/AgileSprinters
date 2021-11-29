package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * This class represents a google map fragment that allows thee user
 * to select a location for their event
 * @author Leen Alzebdeh
 */
public class MapsFragment extends DialogFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener {
    /**
     * This variable contains the tag used for the rest of the class (name of class)
     */
    private static final String TAG = MapsFragment.class.getSimpleName();
    /**
     * This variable contains a GoogleMap's instance
     */
    private GoogleMap map;
    /**
     * This variable contains an instance of the map's marker
     */
    Marker marker;
    /**
     * This variable contains the habit instance passed to the user
     */
    HabitInstance habitInstance;
    /**
     * This variable contains the entry point to the Fused Location Provider.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * This variable default location (Edmonton, Canada) and default zoom to use
     * when location permission is not granted.
     */
    private final LatLng defaultLocation = new LatLng(53.535913517980255,-113.50948255509138);
    /**
     * This variable contains the default zoom value
     */
    private static final int DEFAULT_ZOOM = 15;
    /**
     * This variable contains the num for request fine location code.
     */
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    /**
     * This variable is a boolean of whether or not permission is granted.
     */
    private boolean locationPermissionGranted;
    /**
     * This variable contains the last-known
     * location retrieved by the Fused Location Provider, thus where the device is currently.
     */
    private Location lastKnownLocation;
    /**
     * This variable is to store the ccamera state.
     */
    private static final String KEY_CAMERA_POSITION = "camera_position";
    /**
     * This variable is to store the location state.
     */
    private static final String KEY_LOCATION = "location";
    /**
     * This variable is a string of the location.
     */
    public String location;
    /**
     * Returns a map fragment that lets the user choose their location of a habit event
     *
     * @param  habitInstance the habit instance who's location will be determined
     */
    public static MapsFragment newInstance(HabitInstance habitInstance) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putSerializable("Habit instance", habitInstance);
        fragment.setArguments(args);

        return fragment;
    }
    /**
     * This function is called when the mapsfragment activity starts
     *
     * @param inflater   can be used to inflate any views in the fragment {@link LayoutInflater}
     * @param container  parent view that the fragment's UI should be attached to {@link ViewGroup}
     * @param savedInstanceState a reference to Bundle object that is passed into the onCreate method {@link Bundle } <br>
     *                           if null is passed an exception is thrown
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    /**
     * runs after the onCreateView
     * @param view The View returned by #onCreateView {@link View}
     * @param savedInstanceState a reference to Bundle object that is passed into the onCreate method {@link Bundle } <br>
     *      *                           if null is passed an exception is thrown
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        habitInstance = (HabitInstance) getArguments().getSerializable("Habit instance");
        // Get location from saved instance state
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        Places.initialize(getContext(), getString(R.string.MAPS_API_KEY));

        // Build a FusedLocationProviderClient for approximate location.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //place map in the app
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    /**
     * Handles saving data
     * @param savedInstanceState a reference to Bundle object that is passed into the onCreate method {@link Bundle } <br>
     *                                 if null is passed an exception is thrown
     */
    @Override
    public void onSaveInstanceState(@NonNull @Nullable Bundle savedInstanceState) {
        if (map != null) {
            savedInstanceState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            savedInstanceState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     *  Called when the map is ready to be used.
     *  Gets location permission and updates to the default of current location
     * @param map Googlemap to be used in the fragment {@link GoogleMap}
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        // Prompt the user for location permission
        getLocationPermission();

        // Turn on the control on the map and update its UI.
        updateLocationUI();

        // Get the current/ default location of the device and set the position of the map
        getDeviceLocation();
        map.setOnMarkerDragListener(this);

    }

    /**
     * called when the marker is being dragged
     * @param marker marker for the map {@link Marker}
     */
    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    /**
     *  called when the marker is done being dragged
     * @param marker marker for the map {@link Marker}.
     *               it sets the TexView to the location of the marker
     */
    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        TextView addressText = (TextView) getView().findViewById(R.id.etAddress);
        String address = (marker.getPosition().latitude +","+ marker.getPosition().longitude);
        addressText.setText(getDisplayLocStr(address));
    }

    /**
     *
     * @param marker marker for the map {@link Marker}
     */
    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    /**
     * Converts the longitude and latitude to a an address of city, state/ province, country
     * @param location
     * @return
     */
    public String getDisplayLocStr(String location){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        if (Objects.isNull(location) | location.equals("")) return "";

        List<Address> addresses = null;
        String[] latLng = location.split(",");

        //retrieve the city then state then country if possible
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]),1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] locations = new String[3];
        String returnStr = "";

        try {
            locations[0] = addresses.get(0).getLocality();
        }catch(Exception e){
            locations[0] = "";
        }
        try {
            locations[1] = addresses.get(0).getAdminArea();
        } catch(Exception e){
            locations[1] = "";
        }
        try {
            locations[2] = addresses.get(0).getCountryName();
        } catch(Exception e) {
            locations[2] = "";
        }
        for (int i = 0; i<locations.length;i++){
                System.out.println("location: "+locations[i]);
                if (locations[i]!=null | !locations[i].equals(""))
                    returnStr = returnStr + (locations[i] + ", ");
        }

        return returnStr.substring(0,returnStr.length()-2);
    }

    /**
     * called when the fragment comes to the foreground
     */
    @Override
    public void onResume() {
        super.onResume();
        Button saveAddressBtn = getView().findViewById(R.id.saveAddressBtn);
        //if the save button is pressed, st the habit event's location to the marker's location
        saveAddressBtn.setOnClickListener(view -> {
            location = (marker.getPosition().latitude + "," + marker.getPosition().longitude);
            habitInstance.setOptLoc(location);
            dismiss();
        });
    }

    /**
     * gets the most recent locatin of the device of permission is granted
     */
    private void getDeviceLocation() {

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            marker = map.addMarker(new MarkerOptions()
                                    .position(
                                            new LatLng(lastKnownLocation.getLatitude(),
                                                    lastKnownLocation.getLongitude()))
                                    .draggable(true).visible(true));
                        }
                        //if retrieving location is not successful, set to default
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * gets permission to the location of the user. The result  is handled by onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * this function receives the result of permission requests
     * @param requestCode  num of the  request code {@link int}
     * @param permissions  The requested permissions
     * @param grantResults  The grant results for the permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        //adds a set my location button if there's permission
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}