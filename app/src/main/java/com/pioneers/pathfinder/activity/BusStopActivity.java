package com.pioneers.pathfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pioneers.pathfinder.R;

import java.util.Map;
import java.util.Set;

public class BusStopActivity extends AppCompatActivity {

    Toolbar toolbar;
    String TAG="Database setup";

    private Button findBusServices;
    private Button findShortestPath;
    private AutoCompleteTextView mSourceTextView;
    private Button btnClearSrc;
    private AdView mAdView;
    private Set busStops;
    private DatabaseReference busStopRef;
    private ArrayAdapter<String> adapterBusStop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop);

        adapterBusStop = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        try {


            //Getting Database reference:
            busStopRef = FirebaseDatabase.getInstance().getReference("LatLong");

            // Read from the database
            busStopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    Map value = (Map<String, String>) dataSnapshot.getValue();
                    busStops = value.keySet();
                    adapterBusStop.addAll(busStops);
                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        try {
            getSupportActionBar().setTitle(R.string.app_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Adding event listener to the find bus service button
        findBusServices = (Button) findViewById(R.id.btnFindBus);

        //setting onclick listener for find shortest path button

        findBusServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BusFinder", "Bus services found");

                Intent showOnMap = new Intent(BusStopActivity.this, ExpandableActivity.class);
                showOnMap.putExtra("reqType", getString(R.string.busService));
                showOnMap.putExtra("stopName", mSourceTextView.getText().toString());
                startActivity(showOnMap);
            }


        });

        //Adding event listener to the find shortest path button
        findShortestPath = (Button) findViewById(R.id.btnFindRoute);

        //setting onclick listener for find shortest path button

        findShortestPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PathFinder", "Finding shortest path");

                Intent findPath = new Intent(BusStopActivity.this, PathFinderActivity.class);
                startActivity(findPath);
            }


        });


        // Retrieve the AutoCompleteTextView that will display Source place suggestions.
        mSourceTextView = (AutoCompleteTextView) findViewById(R.id.stopTxt);

        mSourceTextView.setAdapter(adapterBusStop);

        btnClearSrc = (Button) findViewById(R.id.btnClearStop);
        btnClearSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSourceTextView.getText().toString().equals("")) {
                    mSourceTextView.setText("");
                    Log.d("PathFinder", "Clicked");
                }
            }
        });



        initAdMob();
    }

    private void initAdMob() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }
}
