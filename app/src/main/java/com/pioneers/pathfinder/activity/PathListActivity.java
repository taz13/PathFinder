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
    String[] name = {"Path 1(5 Stops)", "Path 2(7 Stops)", "Path 3(10 Stops)", "Path 4(12 Stops)", "Path 5(14 Stops)"};
    String[] route_one = {"1.Mohammadpur to Agargaon", "1.Mohammadpur to Mohakhali", "1.Mohammadpur to BRTC Area", "1.Mohammadpur to Sankar", "1.Mohammadpur to Star Kabab Dhanmondi"};
    String[] route_two = {"2.Agargaon to Mirpur 1", "2.Mohakhali to Mirpur 1", "2.BRTC Area to Mirpur 1", "2.Sankar to Mirpur 1", "2.Star Kabab Dhanmondi to Mirpur 1"};
    ArrayList<DataModel> sourceArrayList = new ArrayList<DataModel>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference dbRef;
    private Map<String,String> LatLongMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_list);

        Intent intent = getIntent();
        source=intent.getStringExtra("Source");
        destination=intent.getStringExtra("Destination");


        dbRef=FirebaseDatabase.getInstance().getReference("LatLong");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                LatLongMap= (Map<String, String>) dataSnapshot.getValue();

                System.out.println("Source Latlong: "+source+" => "+LatLongMap.get(source));
                System.out.println("Destination Latlong: "+destination+" => "+LatLongMap.get(destination));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DBREF", "Failed to read value.", error.toException());

            }
        });




//        for (int i = 0; i < name.length; i++) {
//            DataModel item = new DataModel();
//            item.setName(name[i]);
//            item.setRoute1(route_one[i]);
//            item.setRoute2(route_two[i]);
//
//            sourceArrayList.add(item);
//
//        }
//
//        //Connecting Xml to Java
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(PathListActivity.this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new MyRecyclerviewAdapter(sourceArrayList);
//        mRecyclerView.setAdapter(mAdapter);
//
//
//        ((MyRecyclerviewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerviewAdapter.MyClickListener() {
//
//            @Override
//            public void onItemClick(int position, View view) {
//                Intent intent = new Intent(PathListActivity.this, ExpandableActivity.class);
//                intent.putExtra("SourceLat", sourceLatitude);
//                intent.putExtra("SourceLong", sourceLongitude);
//                intent.putExtra("DestinationLat", destinationLatitude);
//                intent.putExtra("DestinationLong", destinationLongitude);
//                startActivity(intent);
//
//            }
//        });
    }


}
