package com.lemur.weather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class SuggestionActivity extends AppCompatActivity {

    private ListView lv_geo;
    private EditText et_filter;
    private GeoAdapter geoadapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        context = this;

        lv_geo = (ListView) findViewById(R.id.lv_geo);
        et_filter = (EditText) findViewById(R.id.et_filter);

        geoadapter = new GeoAdapter(this);
        lv_geo.setAdapter(geoadapter);

        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    geoadapter.setFilter(s.toString());
                }
            }
        });

        lv_geo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BD bd = new BD(context);

                bd.InsertGeolocation(geoadapter.items.get(position));

                Intent intent = new Intent(SuggestionActivity.this, DetailActivity.class);

                intent.putExtra("geo", geoadapter.items.get(position));

                startActivity(intent);

                finish();

            }
        });
    }
}
