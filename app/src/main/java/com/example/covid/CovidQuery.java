package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CovidQuery extends AppCompatActivity {

    //https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z

    //ProgressBar progressBar;

    String searchQ, query1, query2, query3, query4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_query);



        //progressBar.setVisibility(View.VISIBLE);

        query1 = "https://api.covid19api.com/country/";
        query2 = "/status/confirmed/live?from=";
        query3 = "T00:00:00Z&to=";
        query4 = "T00:00:00Z";

        //Gets you from MainActivity.java
        Intent intent = getIntent();

        String countryName = intent.getStringExtra("country");
        String startDate = intent.getStringExtra("dateStart");
        String endDate = intent.getStringExtra("dateEnd");

        searchQ = query1 + countryName + query2 + startDate + query3 + endDate + query4;

        //query.setText(searchQ); //Sets the passed text to eMail

        //Two lines of code to get AsyncTask going to retrieve data from a site
        CQuery req = new CQuery(); //Creates a background thread
        req.execute(searchQ); //Type 1
        //req.execute("https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z");

    }

    //Type 1, Type 2, Type 3
    public class CQuery extends AsyncTask< String, Integer, ArrayList<String> > {

        ArrayList<String> covidList = new ArrayList<String>();

        protected ArrayList<String> doInBackground(String... args) {

            try {

                //Create URL object of what server to contact
                URL url = new URL(args[0]);

                //Open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON --------------------

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while((line = reader.readLine()) != null) {

                    sb.append(line + "\n");

                }
                String result = sb.toString(); //result is the whole string

                //Convert the string to a JSON Object
                //JSONObject jObject = new JSONObject(result);
                  //array                             //the whole string
                JSONArray covidData = new JSONArray(result);

                //Loops through the JSON Array to obtain all the elements (in this case its JSONObjects)
                for(int i = 0; i < covidData.length(); i++) {

                    //This will get the JSON Object at that position in the array
                    JSONObject currentObj = covidData.getJSONObject(i);

                    String Cprov = currentObj.getString("Province");
                    String Cdate = currentObj.getString("Date");
                    int cCases = currentObj.getInt("Cases");


                    String combined = "";

                    combined += Cprov + "\n";
                    combined += Cdate + "\n";
                    combined += cCases;

                    //Each array element is now a single string seperated by newline
                    covidList.add(combined);
                }


            }catch (Exception e) { }

            //province.add("InsideDoInBackground");
            //cases.add(2);
            return covidList;
        }

        //(Where you update your GUI)
        //Type 2
        public void onProgressUpdate(Integer ... args) {

           // progressBar.setVisibility(View.VISIBLE);
            //SystemClock.sleep(300);
           // progressBar.setProgress(args[0]);
        }

        //Type 3
        public void onPostExecute(ArrayList<String> covidList) {

            //test1.setText(covidList.get(3));

            //progressBar.setVisibility(View.INVISIBLE);

        }

    }

}