package com.lemur.weather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView lv_geo;
    private GeoAdapter geoadapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        lv_geo = (ListView) findViewById(R.id.lv_geo);

        geoadapter = new GeoAdapter(this);
        lv_geo.setAdapter(geoadapter);

        geoadapter.RetriveFromDB();

        lv_geo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BD bd = new BD(context);

                bd.InsertGeolocation(geoadapter.items.get(position));

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                intent.putExtra("geo", geoadapter.items.get(position));

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            Intent intent = new Intent(MainActivity.this, SuggestionActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

