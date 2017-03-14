package com.pioneers.pathfinder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

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
    public Double sourceLatitude, sourceLongitude, destinationLatitude, destinationLongitude;
    Context context;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listPathHeader;
    HashMap<String, List<String>> listPathDetails;
    String source;
    String destination;
    String busStops[];
    ArrayList costList;
    private DatabaseReference dbRef;
    private DatabaseReference latLongDbRef;
    private DatabaseReference shortestPathDbRef;
    private Map<String, String> shortestPathMap;

    public static ExpandableActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_activity);
        context = this;
        instance = this;
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        Intent intent = getIntent();
        source = intent.getStringExtra("Source");
        destination = intent.getStringExtra("Destination");


        dbRef = FirebaseDatabase.getInstance().getReference();
        latLongDbRef = dbRef.child("LatLong");
        shortestPathDbRef = dbRef.child("ShortestPaths").child(source + ":" + destination);

        //DB Ref for shortest path
        shortestPathDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shortestPathMap = (Map<String, String>) dataSnapshot.getValue();
                prepareListData();
                listAdapter = new ExpandableListAdapter(getBaseContext(), listPathHeader, listPathDetails);
                expListView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DBREF", "Failed to read Shortest path list", databaseError.toException());
            }
        });


    }

    private void prepareListData() {
        listPathDetails = new HashMap<String, List<String>>();
        listPathHeader = new ArrayList<>();
        //Add header
        costList = new ArrayList<>(shortestPathMap.keySet());

        for (int i = 0; i < shortestPathMap.size(); i++) {
            listPathHeader.add("Route " + (i + 1));
            busStops = shortestPathMap.get(costList.get(i)).split(":");
            StringBuilder buildRoute = new StringBuilder("");
            buildRoute.append("Cost: " + costList.get(i) + "\n");
            buildRoute.append("Number of stops: " + busStops.length + "\n");
            buildRoute.append("Route:\n");
            for (int j = 0; j < busStops.length; j++) {
                buildRoute.append("\t" + (j + 1) + ". " + busStops[j] + "\n");
            }
            ArrayList temp = new ArrayList();
            temp.add(buildRoute.toString());
            listPathDetails.put(listPathHeader.get(i), temp);
        }
//        // Adding child data
//        List<String> BRTC9 = new ArrayList<String>();
//        BRTC9.add("1. Mohammadpur\n" +
//                "\n" +
//                "2. Shyamoli\n" +
//                "\n" +
//                "3. Shishu Mela\n" + "\n" +
//                "4. Agargaon\n" + "\n" +
//                "5. Mohakhali\n" + "\n" +
//                "6. Gulsan 1\n" + "\n" +
//                "7. Badda Link Road\n" + "\n" +
//                "8. Badda");
//
//        List<String> BRTC10 = new ArrayList<String>();
//        BRTC9.add("1. Mohammadpur\n" +
//                "\n" +
//                "2. Shyamoli\n" +
//                "\n" +
//                "3. Shishu Mela\n" + "\n" +
//                "4. Agargaon\n" + "\n" +
//                "5. Mohakhali\n" + "\n" +
//                "6. Gulsan 1\n" + "\n" +
//                "7. Gulsan 2\n");
//
//
//        List<String> sevend = new ArrayList<String>();
//        sevend.add("1. Gabtoli\n" +
//                "\n" +
//                "2. Mirpur 1\n" +
//                "\n" +
//                "3. Mirpur 10\n" + "\n" +
//                "4. Agargaon\n" + "\n" +
//                "5. Mohakhali\n" + "\n" +
//                "6. Kakoli\n" + "\n" +
//                "7. Uttara\n" + "\n" +
//                "8. Abdullahpur");
//
//        List<String> two = new ArrayList<String>();
//        two.add("1. Kamalapur\n" +
//                "\n" +
//                "2. Razarbag Police Line\n" +
//                "\n" +
//                "3. Maghbazar\n" + "\n" +
//                "4. Bangla Motor\n" + "\n" +
//                "5. Farmgate\n" + "\n" +
//                "6. Agargaon\n" + "\n" +
//                "7. Shewrapara\n" + "\n" +
//                "8. Kazipara\n" + "\n" +
//                "9. Mirpur 10\n" + "\n" +
//                "10. Mirpur 2\n" + "\n" +
//                "11. Mirpur 1\n" + "\n" +
//                "12. Zoo");
//
//
//        listPathDetails.put(listPathHeader.get(0), BRTC9);
//        listPathDetails.put(listPathHeader.get(1), BRTC10);
//        listPathDetails.put(listPathHeader.get(2), sevend);
//        listPathDetails.put(listPathHeader.get(3), two);// Header, Child data


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
