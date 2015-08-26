package com.lemur.weather;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends FragmentActivity {

    private GoogleMap mMap;
    private Geolocation geo;
    private static final String TAG= "Weather";
    private RequestQueue requestQueue;
    private TextView tv_temp_progress, tv_temp_text;

    JsonObjectRequest jsArrayRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_temp_progress = (TextView) findViewById(R.id.tv_temp_progress);
        tv_temp_text = (TextView) findViewById(R.id.tv_temp_text);


        Intent intent = getIntent();
        geo = (Geolocation) intent.getSerializableExtra("geo");

        // Create de queue
        requestQueue= Volley.newRequestQueue(getApplicationContext());

        String URL_BASE = "http://api.geonames.org/weatherJSON?north=" + String.valueOf(geo.getNorth()) +
                "&south=" + String.valueOf(geo.getSouth()) +
                "&east=" + String.valueOf(geo.getEast()) +
                "&west=" + String.valueOf(geo.getWest()) +
                "&username=ilgeonamessample";

        // Create request
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        double temperature = parseJson(response);

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

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        LatLng latLng = new LatLng(geo.getLatitude(), geo.getLongitude());

        mMap.addMarker(new MarkerOptions().position(latLng).title(geo.getName()).snippet(geo.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
    }

    public double parseJson(JSONObject jsonObject){

        JSONArray jsonArray= null;
        double temperature = 0;

        try {
            jsonArray = jsonObject.getJSONArray("weatherObservations");

            for(int i=0; i<jsonArray.length(); i++) {
                temperature += jsonArray.getJSONObject(i).getDouble("temperature");
            }

            if (jsonArray.length() > 0){
                temperature = temperature / jsonArray.length();

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int widthPixels = metrics.widthPixels;

                int value = (int) (widthPixels * temperature / 50);

                ResizeWidthAnimation anim = new ResizeWidthAnimation(tv_temp_progress, value);
                anim.setDuration(1000);
                tv_temp_progress.startAnimation(anim);

                tv_temp_text.setText("Temp: " + String.valueOf(temperature) + "C" );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temperature;

    }

    public class ResizeWidthAnimation extends Animation
    {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public ResizeWidthAnimation(View view, int width)
        {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

            mView.getLayoutParams().width = newWidth;
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight)
        {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds()
        {
            return true;
        }
    }

}
