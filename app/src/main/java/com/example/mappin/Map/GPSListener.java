package com.example.mappin.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.mappin.Dialog.CreateDialog;
import com.example.mappin.Dialog.ModifyDialog;
import com.example.mappin.Thread.CreateThread;
import com.example.mappin.Thread.DeleteThread;
import com.example.mappin.Thread.ReadThread;
import com.example.mappin.Thread.UpdateThread;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GPSListener implements LocationListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final float MARKER_COLOR = BitmapDescriptorFactory.HUE_RED;
    private static final float SELECTED_MARKER_COLOR = BitmapDescriptorFactory.HUE_GREEN;

    private CreateDialog createDialog = null;
    private ModifyDialog modifyDialog = null;

    private GoogleMap map;
    private Context context;
    private Location currentLocation;
    private LocationManager locationManager;
    private HashMap<String, Marker> markers = new HashMap<>();
    private Marker tempMarker = null;

    private boolean isOnStartMap = true;

    private static class GPSListenerHolder {
        private static final GPSListener instance = new GPSListener();
    }

    public static GPSListener getInstance() {
        return GPSListenerHolder.instance;
    }

    @Override
    public void onLocationChanged(Location location) {
        // check best location provider
        if (isBetterLocation(location, currentLocation)) {
            currentLocation = location;
        }

        if (isOnStartMap) {
            // Map start : move camera
            showLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
            isOnStartMap = false;
        }
    }

    // Move camera to current location on Google Map
    private void showLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    public void showAllMarker(String value){
        removeAllMark();

        try{
            JSONObject jsonObject = new JSONObject(value);

            JSONArray markerArray = jsonObject.getJSONArray("result");

            for(int i=0; i<markerArray.length(); i++)
            {
                JSONObject markerObject = markerArray.getJSONObject(i);

                LatLng curPoint = new LatLng(Double.parseDouble(markerObject.getString("latitude")), Double.parseDouble(markerObject.getString("longitude")));

                Marker marker = map.addMarker(new MarkerOptions()
                        .position(curPoint)
                        .title(markerObject.getString("id"))
                        .snippet(markerObject.getString("address"))
                        .icon(BitmapDescriptorFactory.defaultMarker(MARKER_COLOR)));

                markers.put(markerObject.getString("id"), marker);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* remove Marker by id */
    private void removeAllMark() {
        for (Marker marker : markers.values()) {
            marker.remove();
        }
        markers.clear();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("onMapClick : ", "MapClick!!!");
        /* If tempMarker already exist, remove tempMarker */
        if(tempMarker != null){
            tempMarker.remove();
            tempMarker = null;
        }
        /* Save new marker */
        tempMarker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(SELECTED_MARKER_COLOR)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(tempMarker)){
            Log.d("onMarkerClick", "Temp MarkerClick@@@");
            showCreateDialog(marker);
            tempMarker.remove();
            tempMarker = null;
        }else{
            showModifyDialog(marker);
            Log.d("onMarkerClick", "MarkerClick@@@");
        }
        return false;
    }

    private void showCreateDialog(final Marker marker){
        createDialog = new CreateDialog(context);
        createDialog.setTitle("마커 생성");
        createDialog.setTxt_id(marker.getTitle());

        createDialog.setCreateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateThread c_thread = new CreateThread(context);
                ReadThread r_thread = new ReadThread(context);

                String latitude = Double.toString(marker.getPosition().latitude);
                String longitude = Double.toString(marker.getPosition().longitude);
                String address = createDialog.getTxt_address();
                String user = "test";

                c_thread.execute(latitude, longitude, address, user);
                r_thread.execute();
                createDialog.dismiss();
            }
        });
        createDialog.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.cancel();
            }
        });
        createDialog.show();
    }
    private void showModifyDialog(final Marker marker){
        modifyDialog = new ModifyDialog(context);
        modifyDialog.setTitle("마커 정보 업데이트");
        modifyDialog.setTxt_id(marker.getTitle());

        modifyDialog.setUpdateClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateThread u_thread = new UpdateThread(context);
                ReadThread r_thread = new ReadThread(context);

                String id = marker.getTitle();
                String address = modifyDialog.getTxt_address();

                u_thread.execute(id, address);
                r_thread.execute();
                modifyDialog.dismiss();
            }
        });
        modifyDialog.setDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteThread d_thread = new DeleteThread(context);
                ReadThread r_thread = new ReadThread(context);

                String id = marker.getTitle();

                d_thread.execute(id);
                r_thread.execute();
                modifyDialog.dismiss();
            }
        });
        modifyDialog.show();
    }

    // check newLocation better than currentLocation
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /* check same location Provider */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    /* Register GPSListener in LocationManager */
    public void startLocationUpdate(){
        /* if map is null or context is null, return false; */
        if(map == null || context == null){
            Log.e("startLocationUpdate", "no map instance OR no context instance");
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            /* Check location authority and location function available */
            /* False: close activity */
            if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) ||
                    !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(context, "GPS 권한이 없거나 위치 기능이 꺼져있습니다.", Toast.LENGTH_LONG).show();
                ((Activity)context).finish();
            }
            /* set Camera location */
            if(currentLocation != null)
                showLocation(currentLocation.getLatitude(), currentLocation.getLongitude());

            // GPSListener register
            long minTime = 10000;
            float minDistance = 0;

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Remove GPSListener from LocationManager */
    public void removeLocationUpdate(){
        if (locationManager != null)
            locationManager.removeUpdates(this);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
        /* show my location button on map */
        map.setMyLocationEnabled(true);
        /* Add ClickLister on map */
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);

        searchAllMarker();
    }

    /* search all marker data in database */
    public void searchAllMarker(){
        ReadThread readThread = new ReadThread(context);
        readThread.execute();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
