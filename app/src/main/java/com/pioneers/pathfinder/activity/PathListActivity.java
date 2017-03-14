package com.pioneers.pathfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pioneers.pathfinder.R;
import com.pioneers.pathfinder.adapter.MyRecyclerviewAdapter;
import com.pioneers.pathfinder.model.DataModel;

import java.util.ArrayList;
import java.util.Map;

public class PathListActivity extends AppCompatActivity {

    String source;
    String destination;
//    String[] name = {"Path 1(5 Stops)", "Path 2(7 Stops)", "Path 3(10 Stops)", "Path 4(12 Stops)", "Path 5(14 Stops)"};
//    String[] route_one = {"1.Mohammadpur to Agargaon", "1.Mohammadpur to Mohakhali", "1.Mohammadpur to BRTC Area", "1.Mohammadpur to Sankar", "1.Mohammadpur to Star Kabab Dhanmondi"};
//    String[] route_two = {"2.Agargaon to Mirpur 1", "2.Mohakhali to Mirpur 1", "2.BRTC Area to Mirpur 1", "2.Sankar to Mirpur 1", "2.Star Kabab Dhanmondi to Mirpur 1"};
    ArrayList<String> pathName;
    //ArrayList<String> routes;
    ArrayList<DataModel> pathArrayList = new ArrayList<DataModel>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference dbRef;
    private DatabaseReference latLongDbRef;
    private DatabaseReference shortestPathDbRef;
    private Map<String,String> latLongMap;
    private Map<String,String> shortestPathMap;
    private Iterable databaseChildren;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_list);

        Intent intent = getIntent();
        source=intent.getStringExtra("Source");
        destination=intent.getStringExtra("Destination");


        dbRef=FirebaseDatabase.getInstance().getReference();
        latLongDbRef=dbRef.child("LatLong");
        shortestPathDbRef=dbRef.child("ShortestPaths").child(source+":"+destination);

        //DB ref for bus stop latitude and longitude list
        latLongDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                latLongMap= (Map<String, String>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DBREF", "Failed to read Latitude and Longitude.", error.toException());

            }
        });
        //DB Ref for shortest path
        shortestPathDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                shortestPathMap= (Map<String, String>) dataSnapshot.getValue();
                populatePathList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.w("DBREF", "Failed to read Shortest path list", databaseError.toException());
            }
        });



//

    }
    //Populate paths
    private void populatePathList()
    {
        pathName=new ArrayList<>(shortestPathMap.keySet());
         //TODO: sort pathName
        for (int i = 0; i < shortestPathMap.size(); i++) {
            DataModel item = new DataModel();
            item.setName(pathName.get(i));
            item.setRoute(shortestPathMap.get(pathName.get(i)));
            item.setStopCount(String.valueOf(shortestPathMap.get(pathName.get(i)).split(":").length));

            pathArrayList.add(item);

        }

        //Connecting Xml to Java0
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(PathListActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerviewAdapter(pathArrayList);
        mRecyclerView.setAdapter(mAdapter);


        ((MyRecyclerviewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerviewAdapter.MyClickListener() {

            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(PathListActivity.this, ExpandableActivity.class);
//                intent.putExtra("SourceLat", sourceLatitude);
//                intent.putExtra("SourceLong", sourceLongitude);
//                intent.putExtra("DestinationLat", destinationLatitude);
//                intent.putExtra("DestinationLong", destinationLongitude);
                startActivity(intent);

            }
        });
    }


}
