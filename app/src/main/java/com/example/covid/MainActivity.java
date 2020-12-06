package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button search;
    EditText countryName, fromDate, toDate;

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared preferences
        prefs = getSharedPreferences("fileName", MODE_PRIVATE);

        String savedCountry = prefs.getString("CName", "Country Name");
        String date1 = prefs.getString("Date1", "2020-01-10");
        String date2 = prefs.getString("Date2", "2020-11-25");

        search = findViewById(R.id.search);
        countryName = (EditText) findViewById(R.id.countryNamePrompt);
        fromDate = (EditText) findViewById(R.id.fromDatePrompt);
        toDate = (EditText) findViewById(R.id.toDatePrompt);

        countryName.setText(savedCountry);
        fromDate.setText(date1);
        toDate.setText(date2);


        //This creates a transition to load CovidQuery
        Intent covidPage = new Intent(this, CovidQuery.class);

        //When you click the search button, start the next activity
        search.setOnClickListener( click ->
        {
            String countryNamePrompt = countryName.getText().toString();
            String fromDatePrompt = fromDate.getText().toString();
            String toDatePrompt = toDate.getText().toString();

            covidPage.putExtra("country", countryNamePrompt);
            covidPage.putExtra("dateStart", fromDatePrompt);
            covidPage.putExtra("dateEnd", toDatePrompt);

            startActivity(covidPage);
        });

    }

    //Adds the string to saved prefs
    private void saveSharedPrefs(String savedCountry, String date1, String date2) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CName", savedCountry);
        editor.putString("Date1", date1);
        editor.putString("Date2", date2);

        editor.commit();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs( countryName.getText().toString(),
                fromDate.getText().toString(), toDate.getText().toString() );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}