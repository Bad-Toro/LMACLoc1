package com.mobileappscompany.training.macloc1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.gms.location.LocationServices;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    TextView tV;
    Location lLoc;
    GoogleApiClient gaClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tV = (TextView) findViewById(R.id.textView);
        checkGPSIsOn();

        buildAPIC();


    }

    private void buildAPIC() {
        gaClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    void checkGPSIsOn() {
        LocationManager lM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean yesOn = lM.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!yesOn) {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        gaClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gaClient.isConnected()){
            gaClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lLoc = LocationServices.FusedLocationApi.getLastLocation(gaClient);

        if(lLoc != null){
            tV.setText(
                    "Lat: " +
                            String.valueOf(lLoc.getLatitude()) + "\n" +
                            "Long: " +
                            String.valueOf(lLoc.getLongitude())
            );
        } else {
            Toast.makeText(this, "No Location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
