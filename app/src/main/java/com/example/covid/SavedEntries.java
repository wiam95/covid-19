package com.example.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SavedEntries extends AppCompatActivity {

    MyOpener myOpener;

    ArrayList<CovidEntry> dbData = new ArrayList<>();

    private MyListAdapter myAdapter;

    TextView savedListTitle;
    ListView databaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_entries);

        savedListTitle = findViewById(R.id.savedListTitle);
        databaseList = findViewById(R.id.savedList);

        myOpener = new MyOpener(this);

        databaseList.setAdapter( (ListAdapter) ( myAdapter = new SavedEntries.MyListAdapter() ) );

        fillListView();



        //If you click on a message
        databaseList.setOnItemLongClickListener((parent, view, position, id) -> {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this entry?")


                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {

                        //Remove this entry
                        dbData.remove(position); //Removes from the arrayList

                        myOpener.removeRow(position); //Removes from the database

                        myAdapter.notifyDataSetChanged(); //Notifies the list that a change has occurred

                    })

                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })

                    //Show the dialog
                    .create().show();

            return true;

        }); //End of if you click an item in the listView




    } //End of onCreate method

    private void fillListView() {

        Cursor data = myOpener.getData();

        while(data.moveToNext()) {

            String province = data.getString(1);
            String date = data.getString(2);
            String cases = data.getString(3);

            dbData.add( new CovidEntry(province, date, cases) );

        } //End of while loop

        myAdapter.notifyDataSetChanged();

    } //End of fillListView method


    //MyListAdapter class
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return dbData.size();
        } //Size of arrayList

        public ArrayList<CovidEntry> getItem(int position) { return dbData; } //Gets the item

        //Gets the ID for the item
        public long getItemId(int position) {

            return position;

        }

        //The view
        public View getView(int position, View old, ViewGroup parent) {

            View newView;

            LayoutInflater inflater = getLayoutInflater();

            newView = inflater.inflate(R.layout.c_province, parent, false);

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