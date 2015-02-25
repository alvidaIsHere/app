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
public class GroupPopularPlace implements Comparable<GroupPopularPlace> {

    private int rank;
    private String semanticPlace;
    private int count;
    private int groupMemberCount;

    /**
     * This constructor creates a GroupPopularPlace object that stores the rank,
     * location, and list of groups in a location
     *
     * @param rank the rank for this group
     * @param semanticPlace the semantic place for this group
     * @param count the count for this group
     */
    public GroupPopularPlace(int rank, String semanticPlace, int count) {
        this.rank = rank;
        this.semanticPlace = semanticPlace;
        this.count = count;
    }

    /**
     * This constructor creates a GroupPopularPlace object that stores the
     * location, and list of groups in a location
     *
     * @param semanticPlace the semantic place for this group
     * @param count the count for this group
     */
    public GroupPopularPlace(String semanticPlace, int count) {
        this.semanticPlace = semanticPlace;
        this.count = count;
    }
    
        /**
     * This constructor creates a GroupPopularPlace object that stores the rank, location, and list of groups in a location
     * @param rank  rank number of this object
     * @param semanticPlace  semantic place of this object
     * @param count  number of visits for this object
     * @param groupMemberCount  number of group member that visited
     */
    public GroupPopularPlace(int rank, String semanticPlace, int count, int groupMemberCount){
        this.rank = rank;
        this.semanticPlace = semanticPlace;
        this.count = count;
        this.groupMemberCount = groupMemberCount;
    }
    
    /**
     * This constructor creates a GroupPopularPlace object that stores the location, and list of groups in a location
     * @param semanticPlace  the semantic place for this group
     * @param count  the count of the number of groups that has been to this semantic location
     * @param groupMemberCount the number of people in this group
     */
    public GroupPopularPlace(String semanticPlace, int count, int groupMemberCount){
        this.semanticPlace = semanticPlace;
        this.count = count;
        this.groupMemberCount = groupMemberCount;
    }

    /**
     *
     * @return the rank for this group
     */
    public int getRank() {
        return rank;
    }

    /**
     *
     * @param rank the rank for this group
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     *
     * @return the semantic place for this group
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     *
     * @return the count for this group
     */
    public int getCount() {
        return count;
    }
    
    /**
     * 
     * @return  Number of people in this place
     */
    public int getGroupMemberCount(){
        return groupMemberCount;
    }

    /**
     *
     * @param compareGroupPopularPlace another group to compare with
     * @return the count difference
     */
    @Override
    public int compareTo(GroupPopularPlace compareGroupPopularPlace) {
        int compareCount = ((GroupPopularPlace) compareGroupPopularPlace).getCount();
        if (compareCount - this.count == 0) {
            return this.getSemanticPlace().compareTo(compareGroupPopularPlace.getSemanticPlace());
        }
        /* Descending Order */
        return compareCount - this.count;
    }

}
