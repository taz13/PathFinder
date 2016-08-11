package com.pioneers.pathfinder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.pioneers.pathfinder.adapter.ViewPagerAdapter;
import com.pioneers.pathfinder.common.libs.SlidingTabLayout;
import com.pioneers.pathfinder.fragments.BusStopsFragment;
import com.pioneers.pathfinder.fragments.CheapestPathFragment;
import com.pioneers.pathfinder.fragments.SettingsFragment;
import com.pioneers.pathfinder.fragments.ShortestPathFragment;
import com.pioneers.pathfinder.util.ApiConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PathFinderActivity extends AppCompatActivity implements AdapterViewCompat.OnItemSelectedListener
{

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Shortest Path","Cheapest Path","Find Bus stop","Settings"};
    int Numboftabs =4;
    private Button findShortestPath;

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
                                                    System.out.println("Shortest path found");

                                                    Intent showOnMap = new Intent(PathFinderActivity.this, MapsPathFinderActivity.class);
                                                    startActivity(showOnMap);
                                                }
                                            });

        //Auto complete code
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("PathFinderActivity", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("PathFinderActivity", "An error occurred: " + status);
            }
        });
//
//        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
//        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);
//
//        // Assigning ViewPager View and setting the adapter
//        pager = (ViewPager) findViewById(R.id.pager);
//        pager.setAdapter(adapter);
//
//        // Assiging the Sliding Tab Layout View
//        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
//        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
//
//
//        // Setting Custom Color for the Scroll bar indicator of the Tab View
//        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return getResources().getColor(R.color.tabsScrollColor);
//            }
//        });
//
//        // Setting the ViewPager For the SlidingTabsLayout
//        tabs.setViewPager(pager);

        //creating a new async task
        //new GetBusStopTask().execute(new ApiConnector());

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

//    @Override
//    public void onFragmentInteraction(String id) {
//
//    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

//    @Override
//    public void onClick(View v)
//    {
//
//    }

    @Override
    public void onItemSelected(AdapterViewCompat<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterViewCompat<?> parent) {

    }

//    public void setTextToTextView(JSONArray jsonArray)
//    {
//        String s  = "";
//        for(int i=0; i<jsonArray.length();i++){
//
//            JSONObject json = null;
//            try {
//                json = jsonArray.getJSONObject(i);
//                s = s +
//                        "Name : "+json.getString("FirstName")+" "+json.getString("LastName")+"\n"+
//                        "Age : "+json.getInt("Age")+"\n"+
//                        "Mobile Using : "+json.getString("Mobile")+"\n\n";
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//       // this.responseTextView.setText(s); //Do all the c
//    }

    private class GetBusStopTask extends AsyncTask<ApiConnector, Long,JSONArray>
    {

        @Override
        protected JSONArray doInBackground(ApiConnector... params)
        {
            //It is executed on Background thread

           return params[0].GetAllCustomers();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray)
        {
            //It is executed on the main thread

            //setTextToTextView(jsonArray);
            //Do all the functionalities here
        }
    }
}
