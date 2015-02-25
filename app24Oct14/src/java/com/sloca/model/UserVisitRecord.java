/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.sloca.dao.DateUtilityDAO;
import java.text.ParseException;

/**
 *
 * @author Elvin
 */
public class UserVisitRecord {

    String semanticPlace;
    String startTime;
    String endTime;

    public UserVisitRecord(String semanticPlace, String startTime, String endTime) {
        this.semanticPlace = semanticPlace;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSemanticPlace() {
        return semanticPlace;
    }

    public int getTimeSpentInSeconds() throws ParseException {
        return DateUtilityDAO.getTimeDistanceSecond(startTime, endTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "UserVisitRecord{" + "semanticPlace=" + semanticPlace + ", startTime=" + startTime + ", endTime=" + endTime + '}';
    }

}
