package com.example.covid;

public class CovidEntry {
    private String prov;
    private String date;
    private int cases;

    public CovidEntry(String prov, String date, int cases) {
        this.prov = prov;
        this.date = date;
        this.cases = cases;


    }

    public String getProv() { return this.prov; }
    public String getDate() { return this.date; }
    public int getCases() { return this.cases; }
}
