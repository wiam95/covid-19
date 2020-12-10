package com.example.covid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/*
Name: Wiam Assaad
Student ID: 040905209
Course & Section: CST2335 022
Assignment: Final assignment Covid Task
Due: Dec 11 2020
*/

/*
    @Class: CovidMyOpener
    @Extends: SQLiteOpenHelper

    This class handles the database SQL used in the assignment
 */
public class CovidMyOpener extends SQLiteOpenHelper {


    protected final static String DATABASE_NAME = "SavedCovidEntries";
    protected final static int VERSION_NUM = 1;

    public final static String TABLE_NAME = "SAVEDENTRIES";
    public final static String COL_PROVINCE = "PROVINCE";
    public final static String COL_DATE = "DATE";
    public final static String COL_CASES = "NUMCASES";
    public final static String COL_ID = "_id";


    /*
    @method: CovidMyOpener
    @params: Context -The context

    This method is the constructor for this class
     */
    public CovidMyOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /*
        @method: onCreate
        @params: SQLiteDatabase  -It takes an SQLitedatabase to be used
        @returns: no return value

        This method creates the table in the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //**If you change anything in this method you must increment the VERSION_NUM to force rebuild**
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PROVINCE + " text,"
                + COL_DATE + " text,"
                + COL_CASES + " text);");

    }

    /*
        @method: onUpgrade
        @params: SQLiteDatabase  -It takes an SQLitedatabase to be used
                 oldVersion     -The old database version number
                 newVersion     -The new database version number
        @returns: no return value

        This method drops the old table and replaces it with the newer version
     */
    @Override //onUpgrade method
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table
        onCreate(db);

    }


    /*
        @method: onDowngrade
        @params: SQLiteDatabase  -It takes an SQLitedatabase to be used
                 oldVersion     -The old database version number
                 newVersion     -The new database version number
        @returns: no return value

        This method drops the new table and replaces it with an older version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table
        onCreate(db);

    }

   /*
        @method: insertEntry
        @params: province -the province name
                date -the date
                numCases - number of cases
        @return: boolean -returns true if the entry is saved in the table
                         -returns false if the entry is not saved in the table

        This method adds a new entry to the table
    */
    public boolean insertEntry(String province, String date, String numCases) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_PROVINCE, province);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_CASES, numCases);

        long entryID = db.insert("SAVEDENTRIES", null, contentValues);

        if (entryID == -1) {
            return false;
        }
        return true;
    }

    /*
        @method: deleteWithoutID
        @params: String -province name
                 String -date
                 String -number of cases
        @returns: no return value

        This method removes the entry from the database
     */
    public void deleteWithoutID(String province, String date, String cases) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete( "SAVEDENTRIES", "PROVINCE = ? AND " +
                "DATE = ? AND NUMCASES = ?", new String[]{ province, date, cases } );

    }


    /*
    @method: getData
    @params: no param
    @returns: Cursor -To be able to iterate through the contents of the table

    This method gets the data from the table and returns a cursor
     */
    public Cursor getData() {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

} //End of MyOpener class



