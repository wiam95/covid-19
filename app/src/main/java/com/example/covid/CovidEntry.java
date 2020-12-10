package com.example.covid;

/*
Name: Wiam Assaad
Student ID: 040905209
Course & Section: CST2335 022
Assignment: Final assignment Covid Task
Due: Dec 11 2020
*/

/*
    @class CovidEntry

    This class that holds the entry that we obtained from the JSON pull
 */
public class CovidEntry {
    private String prov;
    private String date;
    private String cases;
    private long dbID;

    /*
         @method: CovidEntry
         @param:  String -The province of the entry
                  String -The date of the entry
                  String -The number of cases for that entry
         @return: no return value
     */
    public CovidEntry(String prov, String date, String cases) {
        this.prov = prov;
        this.date = date;
        this.cases = cases;
    }

    /*
     @method: getProv()
     @returns the value of a private field

     This is a getter method, returns the name of the province
     */
    public String getProv() { return this.prov; }

    /*
     @method: getDate()
     @returns the value of a private field

     This is a getter method, returns the date
     */
    public String getDate() { return this.date; }

    /*
    @method: getCases()
    @returns the value of a private field

    This is a getter method, returns the number of cases
    */
    public String getCases() { return this.cases; }


}
