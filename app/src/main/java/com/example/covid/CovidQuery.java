package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class CovidQuery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_query);

        //Gets you from MainActivity.java
        Intent intent = getIntent();

        String query = intent.getStringExtra("searchQ");
        //query.setText(searchQ); //Sets the passed text to eMail

    }
}