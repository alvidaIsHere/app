/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import java.util.Comparator;

/**
 *
 * @author User
 */
public class TopKNextPlaceResult implements Comparable<TopKNextPlaceResult> {

    String semanticPlace;
    int count;

    public TopKNextPlaceResult(String semanticPlace, int count) {
        this.semanticPlace = semanticPlace;
        this.count = count;

    }

    public String getSemanticPlace() {
        return semanticPlace;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "TopKNextPlaceResult{" + "semanticPlace=" + semanticPlace + ", count=" + count + '}';
    }

    @Override
    //to compare name in alphabetical order
    public int compareTo(TopKNextPlaceResult o) {
        return this.getSemanticPlace().compareTo(o.getSemanticPlace());
    }
}
