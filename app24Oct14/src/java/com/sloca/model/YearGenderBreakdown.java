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
public class YearGenderBreakdown {
    String status;
    ArrayList<YearGenderFilter> breakdown;
    public YearGenderBreakdown(String status, ArrayList<YearGenderFilter> breakdown)    {
        this.status = status;
        this.breakdown = breakdown;
    }
}
