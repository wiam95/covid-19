package com.example.covid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/*
Name: Wiam Assaad
Student ID: 040905209
Course & Section: CST2335 022
Assignment: Final assignment Covid Task
Due: Dec 11 2020
*/



/*
    @Class: CovidQuery
    @extends: AppCompatActivity
    @implements: NavigationView.OnNavigationItemSelectedListener

    This class handles the query, pulls the data from the site, and displays the data on a list
 */
public class CovidQuery extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z

    private ArrayList<CovidEntry> covidList = new ArrayList<>();
    ArrayList<CovidEntry> savedList = new ArrayList<>();

    private MyListAdapter myAdapter;

    CovidMyOpener covidMyOpener;

    String searchQ, query1, query2, query3, query4;
    ListView myList;

    Button search;


    /*
        @method: onCreate
        @params: Bundle -saved instance state
        @returns: no return value

        This method is called when this class run for the first time
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_query);

        //Find the id
        myList = findViewById(R.id.covidListView);;
        search = findViewById(R.id.refreshList);

        covidMyOpener = new CovidMyOpener(this);

        //This gets the toolbar from the layout
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar); //This makes Android call onCreateOptionsMenu()

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar,R.string.open, R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Part of the API link (part that the user does not alter)
        query1 = "https://api.covid19api.com/country/";
        query2 = "/status/confirmed/live?from=";
        query3 = "T00:00:00Z&to=";
        query4 = "T00:00:00Z";

        //Gets you from MainActivity.java
        Intent intent = getIntent();

        //Obtained from previous intent (covidpage)
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
            alertDialogBuilder.setTitle("Save this entry?")

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {

                       //Add this element to saved list
                        savedList.add( covidList.get(position) );

                        //Adds this entry to the database
                        addData(position);

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

            clearListView();

            //Two lines of code to get AsyncTask going to retrieve data from a site
            CQuery req = new CQuery(); //Creates a background thread
            req.execute(searchQ); //Type 1 (This starts AsyncTask)
        }); //End of Search button

    } //End of onCreate method


    /*
       @method: onCreateOptionsMenu
       @params: Menu
       @return: boolean -returns true if the menu has inflated

       This method is called when setSupportActionBar() is used to inflate the menu
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covid_menu, menu);

        return true;
    }

    /*
        @method: onOptionsItemSelected
        @params: MenuItem -The item on the toolbar that is selected
        @return: boolean

         This method handles what happens when an item on the toolbar is clicked
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "music";
                break;

            case R.id.item2:
                message = "recipe";
                break;

            case R.id.item3:
                message = "home";
                //This creates a transition to load CovidQuery
                Intent mainPage = new Intent(this, CovidMainActivity.class);
                startActivity(mainPage);
                break;

            case R.id.item4:
                message = "saved entries";
                Intent savedPage = new Intent(this, CovidSavedEntries.class);
                startActivity(savedPage);
                break;

            case R.id.itemHelp:
                message = "help";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Instructions")

                        //Message has the instructions for this covidpage
                        .setMessage("Press the SEARCH button and the data you requested " +
                                "will be displayed here using a list. \n\nIf you wish " +
                                "to covidsave an entry, click and hold an entry to covidsave it to your " +
                                "SAVED ENTRIES list.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        return true;
    }


    /*
        @method: onNavigationItemSelected
        @params: MenuItem   -The item that is selected on the navigation drawer
        @return: boolean

        This method handles what happens when an item on the navigation drawer is selected
    */
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.item1:
                message = "music";
                Intent mainPage = new Intent(this, CovidMainActivity.class);
                startActivity(mainPage);
                break;
            case R.id.item2:
                message = "recipe";
                Intent weatherPage = new Intent(this, CovidMainActivity.class);
                startActivity(weatherPage);
                break;
            case R.id.item3:

                message = "home";
                Intent loginPage = new Intent(this, CovidMainActivity.class);
                startActivity(loginPage);
                break;
            case R.id.item4:
                message = "saved entries";
                Intent savedPage = new Intent(this, CovidSavedEntries.class);
                startActivity(savedPage);
                break;

            case R.id.itemHelp:
                message = "help";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Instructions")

                        //Message has the instructions for this covidpage
                        .setMessage("Press the SEARCH button and the data you requested " +
                                "will be displayed here using a list. \n\nIf you wish " +
                                "to covidsave an entry, click and hold an entry to covidsave it to your " +
                                "SAVED ENTRIES list.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        if (message != null)
            Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
        return false;
    }

    /*
        @method: addData
        @params: int -position of the item in the arraylist
        @return: no return value

        This method adds an entry to the database
     */
    public void addData(int position) {

       boolean isSaved;

       isSaved = covidMyOpener.insertEntry( covidList.get(position).getProv(),
                covidList.get(position).getDate(), covidList.get(position).getCases() );

       if (isSaved) {
           Toast.makeText(CovidQuery.this, getResources().getString(R.string.db_success) , Toast.LENGTH_SHORT).show();
       }else {
           Toast.makeText(CovidQuery.this, getResources().getString(R.string.db_failure) , Toast.LENGTH_SHORT).show();
       }

    }

    /*
        @Class: CQuery
        @extends: AsyncTask< String, Integer, ArrayList<CovidEntry> >

        This class handles the pulling of data from the site and saving the data to the arrayList
     */
                                             //Type 1, Type 2, Type 3
    public class CQuery extends AsyncTask< String, Integer, ArrayList<CovidEntry> > {

        /*
            @method: doInBackground
            @params: (Strings... args)
            @return: ArrayList<CovidEntry>

            This method runs in the background and pulls the data from the site and stores it to the arraylist
         */
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

                    String covidC = Integer.toString(covidCases);

                    covidList.add( new CovidEntry(covidProvince, covidDate, covidC) );
                }

            }catch (Exception e) { }

            return covidList;
        }

        /*
            @method: onProgressUpdate
            @params: Integer ... args
            @return: no return value

            This method is where you can update your GUI while doInBackground is happening
         */
        //Type 2
        public void onProgressUpdate(Integer ... args) {  }


        /*
            @method: onPostExecute
            @params: ArrayList<CovidEntry>
            @returns: no return value

            This method happens after doInBackground is done with its task
         */
        //Type 3
        public void onPostExecute(ArrayList<CovidEntry> covidList) {

            myAdapter.notifyDataSetChanged();

        }
    } //End of CQuery class


    /*
        @method: clearListView
        @params: none
        @return: no return value

        This method clears the list of queries
     */
    public void clearListView()
    {
        covidList.clear();
        myAdapter.notifyDataSetChanged();
    }


    /*
        @Class: MyListAdapter
        @extends: BaseAdapter

        This class handles how the our custom listview will appear
     */
    //MyListAdapter class
    private class MyListAdapter extends BaseAdapter {

        /*
            @method: getCount
            @return: int -size of the arrayList

            This method returns the size of the arraylist to be displayed in the list
         */
        public int getCount() {
            return covidList.size();
        } //Size of arrayList

        /*
            @method: getItem
            @param: int -position in the arrayList
            @returns: ArrayList<CovidEntry> -Returns an array list of type class Covid Entry

            This method returns the data at that postion of the arraylist
         */
        public ArrayList<CovidEntry> getItem(int position) { return covidList; } //Gets the item

        /*
            @method: getItemId
            @params: int -position in the arraylist
            @return: long -the id the of that position

            This method returns the id of the item in that position of the arraylist
        */
        public long getItemId(int position) {
            return position;
        } //Gets the ID for the item

        /*
            @method: getView
            @params: int    -position of the item in the arraylist
                     View   -The view
                     ViewGroup -The viewgroup

            This method handles how what data the listview will have in each entry
         */
        //The view
        public View getView(int position, View old, ViewGroup parent) {

            View newView;

            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.covid_province, parent, false);

            TextView pText = newView.findViewById(R.id.provinceTextView);
            pText.setText( covidList.get(position).getProv() );

            TextView pDate = newView.findViewById(R.id.provinceDate);
            pDate.setText( covidList.get(position).getDate() );

            TextView pCases = newView.findViewById(R.id.provinceCases);
            String cases = covidList.get(position).getCases();

            pCases.setText( cases );

            return newView;
        }

    } //End of Base adapter class

} //End of CovidQuery class