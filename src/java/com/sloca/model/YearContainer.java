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

public class YearContainer {

    @Expose
    private Long year;
    @Expose
    private Long count;
    private transient long percentage;
    private transient ArrayList<Student> list;
    private ArrayList<Object> breakdown;

    /**
     * Constructor for YearContainer
     *
     * @param year from 2010 to 2014
     */
    public YearContainer(String year) {
        this.year = Long.parseLong(year);
        this.count = 0L;
        list = new ArrayList<>();
    }

    /**
     * Breakdown process specific for year container
     *
     * @param order by which the user want to sort the breakdown by
     */
    public void processBreakdown(ArrayList<String> order) {
        if (!order.isEmpty()) {
            String first_element = order.get(0);
            if (first_element.equals("gender")) {
                setBreakdown(BreakdownDAO.breakdownByGender(order, list));
            }
            if (first_element.equals("school")) {
                setBreakdown(BreakdownDAO.breakdownBySchool(order, list));
            }
        }
    }

    /**
     * Add a new Student object
     *
     * @param s is add a new Student to the application
     */
    public void add(Student student) {
        setCount((Long) (getCount() + 1));
        list.add(student);
    }

    /**
     * @return the year
     */
    public String getYear() {
        return "" + year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = Long.parseLong(year);
    }

    /**
     * Create a String to describe the students year and the total number of
     * students
     *
     * @return a String to describe the students year and the total number of
     * students
     */
    public String toString() {
        return "<p>" + year + " : " + getCount() + " (" + this.count + "%)" + "</p>";
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
        //int temp_percentage = Math.round((float)count * 100 / d);
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
