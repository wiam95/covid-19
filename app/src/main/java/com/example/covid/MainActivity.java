package com.example.covid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button search, goToSavedEntries;
    EditText countryName, fromDate, toDate;

    ProgressBar pbCovid;

    public static final String ACTIVITY_NAME = "MAIN_ACTIVITY";

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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



        //shared preferences
        prefs = getSharedPreferences("fileName", MODE_PRIVATE);

        String savedCountry = prefs.getString("CName", "Canada");
        String date1 = prefs.getString("Date1", "2020-10-14");
        String date2 = prefs.getString("Date2", "2020-10-15");

        //Finds the xml by id
        search = findViewById(R.id.search);
        goToSavedEntries = findViewById(R.id.goToSavedEntries);
        countryName = (EditText) findViewById(R.id.countryNamePrompt);
        fromDate = (EditText) findViewById(R.id.fromDatePrompt);
        toDate = (EditText) findViewById(R.id.toDatePrompt);
        pbCovid = (ProgressBar) findViewById(R.id.pbCovid);

        countryName.setText(savedCountry);
        fromDate.setText(date1);
        toDate.setText(date2);

        //for the progressbar
        pbCovid.setVisibility(View.INVISIBLE);


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



            //Done for the progressbar
            pbCovid.setVisibility(View.VISIBLE);
            for (int i = 0; i < 100; i += 10) {
                pbCovid.setProgress(i);
                SystemClock.sleep(100);
            }
            startActivity(covidPage);

        });

        //This creates a transition to load CovidQuery
        Intent savedEntriesPage = new Intent(this, SavedEntries.class);

        //When you click the search button, start the next activity
        goToSavedEntries.setOnClickListener( click ->
        {
            startActivity(savedEntriesPage);
        });



    }

    @Override //Gets called when I used setSupportActionBar() to inflate the menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        return true;
    }


    @Override //What to do when you click on an item from the toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "You clicked music task";
                break;

            case R.id.item2:
                message = "You clicked recipe task";
                break;

            case R.id.item3:
                message = "You clicked go to main activity";
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;

            case R.id.item4:
                message = "You clicked go to saved entries";
                Intent savedPage = new Intent(this, SavedEntries.class);
                startActivity(savedPage);
                break;

            case R.id.itemHelp:
                message = "You clicked on help";

                //Snackbar
                Snackbar.make(search, "Click DONE to proceed", Snackbar.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Instructions")

                        //Message has the instructions for this page
                        .setMessage("Select a country, start date and end date that you wish to " +
                                "search for then press submit. \nThis will redirect you to the next " +
                                "page to present you the covid results for the data you entered.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

            break;

        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }



    // Needed for the OnNavigationItemSelected interface:
    //When someone clicks on an item on the navigation drawer
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.item1:
                //message = "You clicked cart";
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;

            case R.id.item2:
                //message = "You clicked credit card";
                Intent weatherPage = new Intent(this, MainActivity.class);
                startActivity(weatherPage);
                break;

            case R.id.item3:
                message = "You clicked go to main activity";
                Intent loginPage = new Intent(this, MainActivity.class);
                startActivity(loginPage);
                break;

            case R.id.item4:
                message = "You clicked go to saved entries";
                Intent savedPage = new Intent(this, SavedEntries.class);
                startActivity(savedPage);
                break;

            case R.id.itemHelp:
                message = "You clicked on help";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Instructions")

                        //Message has the instructions for this page
                        .setMessage("Select a country, start date and end date that you wish to " +
                                "search for then press submit. \nThis will redirect you to the next " +
                                "page to present you the covid results for the data you entered.")

                        //what the Yes button does:
                        .setPositiveButton("Done", (click, arg) -> { })

                        //Show the dialog
                        .create().show();

                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        if (message != null)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return false;
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