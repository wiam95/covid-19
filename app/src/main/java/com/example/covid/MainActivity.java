package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button search;
    EditText countryName, fromDate, toDate;


    //https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z
    String searchQ, query1, query2, query3, query4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        countryName = findViewById(R.id.countryNamePrompt);
        fromDate = findViewById(R.id.fromDatePrompt);
        toDate = findViewById(R.id.toDatePrompt);

        query1 = "https://api.covid19api.com/country/";
        query2 = "/status/confirmed/live?from=";
        query3 = "T00:00:00Z&to=";
        query4 = "T00:00:00Z";
        
        searchQ = query1 + countryName + query2 + fromDate + query3 + toDate + query4;

        //This creates a transition to load CovidQuery
        Intent nextPage = new Intent(this, CovidQuery.class);

        //When you click the search button, start the next activity
        search.setOnClickListener( click ->
        {
            nextPage.putExtra("searchQ", searchQ);

            startActivity(nextPage);
        });

    }
}