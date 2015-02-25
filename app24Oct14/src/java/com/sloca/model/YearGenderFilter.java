/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import java.util.ArrayList;

/**
 *
 * @author alice
 */
public class YearGenderFilter {
    String gender;
    int count;
    private ArrayList<YearSchoolFilter> breakdown;
    // constructor
    public YearGenderFilter(String gender, int count) {
        this.gender = gender;
        this.count = count;
    }
    // overload
    public YearGenderFilter(String gender, int count, ArrayList<YearSchoolFilter> breakdown)   {
        this(gender, count);
        this.breakdown = breakdown;
    }

    /**
     * @return the breakdown
     */
    public ArrayList<YearSchoolFilter> getBreakdown() {
        return breakdown;
    }

    /**
     * @param breakdown the breakdown to set
     */
    public void setBreakdown(ArrayList<YearSchoolFilter> breakdown) {
        this.breakdown = breakdown;
    }
}
