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

        if (!ExistGeolocation(geolocation.getGeoId())){

            SQLiteDatabase db = usdbh.getWritableDatabase();

            if (db != null) {

                ContentValues reg = new ContentValues();

                reg.put("geoId", geolocation.getGeoId());
                reg.put("name", geolocation.getName());
                reg.put("description", geolocation.getDescription());
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

    public boolean ExistGeolocation(int geoId){

        SQLiteDatabase db = usdbh.getReadableDatabase();

        if (db != null) {

            String[] args = new String[]{String.valueOf(geoId)};

            Cursor cursor = db.rawQuery(" SELECT name FROM Geolocation WHERE geoId =?", args);

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
            try {
                String[] columns = new String[]{"geoId", "name", "description", "latitude", "longitude", "south", "north", "east", "west"};

                Cursor cursor = db.query("Geolocation", columns, null, null, null, null, null);

                if (cursor.moveToFirst()) {

                    do {

                        geolocations.add(new Geolocation(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5),
                                cursor.getDouble(6), cursor.getDouble(7), cursor.getDouble(8)));

                    } while (cursor.moveToNext());

                }

                db.close();
            }catch(Exception ex){
                Log.v("geo", ex.getLocalizedMessage());
            }
        }

        return geolocations;
    }


}
