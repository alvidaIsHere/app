/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

public class TopKNextPlaceResult implements Comparable<TopKNextPlaceResult> {

    String semanticPlace;
    int count;

    /**
     * Constructor of the class. As a model to store top k next semantic place
     * and the count
     *
     * @param semanticPlace the semantic place
     * @param count the count for this semantic place
     */
    public TopKNextPlaceResult(String semanticPlace, int count) {
        this.semanticPlace = semanticPlace;
        this.count = count;
    }

    /**
     * getter for semantic place
     *
     * @return semantic place that user visited
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     * getter for count
     *
     * @return number of user visited this semantic place
     */
    public int getCount() {
        return count;
    }

    /**
     * override toString
     *
     * @return a string with format {semanticPlace, count}
     */
    @Override
    public String toString() {
        return "TopKNextPlaceResult{" + "semanticPlace=" + semanticPlace + ", count=" + count + '}';
    }

    /**
     * override compareTo method for sorting purpose
     *
     * @param another topKNextPlaceResult object
     * @return 0 if the semantic place is the same, positive if another object
     * is more lexicographically and negative if another object is less
     * lexicographically
     */
    @Override
    public int compareTo(TopKNextPlaceResult another) {
        return this.getSemanticPlace().compareTo(another.getSemanticPlace());
    }
}
