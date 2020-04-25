package com.example.mappin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mappin.Map.GPSListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {
    /* Map object */
    private SupportMapFragment mapFragment;
    private GPSListener gpsListener;

    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGoogleMap();

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsListener.searchAllMarker();
            }
        });
    }

    /* Google Map first setting */
    private void initGoogleMap() {
        /* Google Map Fragment register */
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                /* set GPSListener */
                gpsListener = GPSListener.getInstance();
                gpsListener.setContext(MainActivity.this);
                gpsListener.setMap(googleMap);
                gpsListener.startLocationUpdate();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsListener.removeLocationUpdate();
    }
}
