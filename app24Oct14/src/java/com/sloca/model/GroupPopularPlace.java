/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

/**
 *
 * @author jeremy.kuah.2013
 */
public class GroupPopularPlace implements Comparable<GroupPopularPlace>{
    
    private int rank;
    private String semanticPlace;
    private int count;
    
    public GroupPopularPlace(int rank, String semanticPlace, int count){
        this.rank = rank;
        this.semanticPlace = semanticPlace;
        this.count = count;
    }
    
    public GroupPopularPlace(String semanticPlace, int count){
        this.semanticPlace = semanticPlace;
        this.count = count;
    }

    public int getRank() {
        return rank;
    }
    
    public void setRank(int rank){
        this.rank = rank;
    }

    public String getSemanticPlace() {
        return semanticPlace;
    }

    public int getCount() {
        return count;
    }
    
    @Override
    public int compareTo(GroupPopularPlace compareGroupPopularPlace) {
        int compareCount = ((GroupPopularPlace)compareGroupPopularPlace).getCount();
        
        /* Descending Order */
        return compareCount - this.count;
    }
    
}
