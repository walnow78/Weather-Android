package com.lemur.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pawelwalicki on 26/8/15.
 */
public class GeoAdapter extends ArrayAdapter {

    private static final String TAG= "GeoAdapter";

    private RequestQueue requestQueue;
    JsonObjectRequest jsArrayRequest;
    ArrayList<Geolocation> items;
    Context context;

    public GeoAdapter(Context context){
        super(context, 0);
        this.context = context;
    }

    public void RetriveFromDB(){
        BD bd = new BD(context);
        items = bd.RetriveGeolocations();
        notifyDataSetChanged();

    }

    public void setFilter(String filterText){

        // Create de queue
        requestQueue= Volley.newRequestQueue(context);

        String URL_BASE ="http://api.geonames.org/searchJSON?q=" + filterText + "&maxRows=20&startRow=0&lang=en&isNameRequired=true&style=FULL&username=ilgeonamessample";

        // Create request
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("geo", response.toString());
                        items = parseJson(response);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());

                    }
                }
        );

        // Add current request to queue.
        requestQueue.add(jsArrayRequest);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listItemView = convertView;

        listItemView = layoutInflater.inflate(R.layout.geo_row, parent, false);

        // Get current item
        Geolocation item = items.get(position);

        // Refresh Views
        TextView textoTitulo = (TextView) listItemView.findViewById(R.id.tv_title);
        TextView textoDescripcion = (TextView) listItemView.findViewById(R.id.tv_subtitle);

        textoTitulo.setText(item.getName());
        textoDescripcion.setText(item.getDescription());

        return listItemView;

    }
    // Parse the json.
    public ArrayList<Geolocation> parseJson(JSONObject jsonObject){
        ArrayList<Geolocation> geolocations = new ArrayList();
        JSONArray jsonArray= null;

        try {
            jsonArray = jsonObject.getJSONArray("geonames");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject geo= jsonArray.getJSONObject(i);

                    double south = 0;
                    double east = 0;
                    double north = 0;
                    double west = 0;

                    String countryName = geo.getString("countryName");
                    String asciiName = geo.getString("asciiName");

                    String geonameId = geo.getString("geonameId");

                    if (geo.has("bbox")) {

                        JSONObject bbox = geo.getJSONObject("bbox");

                        south = bbox.getDouble("south");
                        east = bbox.getDouble("east");
                        north = bbox.getDouble("north");
                        west = bbox.getDouble("west");
                    }

                    double latitude = geo.getDouble("lat");
                    double longitude = geo.getDouble("lng");

                    Geolocation geolocation = new Geolocation(asciiName + "-" + geonameId.toString(), countryName, latitude, longitude, south, north, east, west);

                    geolocations.add(geolocation);

                } catch (JSONException e) {
                    Log.e(TAG, "Error de parsing: "+ e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return geolocations;
    }
}
