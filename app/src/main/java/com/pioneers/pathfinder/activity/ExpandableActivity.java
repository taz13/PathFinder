package com.pioneers.pathfinder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pioneers.pathfinder.R;
import com.pioneers.pathfinder.adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Taslima on 10/17/2015.
 */
public class ExpandableActivity extends Activity { // For Test Commit

    public static ExpandableActivity instance = null;
    Context context;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listPathHeader;
    HashMap<String, List<String>> listPathDetails;
    String reqType;
    String stopName;
    String source;
    String destination;
    String busStops[];
    ArrayList keyList;
    private DatabaseReference dbRef;
    private DatabaseReference shortestPathDbRef;
    private DatabaseReference busServiceDbRef;
    private Map<String, String> shortestPathMap;
    private Map<String, String> busServiceMap;
    private AdView mAdView;
    private DatabaseReference latLongDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_activity);
        context = this;
        instance = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        Intent intent = getIntent();
        dbRef = FirebaseDatabase.getInstance().getReference();
        latLongDbRef = dbRef.child("LatLong");
        reqType=intent.getStringExtra("reqType");
        if (reqType.equals(getString(R.string.shortestPath))) {
            source = intent.getStringExtra("Source");
            destination = intent.getStringExtra("Destination");

            //DB Ref for shortest path
            shortestPathDbRef = dbRef.child("ShortestPaths").child(source + ":" + destination);
            shortestPathDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    shortestPathMap = (Map<String, String>) dataSnapshot.getValue();
                    prepareRouteListData();
                    listAdapter = new ExpandableListAdapter(getBaseContext(), listPathHeader, listPathDetails);
                    listAdapter.setSource(source);
                    listAdapter.setDestination(destination);
                    listAdapter.setKeyList(keyList);
                    listAdapter.setPathMap(shortestPathMap);
                    expListView.setAdapter(listAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("DBREF", "Failed to read Shortest path list", databaseError.toException());
                }
            });
        } else if (reqType.equals(getString(R.string.busService)))
        {
            stopName=intent.getStringExtra("stopName");

            //DB Ref for shortest path
            busServiceDbRef = dbRef.child("BusServices");
            busServiceDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    busServiceMap = (Map<String, String>) dataSnapshot.getValue();
                    prepareBusServiceListData();
                    listAdapter = new ExpandableListAdapter(getBaseContext(), listPathHeader, listPathDetails);
                    listAdapter.setSource(busServiceMap.get(keyList.get(0)));
                    listAdapter.setDestination(busServiceMap.get(keyList.get(keyList.size() - 1)));
                    listAdapter.setKeyList(keyList);
                    listAdapter.setPathMap(busServiceMap);
                    expListView.setAdapter(listAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("DBREF", "Failed to read Shortest path list", databaseError.toException());
                }
            });
        }
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

    //format shortest path data
    private void prepareRouteListData() {
        listPathDetails = new HashMap<String, List<String>>();
        listPathHeader = new ArrayList<>();
        //Add header
        keyList = new ArrayList<>(shortestPathMap.keySet());
        ArrayList temp = new ArrayList();
        for (int i = 0; i < shortestPathMap.size(); i++) {
            listPathHeader.add("Route " + (i + 1));
            busStops = shortestPathMap.get(keyList.get(i)).split(":");
            StringBuilder buildRoute = new StringBuilder("");
            buildRoute.append("Cost: " + keyList.get(i) + "\n");
            buildRoute.append("Number of stops: " + busStops.length + "\n");
            buildRoute.append("Route:\n");
            for (int j = 0; j < busStops.length; j++) {
                buildRoute.append("\t" + (j + 1) + ". " + busStops[j] + "\n");
            }

            temp.add(buildRoute.toString());
            listPathDetails.put(listPathHeader.get(i), temp);
        }


    }

    //Formatting bus service data
    private void prepareBusServiceListData() {
        listPathDetails = new HashMap<String, List<String>>();
        listPathHeader = new ArrayList<>();
        //Add header
        keyList = new ArrayList<>(busServiceMap.keySet());
        ArrayList temp = new ArrayList();
        for (int i = 0; i < busServiceMap.size(); i++) {
            listPathHeader.add((String) keyList.get(i));
            busStops = busServiceMap.get(keyList.get(i)).split(":");
            StringBuilder buildRoute = new StringBuilder("");
            buildRoute.append("Stops:\n");
            for (int j = 0; j < busStops.length; j++) {
                buildRoute.append("\t" + (j + 1) + ". " + busStops[j] + "\n");
            }
            temp.clear();
            temp.add(buildRoute.toString());
            listPathDetails.put(listPathHeader.get(i), (ArrayList<String>) temp.clone());
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("Expandable", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
}
