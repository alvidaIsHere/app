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

    /**
     * Returns the result date after adding or subtracting the times in minute
     *
     *
     * @param startDate date which user selected
     * @param format format of the date the user selected
     * @param minutes the number of minutes to add to given startDate
     * @return end date after adding required number of minutes to startDate
     * @throws java.text.ParseException if the start date is not in the given
     * date format
     */
    public static String getEndDate(String startDate, String format, double minutes)
            throws ParseException {
        // null check
        if (startDate == null) {
            return "";
        }
//        "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        sdf1.setLenient(false);
        Date date = sdf1.parse(startDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        double second = minutes * 60;
        cal.add(Calendar.SECOND, (int) second);
        Date endDate = cal.getTime();

        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        return sdf2.format(endDate);
    }

    /**
     * Returns the formatted date in String based on the given format
     *
     * @param inputFormat date format of input string
     * @param outputFormat date format of output string intended
     * @param input date to be formatted
     * @return string of the input date with intended format
     * @throws IllegalArgumentException if the input string is not date
     * @throws ParseException if input string is not in the given input format
     */
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

    /**
     * Returns the formatted date to SQL timestamp based on the given format
     *
     * @param inputFormat date format of input string
     * @param input Date to b formatted
     * @return date object in sql timestamp format of the input date with
     * intended format
     * @throws IllegalArgumentException if the input string is not date
     * @throws ParseException if input string is not in the given input format
     */
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

    /**
     * Returns the times difference in second for two different dates. Order of
     * input does not matter. i.e. getTimeDistanceSecond(date1, date2) returns
     * the same output as getTimeDistanceSecond(date2, date1)
     *
     * @param startDate first date
     * @param endDate second date
     * @return the distance between two date in second
     * @throws ParseException if the given date is not in the date format
     * "yyyy-MM-dd HH:mm:ss"
     */
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
        long diffMins = diff / (60 * 1000) % 60; //from millisecond to second
        long diffSeconds = diff / (1000) % 60; //from millisecond to second
        int result = (int) Math.abs(diffSeconds) + (int) (Math.abs(diffMins) * 60);
        return result;
    }

    /**
     * To check if a date is before or after another date.
     *
     * @param startDate
     * @param endDate
     * @return true if endDate is after startDate
     * @throws ParseException if the given date is not in the date format
     * "yyyy-MM-dd HH:mm:ss"
     */
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
