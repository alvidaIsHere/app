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
public class YearBreakdown {
    String status;
    ArrayList<YearFilter> breakdown;
    // constructor
    public YearBreakdown(String status, ArrayList<YearFilter> breakdown)    {
        this.status = status;
        this.breakdown = breakdown;
    }
}
