package com.pioneers.pathfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pioneers.pathfinder.adapter.PlaceAutocompleteAdapter;
import com.pioneers.pathfinder.util.ApiConnector;

import org.json.JSONArray;


public class PathFinderActivity extends AppCompatActivity implements AdapterViewCompat.OnItemSelectedListener,GoogleApiClient.OnConnectionFailedListener
{

    Toolbar toolbar;
    private Button findShortestPath;

    //For auto complete location
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mSourceTextView;
    private AutoCompleteTextView mDestTextView;
    private Button btnClearSrc,btnClearDestination;

    Double sourceLatitude, sourceLongitude, destinationLatitude,destinationLongitude;

    //For admob
    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_finder_v2);


        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Changing Title from Toolbar
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
        findShortestPath= (Button)findViewById(R.id.btnFindPath);

        //setting onclick listener for find shortest path button

        findShortestPath.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Log.d("PathFinder","Shortest path found");
                                                    Intent showOnMap = new Intent(PathFinderActivity.this, MapsActivity.class);
                                                    showOnMap.putExtra("SourceLat",sourceLatitude);
                                                    showOnMap.putExtra("SourceLong", sourceLongitude);
                                                    showOnMap.putExtra("DestinationLat", destinationLatitude);
                                                    showOnMap.putExtra("DestinationLong",destinationLongitude);
                                                    startActivity(showOnMap);
                                                }
                                            });

        //Auto complete location code

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        // Retrieve the AutoCompleteTextView that will display Source place suggestions.
        mSourceTextView = (AutoCompleteTextView) findViewById(R.id.sourceText);

        // Register a listener that receives callbacks when a suggestion has been selected
        mSourceTextView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the AutoCompleteTextView that will display Destination place suggestions.
        mDestTextView = (AutoCompleteTextView) findViewById(R.id.destText);

        // Register a listener that receives callbacks when a suggestion has been selected
        mDestTextView.setOnItemClickListener(mListen);

        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                null);
        mSourceTextView.setAdapter(mAdapter);
        mDestTextView.setAdapter(mAdapter);

        btnClearSrc = (Button) findViewById(R.id.btnClearSrc);
        btnClearSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSourceTextView.getText().toString().equals("")){
                    mSourceTextView.setText("");
                    Log.d("PathFinder", "Clicked");
                }
            }
        });
        btnClearDestination = (Button) findViewById(R.id.btnClearDestination);
        btnClearDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDestTextView.getText().toString().equals("")){
                    mDestTextView.setText("");
                    Log.d("PathFinder", "Clicked");
                }
            }
        });

        //Create ad banner
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

    @Override
    public void onItemSelected(AdapterViewCompat<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterViewCompat<?> parent) {

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.

    }
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if(places.getCount()==1){
                        //Do the things here on Click.....
                        sourceLatitude = places.get(0).getLatLng().latitude;
                        sourceLongitude = places.get(0).getLatLng().longitude;
                        CharSequence sourceName = places.get(0).getName();

                        Toast.makeText(getApplicationContext(),"Latitude:"+sourceLatitude+"Longitude:"+sourceLongitude,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"SOMETHING_WENT_WRONG",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };



    private AdapterView.OnItemClickListener mListen
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.

    }
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if(places.getCount()==1){
                        //Do the things here on Click.....

                        destinationLatitude =  places.get(0).getLatLng().latitude;
                        destinationLongitude = places.get(0).getLatLng().longitude;
                        Toast.makeText(getApplicationContext(),"Latitude:"+sourceLatitude+"Longitude:"+sourceLongitude,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"SOMETHING_WENT_WRONG",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     *
     */


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}
