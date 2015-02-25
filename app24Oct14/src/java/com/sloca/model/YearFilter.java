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
public class YearFilter {
    private int year;
    private int count;
    private ArrayList<YearGenderFilter> breakdown;

    // constructor
    public YearFilter(int year, int count)  {
        this.year = year;
        this.count = count;
    }
    // overload
    public YearFilter(int year, int count, ArrayList<YearGenderFilter> genderBreakdown) {
        this(year, count);
        this.breakdown = genderBreakdown;
    }
    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the genderBreakdown
     */
    public ArrayList<YearGenderFilter> getGenderBreakdown() {
        return breakdown;
    }

    /**
     * @param genderBreakdown the genderBreakdown to set
     */
    public void setGenderBreakdown(ArrayList<YearGenderFilter> genderBreakdown) {
        this.breakdown = genderBreakdown;
    }
    
}
