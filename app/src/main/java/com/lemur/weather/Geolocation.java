package com.lemur.weather;

import android.util.EventLogTags;

import java.io.Serializable;

/**
 * Created by pawelwalicki on 26/8/15.
 */

public class Geolocation implements Serializable {

    private int geoId;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private double south;
    private double north;
    private double east;
    private double west;

    public Geolocation(int geoId, String name, String description, double latitude, double longitude, double south, double north, double east, double west) {
        this.geoId = geoId;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.south = south;
        this.north = north;
        this.east = east;
        this.west = west;
    }

    public int getGeoId() {
        return geoId;
    }

    public void setGeoId(int geoId) {
        this.geoId = geoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }
}


