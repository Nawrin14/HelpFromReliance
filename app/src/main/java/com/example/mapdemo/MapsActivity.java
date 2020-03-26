package com.example.mapdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleAPIClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long update_interval = 120000;
    private long fastest_interval = 300000;
    private LocationManager locationmanager;
    private LatLng latLng;
    private boolean isPermission;

    private Button confirmLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        confirmLocation = (Button) findViewById(R.id.confirmButton);

        if(requestSinglePermission()){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleAPIClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation();
        }

        confirmLocation.setOnClickListener(this);
    }

    private boolean requestSinglePermission() {

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                isPermission = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                isPermission = false;
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();

        return isPermission;
    }

    private boolean checkLocation() {

        if(!isLocationEnabled()){
            showAlert();
        }

        return isLocationEnabled();
    }

    private void showAlert() {

        Toast.makeText(this,"Enable Location!",Toast.LENGTH_SHORT).show();

    }

    private boolean isLocationEnabled() {

        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(latLng!=null){
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14F));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.
                ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            return;
        }

        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleAPIClient);

        if(mLocation == null)
            startLocationUpdates();

    }

    private void startLocationUpdates() {

        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(update_interval).setFastestInterval(fastest_interval);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.
                ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleAPIClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mGoogleAPIClient!=null)
            mGoogleAPIClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleAPIClient.isConnected())
            mGoogleAPIClient.disconnect();
    }

    @Override
    public void onClick(View v) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        String fullAddress;

        try {
            address = geocoder.getFromLocation(mLocation.getLatitude(),mLocation.getLongitude(),1);
            String addresses = address.get(0).getAddressLine(0);
            String area = address.get(0).getLocality();
            //String city = address.get(0).getAdminArea();
            //String country = address.get(0).getCountryName();
            //String postalCode = address.get(0).getPostalCode();

            fullAddress = addresses+" "+area;

            Intent newIntent = new Intent(MapsActivity.this,Main4Activity.class);
            newIntent.putExtra("address", fullAddress);
            startActivity(newIntent);
            finish();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
