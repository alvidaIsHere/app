/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.sloca.dao.DateUtilityDAO;
import java.text.ParseException;

public class UserVisitRecord {

    String semanticPlace;
    String startTime;
    String endTime;

    /**
     * Constructor of the class
     *
     * @param semanticPlace the place where user is found
     * @param startTime user is found at the semanticPlace from this time
     * @param endTime user is found at the semanticPlace until this time
     */
    public UserVisitRecord(String semanticPlace, String startTime, String endTime) {
        this.semanticPlace = semanticPlace;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     *
     * @return semantic place
     */
    public String getSemanticPlace() {
        return semanticPlace;
    }

    /**
     * to get the time spent in this particular location
     *
     * @return time spent in seconds
     * @throws ParseException if the input is not in the correct format
     */
    public int getTimeSpentInSeconds() throws ParseException {
        return DateUtilityDAO.getTimeDistanceSecond(startTime, endTime);
    }

    /**
     * to update the end time a user located at this place
     *
     * @param endTime new end time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     *
     * @return end time
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     *
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * override toString method
     *
     * @return the string format for this user record
     */
    @Override
    public String toString() {
        return "UserVisitRecord{" + "semanticPlace=" + semanticPlace + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }

}
