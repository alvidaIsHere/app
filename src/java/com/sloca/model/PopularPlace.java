/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

public class PopularPlace implements Comparable<PopularPlace> {

    private final int rank;
    private final String semanticPlace;
    private final int count;

    /**
     * A PopularPlace object represents a location with its rank, that is
     * determined by the number of visits
     *
     * @param rank an integer value that ranges from 1 to 10 that indicates the
     * rank of the object
     * @param semanticPlace name of location
     * @param count number of people that visited the location
     */
    public PopularPlace(int rank, String semanticPlace, int count) {
        this.rank = rank;
        this.semanticPlace = semanticPlace;
        this.count = count;
    }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @return the semanticPlace
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Method to compare popular place object, sorting them first by the number
     * of count, then by alphabetical order
     *
     * @param comparePopularPlace
     * @return 0 if parameter's count and semantic place is the same. Negative
     * number if count is lesser or semantic place is lesser lexicographically.
     * Positive number if count is more or semantic place is more
     * lexicographically.
     */
    @Override
    public int compareTo(PopularPlace comparePopularPlace) {
        int compareCount = ((PopularPlace) comparePopularPlace).getCount();
        if (compareCount - this.count == 0) {
            return this.getSemanticPlace().compareTo(comparePopularPlace.getSemanticPlace());
        }
        /* Descending Order */
        return compareCount - this.count;
    }

}
