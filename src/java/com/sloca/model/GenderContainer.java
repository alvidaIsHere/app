/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.google.gson.annotations.Expose;
import com.sloca.dao.BreakdownDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author alice
 */
public class GenderContainer {

    @Expose
    private String gender;
    @Expose
    private Long count;
    private transient long percentage;
    private transient ArrayList<Student> student_list;
    private ArrayList<Object> breakdown;

    /**
     * Object represent a gender breakdown for demographic breakdown
     *
     * @param gender the gender in which the user queries, e.g. M or F
     */
    public GenderContainer(String gender) {
        this.gender = gender;
        this.count = 0L;
        this.student_list = new ArrayList<>();
    }

    /**
     * Breakdown process specific for gender container, specific process
     * breakdown method for gender container (with only year and school)
     *
     * @param order by which the user want to sort the breakdown by
     */
    public void processBreakdown(ArrayList<String> order) {
        if (!order.isEmpty()) {
            // get first element
            String first_element = order.get(0);
            if (first_element.equals("year")) {
                // if it's breakdown by year, call method from DAO to process year breakdown
                setBreakdown(BreakdownDAO.breakdownByYear(order, student_list));
            }
            if (first_element.equals("school")) {
                // invoke breakdown by school method
                setBreakdown(BreakdownDAO.breakdownBySchool(order, student_list));
            }
        }
    }

    /**
     * Add a new Student object and add to the total count
     *
     * @param s is add a new Student to the application
     */
    public void add(Student s) {
        setCount((Long) (getCount() + 1));
        student_list.add(s);
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return a String to describe the students gender and the total number of
     * students
     */
    public String toString() {
        return "<p>" + gender + " : " + getCount() + " (" + this.count + "%)" + "</p>";
    }

    /**
     * @return the breakdown
     */
    public ArrayList<Object> getBreakdown() {
        return breakdown;
    }

    /**
     * @param breakdown the breakdown to set
     */
    public void setBreakdown(ArrayList<Object> breakdown) {
        this.breakdown = breakdown;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * Calculate the percentage of students after each 'update'
     *
     * @param d represents the denominator of the total number of users
     */
    public void calcPercentage(int d) {
        // takes in the total number of users as param (denominator)
        // calculates percentage
        BigDecimal num;
        if (count != 0) {
            num = new BigDecimal((double) count * 100 / d);
            BigDecimal percen = num.setScale(0, RoundingMode.HALF_UP);
            percentage = percen.intValue();
        } else {
            num = new BigDecimal(count);
            BigDecimal percen = num.setScale(0, RoundingMode.HALF_UP);
            percentage = percen.intValue();
        }
    }

    /**
     * Get the percentage of students after each update
     *
     * @return a type long of the percentage of students after each update
     */
    public long getPercentage() {
        return this.percentage;
    }
}
