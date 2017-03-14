package com.pioneers.pathfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pioneers.pathfinder.R;
//import com.pioneers.pathfinder.util.ApiConnector;


public class PathFinderActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //For auto complete location
    protected GoogleApiClient mGoogleApiClient;
    Toolbar toolbar;
    String TAG="Database setup";
    Double sourceLatitude, sourceLongitude, destinationLatitudde, destinationLongitude;


    //private AutoCompleteTextView mAutocompleteView;
    private Button findShortestPath;
    private AutoCompleteTextView mSourceTextView;
    private AutoCompleteTextView mDestTextView;
    private Button btnClearSrc, btnClearDestination;
    private AdView mAdView;
    private AdapterView.OnItemClickListener mAutocompleteClickListener;
    private AdapterView.OnItemClickListener mListen;
    private String busStops[];
    private DatabaseReference busStopRef;
    private ArrayAdapter<String> adapterBusStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_finder_v2);
        adapterBusStop=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line);
        try {
            //Getting Database reference:
            busStopRef = FirebaseDatabase.getInstance().getReference("BusStopNames");

            // Read from the database
            busStopRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    busStops=value.split(":");
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

        getSupportActionBar().setTitle(R.string.app_name);

        //Populating shortest path options
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.path_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Adding event listener to the button
        findShortestPath = (Button) findViewById(R.id.btnFindPath);

        //setting onclick listener for find shortest path button

        findShortestPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PathFinder", "Shortest path found");

                Intent showOnMap = new Intent(PathFinderActivity.this, ExpandableActivity.class);
                showOnMap.putExtra("Source", mSourceTextView.getText().toString());
                showOnMap.putExtra("Destination", mDestTextView.getText().toString());
                startActivity(showOnMap);
            }


        });

        //Auto complete location code



        // Retrieve the AutoCompleteTextView that will display Source place suggestions.
        mSourceTextView = (AutoCompleteTextView) findViewById(R.id.sourceText);



        // Retrieve the AutoCompleteTextView that will display Destination place suggestions.
        mDestTextView = (AutoCompleteTextView) findViewById(R.id.destText);

        mSourceTextView.setAdapter(adapterBusStop);
        mDestTextView.setAdapter(adapterBusStop);

        btnClearSrc = (Button) findViewById(R.id.btnClearSrc);
        btnClearSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSourceTextView.getText().toString().equals("")) {
                    mSourceTextView.setText("");
                    Log.d("PathFinder", "Clicked");
                }
            }
        });
        btnClearDestination = (Button) findViewById(R.id.btnClearDestination);
        btnClearDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDestTextView.getText().toString().equals("")) {
                    mDestTextView.setText("");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() { // To destroy adview
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
