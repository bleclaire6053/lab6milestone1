package com.example.lab6milestone1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    //Milestone 2
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;

    //Bascom Hill
    private final LatLng mDestinationLatLng = new LatLng(43.0753, -89.4034);


    //private LatLng currDestinationLatLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Milestone 2
        //Obtain a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            //code to display marker
                googleMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Bascom Hall"));

                //Milestone 2
                displayMyLocation();

        });
    }
    //Milestone 2
    private void displayMyLocation() {
        //Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        //If not, ask for it
        if (permission== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    //issue here?
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        //If permission granted, display marker at current location
        else {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(this, task -> {
                            Location mLastKnownLocation = task.getResult();
                            if (task.isSuccessful() && mLastKnownLocation != null) {
                                mMap.addPolyline(new PolylineOptions().add(
                                        new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()),
                                        mDestinationLatLng));
                                mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())));

                                        }
                        });
        }


    }

    //Milestone 2
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            //If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }

    }
}