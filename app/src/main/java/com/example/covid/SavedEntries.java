package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedEntries extends AppCompatActivity {

    ArrayList<CovidEntry> savedList;

    ListView databaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_entries);

        databaseList.findViewById(R.id.databaseList);


    }
}