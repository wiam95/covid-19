package com.example.covid;

public class CovidEntry {
    private String prov;
    private String date;
    private String cases;

    public CovidEntry(String prov, String date, String cases) {
        this.prov = prov;
        this.date = date;
        this.cases = cases;
    }

    public String getProv() { return this.prov; }
    public String getDate() { return this.date; }
    public String getCases() { return this.cases; }
}
