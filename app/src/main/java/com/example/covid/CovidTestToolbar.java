package com.example.covid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class CovidTestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);


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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }



    @Override //Gets called when I used setSupportActionBar() to inflate the menu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        return true;
    }


    @Override //This is when an item on the menu bar is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "You clicked cart";
                break;
            case R.id.item2:
                message = "You clicked credit card";
                break;
            case R.id.item3:
                message = "You clicked handshake";
                break;
            case R.id.item4:
                message = "You clicked item 4";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    //When someone clicks on an item on the navigation drawer
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.item1:
                //message = "You clicked cart";
                Intent chatPage = new Intent(this, CovidMainActivity.class);
                startActivity(chatPage);
                break;
            case R.id.item2:
                //message = "You clicked credit card";
                Intent weatherPage = new Intent(this, CovidMainActivity.class);
                startActivity(weatherPage);
                break;
            case R.id.item3:
                //message = "You clicked handshake";
                Intent loginPage = new Intent(this, CovidMainActivity.class);
                startActivity(loginPage);
                break;
            case R.id.item4:
                message = "You clicked item 4";
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        if (message != null)
            Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_SHORT).show();
        return false;
    }



}