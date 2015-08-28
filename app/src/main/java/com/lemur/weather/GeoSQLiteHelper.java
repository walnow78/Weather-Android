package com.lemur.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pawelwalicki on 26/8/15.
 */
public class GeoSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Geolocation (geoId INTEGER, name TEXT, description TEXT, latitude REAL, longitude REAL, south REAL," +
            "north REAL, east REAL, west REAL )";

    public GeoSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS Geolocation");
        db.execSQL(sqlCreate);
    }
}

