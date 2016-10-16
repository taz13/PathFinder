package com.pioneers.pathfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import com.google.maps.android.SphericalUtil;
import com.pioneers.pathfinder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
                            GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,GoogleMap.OnMapLongClickListener,
        View.OnClickListener{

    private GoogleMap mMap;
    String final_Lat, final_longitude,final_name,latFList,logFList;
    //Double toLatitude, toLongitude,fromLatutude,fromLongitude;
    private GoogleApiClient googleApiClient;
    private Button buttonCalculate;
    Double sourceLatitude, sourceLongitude,destinationLatitude,destinationLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
        // final_result = intent.getStringExtra("Result");
        Intent intent = getIntent();

        sourceLatitude = intent.getDoubleExtra("SourceLat", 0.0);
        sourceLongitude = intent.getDoubleExtra("SourceLong", 0.0);
        destinationLatitude = intent.getDoubleExtra("DestinationLat", 0.0);
        destinationLongitude = intent.getDoubleExtra("DestinationLong", 0.0);

       /* final_name = intent.getStringExtra("ResultName");
        final_Lat = intent.getStringExtra("ResultLat");
        final_longitude = intent.getStringExtra("ResultLon");
        latFList = intent.getStringExtra("LatMap");
        logFList = intent.getStringExtra("LogMap");
       // cityNameFList = intent.getStringExtra("CityNameMap");
        toLatitude  = Double.parseDouble(final_Lat);
        toLongitude = Double.parseDouble(final_longitude);
        fromLatutude  = Double.parseDouble(latFList);
        fromLongitude  = Double.parseDouble(logFList);*/
       // Toast.makeText(MapsActivity.this,"Values"+cityNameFList,Toast.LENGTH_LONG).show();
        googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
       // buttonCalculate = (Button)findViewById(R.id.buttonCalcDistance);
       /* buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
               getDirection();
               /* CameraPosition googlePlex2 = CameraPosition.builder()
                        .target(new LatLng(sourceLatitude,sourceLongitude))
                        .zoom(16)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.addMarker(new MarkerOptions().position(new LatLng(sourceLatitude, sourceLongitude)).title("My Position")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_location))
                ).showInfoWindow();



                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex2));*/
               //mMap.setMyLocationEnabled(true);
            //}
        //});

    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to  AIzaSyCW7MbMhbZLceGX63QEqdpF1HZbqOZM3fQ
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyCFOe4injVmrEQdzfqmnipHcAhGk06vtPY");
        return urlString.toString();
    }

    private void getDirection(){
        //Getting the URL
        String url = makeURL(sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude);
        Log.d("this is reposne",url);
       // Toast.makeText(this,"this is url"+url,Toast.LENGTH_SHORT).show();
        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        drawPath(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //The parameter is the server response
    public void drawPath(String  result) {
        //Getting both the coordinates
        LatLng from = new LatLng(sourceLatitude,sourceLongitude);
        LatLng to = new LatLng(destinationLatitude,destinationLongitude);

        //Calculating the distance in meters
        Double distance = SphericalUtil.computeDistanceBetween(from, to);

        //Displaying the distance
        Toast.makeText(this,String.valueOf(distance+" Meters"),Toast.LENGTH_SHORT).show();


        try {
            //Parsing json
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
           // Toast.makeText(getApplicationContext(),"this is point"+encodedString,Toast.LENGTH_LONG).show();
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(20)
                    .color(Color.RED)
                    .geodesic(true)
            );


        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
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
        mMap.setMyLocationEnabled(true);

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(destinationLatitude,destinationLongitude))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();

        //adding marker to the map
        mMap.addMarker(new MarkerOptions().position(new LatLng(destinationLatitude, destinationLongitude)).title(final_name)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                ).showInfoWindow();
      // .icon(BitmapDescriptorFactory.fromResource(R.mipmap.medical)))

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));

    }


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Maps Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.pioneers.pathfinder/http/host/path")
        );
        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }


}
