package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CovidQuery extends AppCompatActivity {

    //https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z

    //ProgressBar progressBar;

    ArrayList<String> covidList = new ArrayList<String>();
    private MyListAdapter myAdapter;

    String searchQ, query1, query2, query3, query4;
    TextView provinceName, dateD, numCasesC;
    ListView myList;

    TextView testing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_query);

        //Find the id
        provinceName = findViewById(R.id.provinceName);
        dateD = findViewById(R.id.dateD);
        numCasesC = findViewById(R.id.numCasesC);
        myList = findViewById(R.id.covidListView);

        testing = findViewById(R.id.testingSucks);

        myList.setAdapter( (ListAdapter) ( myAdapter = new MyListAdapter() ) );

        //progressBar.setVisibility(View.VISIBLE);

        //Part of the API link (part that the user does not alter)
        query1 = "https://api.covid19api.com/country/";
        query2 = "/status/confirmed/live?from=";
        query3 = "T00:00:00Z&to=";
        query4 = "T00:00:00Z";

        //Gets you from MainActivity.java
        Intent intent = getIntent();

        //Obtained from previous intent (page)
        String countryName = intent.getStringExtra("country");
        String startDate = intent.getStringExtra("dateStart");
        String endDate = intent.getStringExtra("dateEnd");

        myAdapter.notifyDataSetChanged();

        //Putting together the query for the API needed to search
        searchQ = query1 + countryName + query2 + startDate + query3 + endDate + query4;

        //query.setText(searchQ); //Sets the passed text to eMail

        //Two lines of code to get AsyncTask going to retrieve data from a site
        CQuery req = new CQuery(); //Creates a background thread
        req.execute(searchQ); //Type 1
        //req.execute("https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z");



    }

    //Type 1, Type 2, Type 3
    public class CQuery extends AsyncTask< String, Integer, ArrayList<String> > {

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

                    combined += Cprov + ", ";
                    combined += Cdate + ", ";
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

            //progressBar.setVisibility(View.INVISIBLE);

        }

    } //End of CQuery class


    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return covidList.size();
        } //Size of arrayList

        public String getItem(int position) { return covidList.get(position); } //Gets the item

        public long getItemId(int position) {
            return position;
        } //Gets the ID for the item

        //The view
        public View getView(int position, View old, ViewGroup parent) {

            View newView;
            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.c_list, parent, false);


            //set what should be in this row:

            String temp = covidList.get(position);

            String tempProvince = "";
            String tempDate = "";
            String tempNumCases = "";

            String[] arrSplit = temp.split(", ");
            for (int i=0; i < arrSplit.length; i++)
            {
               if (i == 0)
                   tempProvince += arrSplit[0];
               if (i == 1)
                   tempDate += arrSplit[1];
               if (i == 2)
                   tempNumCases += arrSplit[2];
            }

            provinceName.setText(tempProvince);
            dateD.setText(tempDate);
            numCasesC.setText(tempNumCases);

            //return it to be put in the table
            return newView;

        }

    } //End of Base adapter class

} //End of CovidQuery class