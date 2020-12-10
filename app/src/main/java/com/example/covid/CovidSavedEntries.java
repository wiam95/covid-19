package com.example.covid;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


/*
Name: Wiam Assaad
Student ID: 040905209
Course & Section: CST2335 022
Assignment: Final assignment Covid Task
Due: Dec 11 2020
*/


/*
    @Class: CovidSavedEntries
    @extends: AppCompatActivity
    @implements: NavigationView.OnNavigationItemSelectedListener

    This class displays all the saved entries from the database and displays to a list
 */
public class CovidSavedEntries extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CovidMyOpener covidMyOpener;

    ArrayList<CovidEntry> dbData = new ArrayList<>();

    private MyListAdapter myAdapter;

    TextView savedListTitle;
    ListView databaseList;


    /*
        @method: onCreate
        @params: Bundle -saved instance state
        @return: no return value

        This method is called when the class runs
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_saved_entries);

        savedListTitle = findViewById(R.id.savedListTitle);
        databaseList = findViewById(R.id.savedList);

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

        //Sets the adapter
        databaseList.setAdapter( (ListAdapter) ( myAdapter = new CovidSavedEntries.MyListAdapter() ) );

        //Populates the listView with the saved entries from database
        fillListView();


        //If you click on a message
        databaseList.setOnItemLongClickListener((parent, view, position, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Delete this entry?")


                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {

                        //Remove this entry
                        dbData.remove(position); //Removes from the arrayList

                        covidMyOpener.removeRow(position); //Removes from the database

                        myAdapter.notifyDataSetChanged(); //Notifies that a change has occurred

                    })

                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //Show the dialog
                    .create().show();

            return true;

        }); //End of if you click an item in the listView

    } //End of onCreate method


    /*
        @method: onCreateOptionsMenu
        @params: Menu
        @return: boolean -returns true if the menu has inflated

        This method is called when setSupportActionBar is called to inflate the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covid_menu, menu);

        return true;
    }


    /*
        @method: onOptionsItemSelected
        @param: MenuItem
        @returns: boolean

        This method handles what happens when someone clicks on an item on the toolbar
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
                        .setMessage("These are your saved entries. If you wish to delete one, " +
                                "click and hold the entry.")

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
        @params: MenuItem
        @returns: boolean

        This method handles what happens when someone clicks an item on the navigation drawer
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
                        .setMessage("These are your saved entries. If you wish to delete one, " +
                                "click and hold the entry.")

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

    /*
        @method: fillListView
        @params: none
        @returns: no return value

        This method fills the listView using the data from the database by looping through a Cursor
     */
    private void fillListView() {

        Cursor data = covidMyOpener.getData();

        while(data.moveToNext()) {

            String province = data.getString(1);
            String date = data.getString(2);
            String cases = data.getString(3);

            dbData.add( new CovidEntry(province, date, cases) );

        } //End of while loop

        myAdapter.notifyDataSetChanged();

    } //End of fillListView method

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
            return dbData.size();
        } //Size of arrayList

        /*
            @method: getItem
            @param: int -position in the arrayList
            @returns: ArrayList<CovidEntry> -Returns an array list of type class Covid Entry

            This method returns the data at that postion of the arraylist
         */
        public ArrayList<CovidEntry> getItem(int position) { return dbData; } //Gets the item


        /*
            @method: getItemId
            @params: int -position in the arraylist
            @return: long -the id the of that position

            This method returns the id of the item in that position of the arraylist
        */
        public long getItemId(int position) { //Gets the ID for the item

            return position;

        }

        /*
            @method: getView
            @params: int    -position of the item in the arraylist
                     View   -The view
                     ViewGroup -The viewgroup

            This method handles how what data the listview will have in each entry
         */
        public View getView(int position, View old, ViewGroup parent) {

            View newView;

            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.covid_province, parent, false);

            TextView pText = newView.findViewById(R.id.provinceTextView);
            pText.setText( dbData.get(position).getProv() );

            TextView pDate = newView.findViewById(R.id.provinceDate);
            pDate.setText( dbData.get(position).getDate() );

            TextView pCases = newView.findViewById(R.id.provinceCases);
            String cases = dbData.get(position).getCases();

            pCases.setText( cases );

            return newView;
        }

    } //End of Base adapter class


} //End of class SavedEntries