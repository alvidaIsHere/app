/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtilityDAO {

    //public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Accepts start date as a parameter and add the required number of minutes
     * to that date and returns the end date
     *
     * @param startDate date which user selected
     * @param format format of the date the user selected
     * @param minutes the number of minutes to add to given startDate
     * @return end date after adding required number of minutes to startDate
     */
    public static String getEndDate(String startDate, String format, int minutes)
            throws ParseException {
        // null check
        if (startDate == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        Date date = sdf1.parse(startDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        Date endDate = cal.getTime();

        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        return sdf2.format(endDate);
    }

    public static String stringDateConversion(String inputFormat,
            String outputFormat, String input) throws IllegalArgumentException,
            ParseException {
        if (inputFormat == null || outputFormat == null || input == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(inputFormat);
        Date date = sdf1.parse(input);
        SimpleDateFormat sdf2 = new SimpleDateFormat(outputFormat);
        return sdf2.format(date);
    }

    public static java.sql.Timestamp stringToSqlDateConversion(
            String inputFormat, String input) throws IllegalArgumentException,
            ParseException {
        if (inputFormat == null || input == null) {
            return null;
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(inputFormat);
        Date date = sdf1.parse(input);
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        return sqlDate;
    }

    public static int getTimeDistanceSecond(String startDate, String endDate) throws ParseException {
        // null check
        
        if (startDate == null || endDate == null) {
            return 0;
        }
        
            String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT);
        Date d1 = sdf1.parse(startDate);
        Date d2 = sdf1.parse(endDate);

        long diff = d2.getTime() - d1.getTime(); //in millisecond
        long diffMins = diff / (60* 1000) % 60; //from millisecond to second
        long diffSeconds = diff / ( 1000) % 60; //from millisecond to second
        int result = (int) Math.abs(diffSeconds) + (int)(Math.abs(diffMins)*60);
        return result;
    }

    
    public static boolean isAfter(String startDate, String endDate) throws ParseException {
        // null check
        if (startDate == null || endDate == null) {
            return false;
        }
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT);
        Date d1 = sdf1.parse(startDate);
        Date d2 = sdf1.parse(endDate);

        return d1.after(d2);
    }
}
