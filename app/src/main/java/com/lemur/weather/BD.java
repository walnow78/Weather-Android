package com.lemur.weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pawelwalicki on 26/8/15.
 */
public class BD {

    private GeoSQLiteHelper usdbh;
    private ArrayList<Geolocation> geolocations;

    public BD(Context context){
        usdbh = new GeoSQLiteHelper(context, "Geolocation", null, 1);
        geolocations = new ArrayList<Geolocation>();
    }

    public void InsertGeolocation(Geolocation geolocation) {

        if (!ExistGeolocation(geolocation.getLatitude(), geolocation.getLongitude())){

            SQLiteDatabase db = usdbh.getWritableDatabase();

            if (db != null) {

                ContentValues reg = new ContentValues();

                reg.put("name", geolocation.getName());
                reg.put("latitude", geolocation.getLatitude());
                reg.put("longitude", geolocation.getLongitude());
                reg.put("south", geolocation.getSouth());
                reg.put("north", geolocation.getNorth());
                reg.put("east", geolocation.getEast());
                reg.put("west", geolocation.getWest());

                db.insert("Geolocation", null, reg);

                db.close();

            }

        }
    }

    public boolean ExistGeolocation(double latitude, double longitude){

        SQLiteDatabase db = usdbh.getReadableDatabase();

        if (db != null) {

            String[] args = new String[]{String.valueOf(latitude), String.valueOf(longitude)};

            Cursor cursor = db.rawQuery(" SELECT name FROM Geolocation WHERE latitude=? AND longitude =?", args);

            if (!cursor.moveToFirst()) {
                Log.v("geo", "no existe");
                db.close();
                return false;
            } else {
                Log.v("geo", "existe");
                db.close();
                return true;
            }
        }
        db.close();
        return false;
    }

    public ArrayList<Geolocation> RetriveGeolocations() {

        geolocations.clear();

        SQLiteDatabase db = usdbh.getReadableDatabase();

        if (db != null) {
            String[] columns = new String[]{"name", "description", "latitude", "longitude", "south", "north", "east", "west"};

            Cursor cursor = db.query("Geolocation", columns, null, null, null, null, null);

            if (cursor.moveToFirst()) {

                do {

                    geolocations.add(new Geolocation(cursor.getString(0), cursor.getString(1),
                            cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4),
                            cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7)));

                } while (cursor.moveToNext());

            }

            db.close();
        }

        return geolocations;
    }


}
