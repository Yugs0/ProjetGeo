package com.example.hugo.projetgeo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.location.LocationListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    SupportMapFragment map;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation;
    private float DEFAULT_ZOOM = 17.0f; //zoom min : 2, zoom max : 21
    private String TAG = "PROJET GEO";
    LocationListener locationListener;
    LocationManager locationManager;
    private boolean isMovingCameraWithFinger;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //set the default location to Ludus Brussels
        mDefaultLocation = new LatLng(50.8571393, 4.348130800000035);

        //instanciate the location listener to be able to update our currentLocation
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng newPos = new LatLng(location.getLatitude(),location.getLongitude());
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(newPos));
                //Log.e(TAG, "LOCATION CHANGED");
                //centerOnDeviceLocation();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                centerOnDeviceLocation();
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"PERMISSION GRANTED");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 100, locationListener);
                centerOnDeviceLocation();
            }
        }

        final Context context = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerOnDeviceLocation();
                Intent intent = new Intent(context,AddLocation.class);
                intent.putExtra("lat", (mLastKnownLocation.getLatitude()));
                intent.putExtra("lng", (mLastKnownLocation.getLongitude()));
                context.startActivity(intent);
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng kampala = new LatLng(0.347596, 32.582520);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(kampala).title("Da wei"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        enableMyLocation();
        centerOnDeviceLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
               @Override
               public void onMapClick(final LatLng clickCoords) {
                   Log.e(TAG, "LE LISTENER MARCHE");
               }
           }
        );



        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();


        mMap.clear();
        ArrayList<CustomMarker> tabMarkers = dbHelper.getAllMarkers(db);
        for (CustomMarker marker:tabMarkers) {
            LatLng coordonnees = new LatLng(marker.getLatitude(),marker.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(coordonnees)
                    .title(marker.getName())
                    .snippet(marker.getComments())
                    .visible(true)
            );
        }


    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mLocationPermissionGranted = true;
        }
    }
        //LatLng devicePosition = new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());

    private void centerOnDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */


        try {
            if (mLocationPermissionGranted) {
                //if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                //}
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task){
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            if(!isMovingCameraWithFinger) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                Log.e(TAG,"Last known location found");
                            }
                        } else {
                            Log.d(TAG,  "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

        if (mLastKnownLocation == null){
            Log.e(TAG,"last location unknown");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"Permission Granted");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0f, locationListener);
        }

        centerOnDeviceLocation();

        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        if(mMap != null){
            mMap.clear();
            ArrayList<CustomMarker> tabMarkers = dbHelper.getAllMarkers(db);
            for (CustomMarker marker:tabMarkers) {
                LatLng coordonnees = new LatLng(marker.getLatitude(),marker.getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(coordonnees)
                        .title(marker.getName())
                        .snippet(marker.getComments())
                        .visible(true)
                );
            }
        }


    }

}
