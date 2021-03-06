package com.pioneers.pathfinder.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by root on 1/11/16.
 */
public class PathFinderApplication extends Application {
    public static final String TAG = "PathFinder";

    private static Context context;
    private static PathFinderApplication mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static Context getAppContext() {
        return PathFinderApplication.context;
    }

    public static synchronized PathFinderApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        PathFinderApplication.context = getApplicationContext();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

}
