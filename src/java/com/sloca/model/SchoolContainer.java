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
import java.util.Comparator;

/**
 *
 * @author alice
 */
public class SchoolContainer implements Comparator<SchoolContainer>, Comparable<SchoolContainer> {

    @Expose
    private String school;
    @Expose
    private long count;
    private transient int percentage;
    private transient ArrayList<Student> student_list;
    private ArrayList<Object> breakdown;

    /**
     * Object represent a school breakdown for demographic breakdown
     *
     * @param school the school in which this list of students reside in
     */
    public SchoolContainer(String school) {
        this.school = school;
        this.count = 0L;
        student_list = new ArrayList<>();
    }

    /**
     * Breakdown process specific for school container
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
            if (first_element.equals("gender")) {
                // invoke breakdown by gender method
                setBreakdown(BreakdownDAO.breakdownByGender(order, student_list));
            }
        }
    }

    /**
     * Add a new Student object
     *
     * @param s is add a new Student to the application
     */
    public void add(Student s) {
        setCount((Long) (getCount() + 1));
        student_list.add(s);
    }

    /**
     * @return the school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school the school to set
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Create a String to describe the students school and the total number of
     * students
     *
     * @return a String to describe the students school and the total number of
     * students
     */
    public String toString() {
        return "<p>" + school + getCount() + " (" + this.count + "%)" + "</p>";
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
     * Compare the 2 SchoolContainer by inputs
     *
     * @param o1 represents a SchoolContainer object to compare from
     * @param o2 represents a SchoolContainer object to compare with
     * @return 1 if match, -1 if not match and 0 if equal
     */
    @Override
    public int compare(SchoolContainer o1, SchoolContainer o2) {
        int result = String.CASE_INSENSITIVE_ORDER.compare(o1.getSchool(), o2.getSchool());
        if (result == 0) {
            result = o1.compareTo(o2);
        }
        return result;
    }

    /**
     * Compare the 2 SchoolContainer
     *
     * @param o represents a SchoolContainer object to compare with
     * @return 1 if match, -1 if not match and 0 if equal
     */
    @Override
    public int compareTo(SchoolContainer o) {
        return o.getSchool().compareTo(this.getSchool());
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
    public int getPercentage() {
        return this.percentage;
    }
}
