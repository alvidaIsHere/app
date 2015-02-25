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
public class YearSchoolFilter {
    String year;
    int count;
    ArrayList<SchoolFilter> breakdown;
    // constructor
    public YearSchoolFilter(String year, int count, ArrayList<SchoolFilter> breakdown)   {
        this.year = year;
        this.count = count;
        this.breakdown = breakdown;
    }
}
