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
public class HeatMapBreakdown {
    // attribute
    private String status;
    private ArrayList<HeatMap> heatmap;
    
    // constructor
    public HeatMapBreakdown(String status, ArrayList<HeatMap> heatmap)   {
        this.status = status;
        this.heatmap = heatmap;
    }
}
