package com.pioneers.pathfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pioneers.pathfinder.R;
import com.pioneers.pathfinder.adapter.MyRecyclerviewAdapter;
import com.pioneers.pathfinder.model.DataModel;

import java.util.ArrayList;

public class PathList extends AppCompatActivity {

    Double sourceLatitude, sourceLongitude,destinationLatitude,destinationLongitude;
    String[] name = {"Path 1(5 Stops)","Path 2(7 Stops)","Path 3(10 Stops)","Path 4(12 Stops)","Path 5(14 Stops)"};
    String[] route_one = {"1.Mohammadpur to Agargaon","1.Mohammadpur to Mohakhali","1.Mohammadpur to BRTC Area","1.Mohammadpur to Sankar","1.Mohammadpur to Star Kabab Dhanmondi"};
    String[] route_two = {"2.Agargaon to Mirpur 1","2.Mohakhali to Mirpur 1","2.BRTC Area to Mirpur 1","2.Sankar to Mirpur 1","2.Star Kabab Dhanmondi to Mirpur 1"};
    ArrayList<DataModel> sourceArrayList = new ArrayList<DataModel>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_list);

        Intent intent = getIntent();
        sourceLatitude = intent.getDoubleExtra("SourceLat", 0.0);
        sourceLongitude = intent.getDoubleExtra("SourceLong", 0.0);
        destinationLatitude = intent.getDoubleExtra("DestinationLat", 0.0);
        destinationLongitude = intent.getDoubleExtra("DestinationLong", 0.0);


        for (int i = 0;i<name.length;i++){
            DataModel item = new DataModel();
            item.setName(name[i]);
            item.setRoute1(route_one[i]);
            item.setRoute2(route_two[i]);

            sourceArrayList.add(item);

        }

        //Connecting Xml to Java
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(PathList.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerviewAdapter(sourceArrayList);
        mRecyclerView.setAdapter(mAdapter);


        ((MyRecyclerviewAdapter)mAdapter).setOnItemClickListener(new MyRecyclerviewAdapter.MyClickListener(){

            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent(PathList.this,ExpandableActivity.class);
                intent.putExtra("SourceLat",sourceLatitude);
                intent.putExtra("SourceLong", sourceLongitude);
                intent.putExtra("DestinationLat",destinationLatitude);
                intent.putExtra("DestinationLong",destinationLongitude);
                startActivity(intent);

            }
        });
    }


}
