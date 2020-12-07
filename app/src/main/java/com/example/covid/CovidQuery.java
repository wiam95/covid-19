package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

    private ArrayList<CovidEntry> covidList = new ArrayList<>();
    ArrayList<CovidEntry> savedList = new ArrayList<>();

    private MyListAdapter myAdapter;

    String searchQ, query1, query2, query3, query4;
    ListView myList;

    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_query);

        //Find the id
        myList = findViewById(R.id.covidListView);;
        search = findViewById(R.id.refreshList);

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


        //Putting together the query for the API needed to search
        searchQ = query1 + countryName + query2 + startDate + query3 + endDate + query4;
        if (countryName == null || countryName.equals("")) {
            searchQ = "https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z";
        }

        myList.setAdapter( (ListAdapter) ( myAdapter = new MyListAdapter() ) );



        //If you click on a message
        myList.setOnItemLongClickListener((parent, view, position, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to save this entry?")

                    //What is the message:
                    .setMessage("The selected row is " + position + "\nThe database id is " + id)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {

                       //Add this element to saved list
                        savedList.add( covidList.get(position) );

                    })

                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //Show the dialog
                    .create().show();

            return true;

        }); //End of if you click an item in the listView


        //Search button
        search.setOnClickListener(click-> {

            //Two lines of code to get AsyncTask going to retrieve data from a site
            CQuery req = new CQuery(); //Creates a background thread
            req.execute(searchQ); //Type 1
        }); //End of Search button

    } //End of onCreate method

    //Type 1, Type 2, Type 3
    public class CQuery extends AsyncTask< String, Integer, ArrayList<CovidEntry> > {

        protected ArrayList<CovidEntry> doInBackground(String... args) {

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

                    String covidProvince = currentObj.getString("Province");
                    String covidDate = currentObj.getString("Date");
                    int covidCases = currentObj.getInt("Cases");


                    covidList.add( new CovidEntry(covidProvince, covidDate, covidCases) );
                }

            }catch (Exception e) { }

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
        public void onPostExecute(ArrayList<CovidEntry> covidList) {
            //progressBar.setVisibility(View.INVISIBLE);
            //super.onPostExecute(covidList);

            myAdapter.notifyDataSetChanged();

            //This for loop is just for testing that JSON Parse was done right
/*
            for (int i = 0; i < covidList.size(); i++) {
                String temp1 = covidList.get(i).getProv();
                String temp2 = covidList.get(i).getDate();
                int temp3 = covidList.get(i).getCases();
            } //End of testing for loop ----
*/
        }
    } //End of CQuery class

    //MyListAdapter class
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return covidList.size();
        } //Size of arrayList

        public ArrayList<CovidEntry> getItem(int position) { return covidList; } //Gets the item

        public long getItemId(int position) {
            return position;
        } //Gets the ID for the item

        //The view
        public View getView(int position, View old, ViewGroup parent) {

            View newView;

            LayoutInflater inflater = getLayoutInflater();
            //old = inflater.inflate(R.layout.c_province, parent, false);
            newView = inflater.inflate(R.layout.c_province, parent, false);

            TextView pText = newView.findViewById(R.id.provinceTextView);
            pText.setText( covidList.get(position).getProv().toString() );

            TextView pDate = newView.findViewById(R.id.provinceDate);
            pDate.setText( covidList.get(position).getDate() );

            TextView pCases = newView.findViewById(R.id.provinceCases);
            String cases = Integer.toString( covidList.get(position).getCases() );

            pCases.setText( cases );

            /*
            TextView vProv = old.findViewById(R.id.provinceName);
            TextView vDate = old.findViewById(R.id.dateD);
            TextView vCases = old.findViewById(R.id.numCasesC);

            vProv.setText(covidList.get(position).getProv());
            vDate.setText( covidList.get(position).getDate() );
            vCases.setText( covidList.get(position).getCases() );
            */
            //return old;

            return newView;
        }

    } //End of Base adapter class

} //End of CovidQuery class