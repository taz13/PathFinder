package com.pioneers.pathfinder;

import android.app.Activity;
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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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


public class PathFinderActivity extends AppCompatActivity implements ShortestPathFragment.OnFragmentInteractionListener,
                                                                        CheapestPathFragment.OnFragmentInteractionListener,
                                                                        BusStopsFragment.OnFragmentInteractionListener,
                                                                        SettingsFragment.OnFragmentInteractionListener
{

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Shortest Path","Cheapest Path","Find Bus stop","Settings"};
    int Numboftabs =4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_finder);


        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Changing Title from Toolbar
        getSupportActionBar().setTitle(R.string.app_name);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        //creating a new async task
        new GetBusStopTask().execute(new ApiConnector());

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
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
