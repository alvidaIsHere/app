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

/**
 *
 * @author alice
 */
public class Utility {
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

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf2.format(endDate);
    }
}
