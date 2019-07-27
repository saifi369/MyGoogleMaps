package com.saifi369.mygooglemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BatchLocationActivity extends AppCompatActivity {

    public static final String TAG = "MyTag";
    private TextView mOutputText;
    private Button mBtnLocationRequest;
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_location);

        mOutputText = findViewById(R.id.tv_output);
        mBtnLocationRequest = findViewById(R.id.btn_location_request);

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: location error");
                    return;
                }

                List<Location> locations = locationResult.getLocations();

                LocationResultHelper helper = new LocationResultHelper(BatchLocationActivity.this, locations);

                helper.showNotification();

                Toast.makeText(BatchLocationActivity.this, "Location received: " + locations.size(), Toast.LENGTH_SHORT).show();

                mOutputText.setText(helper.getLocationResultText());

//                Log.d(TAG, "onLocationResult: " + location.getLatitude() + " \n" +
//                        location.getLongitude());


            }
        };
        mBtnLocationRequest.setOnClickListener(this::requestBatchLocationUpdates);


    }

    private void requestBatchLocationUpdates(View view) {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(4000);

        locationRequest.setMaxWaitTime(15 * 1000);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        mLocationClient.removeLocationUpdates(mLocationCallback);
    }

}
