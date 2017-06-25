/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.quakereport.components.EarthquakeLoader;
import com.example.android.quakereport.structs.EarthQuakeInfo;
import com.example.android.quakereport.vews.EqArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.net.URL;
import android.app.LoaderManager;
import android.widget.TextView;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<EarthQuakeInfo>>{

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private EqArrayAdapter adapter;



    private static final String URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
//            "http://earthquake.usgs.gov/fdsnws/event/1/query" +
//            "?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

//    "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        prepareUi();
        ConnectivityManager cm = (ConnectivityManager) this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            findViewById(R.id.progress).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.emptyMessage)).setText("No internet connection");
        }
    }

    private void prepareUi() {

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(findViewById(R.id.emptyMessage));

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthQuakeInfo earthQuakeInfo = (EarthQuakeInfo) adapterView.getItemAtPosition(i);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(earthQuakeInfo.getUrl()));
                startActivity(browserIntent);
            }
        });

        // Create a new {@link ArrayAdapter} of earthquakes
        this.adapter = new EqArrayAdapter(this);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
    }

    @Override
    public Loader<List<EarthQuakeInfo>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "onCreateLoader ==>");
        return new EarthquakeLoader(this, LOG_TAG, URL );
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuakeInfo>> loader, List<EarthQuakeInfo> data) {
        findViewById(R.id.progress).setVisibility(View.GONE);
        if(data==null){
            ((TextView)findViewById(R.id.emptyMessage)).setText("no data available");
        } else{
            this.adapter.setData(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuakeInfo>> loader) {
        this.adapter.setData(null);
    }
}
