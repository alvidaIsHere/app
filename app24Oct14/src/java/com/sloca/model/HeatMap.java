/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

/**
 *
 * @author alice
 */
public class HeatMap {
    // attributes
    private String semantic_place;
    private int num_people;
    private int crowd_density;
    
    // constructor
    public HeatMap(String semantic_place, int num_people, int crowd_density)    {
        this.semantic_place = semantic_place;
        this.num_people = num_people;
        this.crowd_density = crowd_density;
    }
    
    // tostring for debugging purposes
    public String toString()    {
        return getSemantic_place() + " " + getNum_people() + " " + getCrowd_density();
    }

    /**
     * @return the semantic_place
     */
    public String getSemantic_place() {
        return semantic_place;
    }

    /**
     * @param semantic_place the semantic_place to set
     */
    public void setSemantic_place(String semantic_place) {
        this.semantic_place = semantic_place;
    }

    /**
     * @return the num_people
     */
    public int getNum_people() {
        return num_people;
    }

    /**
     * @param num_people the num_people to set
     */
    public void setNum_people(int num_people) {
        this.num_people = num_people;
    }

    /**
     * @return the crowd_density
     */
    public int getCrowd_density() {
        return crowd_density;
    }

    /**
     * @param crowd_density the crowd_density to set
     */
    public void setCrowd_density(int crowd_density) {
        this.crowd_density = crowd_density;
    }
}
