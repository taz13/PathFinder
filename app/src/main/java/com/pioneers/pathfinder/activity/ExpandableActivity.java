package com.pioneers.pathfinder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.pioneers.pathfinder.R;
import com.pioneers.pathfinder.adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Taslima on 10/17/2015.
 */
public class ExpandableActivity extends Activity { // For Test Commit

    Context context;
    public static ExpandableActivity instance = null;
    public Double sourceLatitude, sourceLongitude,destinationLatitude,destinationLongitude;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_activity);
        context = this;
        instance = this;
        expListView=(ExpandableListView)findViewById(R.id.lvExp);

        Intent intent = getIntent();
        sourceLatitude = intent.getDoubleExtra("SourceLat", 0.0);
        sourceLongitude = intent.getDoubleExtra("SourceLong", 0.0);
        destinationLatitude = intent.getDoubleExtra("DestinationLat", 0.0);
        destinationLongitude = intent.getDoubleExtra("DestinationLong", 0.0);

        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);



    }

    public static ExpandableActivity getInstance() {
        return instance;
    }
    private void prepareListData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("BRTC-9");
        listDataHeader.add("BRTC-10");
        listDataHeader.add("7/D");
        listDataHeader.add("2");


        // Adding child data
        List<String> BRTC9 = new ArrayList<String>();
        BRTC9.add("1. Mohammadpur\n" +
                "\n" +
                "2. Shyamoli\n" +
                "\n" +
                "3. Shishu Mela\n"+"\n"+
                 "4. Agargaon\n"+"\n"+
                 "5. Mohakhali\n"+"\n"+
                  "6. Gulsan 1\n"+"\n"+
                   "7. Badda Link Road\n"+"\n"+
                    "8. Badda");

        List<String> BRTC10 = new ArrayList<String>();
        BRTC10.add("1. Mohammadpur\n" +
                "\n" +
                "2. Shyamoli\n" +
                "\n" +
                "3. Shishu Mela\n"+"\n"+
                "4. Agargaon\n"+"\n"+
                "5. Mohakhali\n"+"\n"+
                "6. Gulsan 1\n"+"\n"+
                "7. Gulsan 2\n");


        List<String> sevend = new ArrayList<String>();
        sevend.add("1. Gabtoli\n" +
                "\n" +
                "2. Mirpur 1\n" +
                "\n" +
                "3. Mirpur 10\n"+"\n"+
                "4. Agargaon\n"+"\n"+
                "5. Mohakhali\n"+"\n"+
                "6. Kakoli\n"+"\n"+
                "7. Uttara\n"+"\n"+
                "8. Abdullahpur");

        List<String> two = new ArrayList<String>();
        two.add("1. Kamalapur\n" +
                "\n" +
                "2. Razarbag Police Line\n" +
                "\n" +
                "3. Maghbazar\n"+"\n"+
                "4. Bangla Motor\n"+"\n"+
                "5. Farmgate\n"+"\n"+
                "6. Agargaon\n"+"\n"+
                "7. Shewrapara\n"+"\n"+
                "8. Kazipara\n"+"\n"+
                 "9. Mirpur 10\n"+"\n"+
                  "10. Mirpur 2\n"+"\n"+
                  "11. Mirpur 1\n"+"\n"+
                   "12. Zoo");




        listDataChild.put(listDataHeader.get(0), BRTC9);
        listDataChild.put(listDataHeader.get(1), BRTC10);
        listDataChild.put(listDataHeader.get(2), sevend);
        listDataChild.put(listDataHeader.get(3), two);// Header, Child data


    }

}
