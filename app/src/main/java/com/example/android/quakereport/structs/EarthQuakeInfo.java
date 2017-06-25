package com.example.android.quakereport.structs;

import java.util.Date;

/**
 * Created by Samsung on 6/10/2017.
 */
public class EarthQuakeInfo {
    private final String description;
    private final double magnitude;
    private final Date date;
    private final String url;

    public EarthQuakeInfo(String description, double magnitude, Date date, String url) {
        this.description = description;
        this.magnitude = magnitude;
        this.date = date;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public Date getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
