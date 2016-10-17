package com.pioneers.pathfinder;

import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pioneers.pathfinder.util.GMapV2Direction;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class MapsPathFinderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings settings;

    Double sourceLatitude, sourceLongitude,destinationLatitudde,destinationLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_path_finder);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        sourceLatitude = intent.getDoubleExtra("SourceLat",0.0);
        sourceLongitude = intent.getDoubleExtra("SourceLong",0.0);

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
        settings=mMap.getUiSettings();

        //Showing map controls
        settings.setCompassEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setIndoorLevelPickerEnabled(true);
        settings.setMapToolbarEnabled(true);
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        // Getting latitude of the current location
       // double latitude = location.getLatitude();

        // Getting longitude of the current location
       // double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
      //  LatLng latLng = new LatLng(latitude, longitude);
        LatLng sydney = new LatLng(sourceLatitude, sourceLongitude);

        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


//        //For drawing direction
//
//        LatLng sourcePosition=new LatLng(23.750469, 90.382646);
//        LatLng destPosition=new LatLng(23.726729, 90.388880);
//
//        GMapV2Direction md = new GMapV2Direction();
//
//        Document doc = md.getDocument(sourcePosition, destPosition,
//                GMapV2Direction.MODE_DRIVING);
//
//        ArrayList<LatLng> directionPoint = md.getDirection(doc);
//        PolylineOptions rectLine = new PolylineOptions().width(3).color(
//                Color.RED);
//
//        for (int i = 0; i < directionPoint.size(); i++) {
//            rectLine.add(directionPoint.get(i));
//        }
//        Polyline polylin = mMap.addPolyline(rectLine);

    }


}
