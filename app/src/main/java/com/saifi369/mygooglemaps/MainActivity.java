package com.saifi369.mygooglemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int DEFAULT_ZOOM = 15;
    private final double ISLAMABAD_LAT= 33.690904;
    private final double ISLAMABAD_LNG= 73.051865;

    public static final int PERMISSION_REQUEST_CODE = 9001;
    public static final String TAG = "MapDebug";
    private boolean mLocationPermissionGranted;

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view-> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        isServicesOk();

        SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment_container);

        supportMapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is showing on the screen");

        mGoogleMap=googleMap;
        gotoLocation(ISLAMABAD_LAT,ISLAMABAD_LNG);

    }

    private void gotoLocation(double lat,double lng){

        LatLng latLng=new LatLng(lat,lng);

        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);

        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }

    private void isServicesOk() {
        if (mLocationPermissionGranted) {
            Toast.makeText(this, "Ready to Map!", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){

            case R.id.maptype_none:{
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            }
            case R.id.maptype_normal:{
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            }
            case R.id.maptype_satellite:{
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            }
            case R.id.maptype_terrain:{
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            }
            case R.id.maptype_hybrid:{
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }
}
