package com.example.android.quakereport.components;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.quakereport.structs.EarthQuakeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Samsung on 6/18/2017.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuakeInfo>> {
    private final String logTag;
    private final String url;



    public EarthquakeLoader(Context context, String logTag, String url) {
        super(context);
        this.logTag = logTag;
        this.url = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<EarthQuakeInfo> loadInBackground() {
        try {
            String json = networkRequest(url);
            Log.v(logTag, "json="+json);
            return parseJson(json);
        } catch (IOException ex) {
            Log.e(logTag, "ASYNC_ERROR", ex);
        } catch (JSONException e) {
            Log.e(logTag, "ASYNC_ERROR", e);
        }
        return null;
    }



    private String networkRequest(String urlStr) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        Log.v(logTag, "networkRequest url ==> "+urlStr);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            Log.v(logTag, "networkRequest status ==> "+urlConnection.getResponseCode());
            if (urlConnection.getResponseCode() < 400) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(logTag, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(logTag, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {

        Log.v(logTag, "readFromStream");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        int msize = 4096;
        byte[] buf = new byte[msize];
        int s;
        while ((s = inputStream.read(buf, 0, msize)) != -1) {
            b.write(buf, 0, s);
        }
        return new String(b.toByteArray());
    }

    private List<EarthQuakeInfo> parseJson(String json) throws JSONException {
        if(json==null){
            return null;
        }
        JSONObject r = new JSONObject(json);
        JSONArray features = r.getJSONArray("features");
        List<EarthQuakeInfo> list = new ArrayList<>();
        for (int i = 0; i < features.length(); i++) {
            JSONObject f = features.getJSONObject(i);
            JSONObject p = f.getJSONObject("properties");
            list.add(new EarthQuakeInfo(
                    p.getString("title"),
                    p.getDouble("mag"),
                    new Date(p.getLong("time")),
                    p.getString("url")
            ));
        }
        return list;
    }

}
