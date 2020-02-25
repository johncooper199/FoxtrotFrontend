package com.example.foxtrotfrontend;

import java.util.Date;

public class NearbyReport {

    private String name;
    private Date date;
    private double latitude;
    private double longitude;
    private int severity;

    public NearbyReport(String n, Date d, double lat, double lon, int s) {
        this.name = n;
        this.date = d;
        this.latitude = lat;
        this.longitude = lon;
        this.severity = s;
    }

    public String getName() { return this.name; }

    public Date getDate() { return this.date; }

    public double getLatitude() { return this.latitude; }

    public double getLongitude() { return this.longitude; }

    public int getSeverity() { return this.severity; }

}
