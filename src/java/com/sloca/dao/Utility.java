/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    /**
     * Accepts start date as a parameter and add the required number of minutes
     * to that date and returns the end date
     *
     * @param startDate date which user selected
     * @param format format of the date the user selected
     * @param minutes the number of minutes to add to given startDate
     * @return end date after adding required number of minutes to startDate
     * @throws java.text.ParseException if the start date is not in the given
     * date format
     */
    public static String getEndDate(String startDate, String format, int minutes)
            throws ParseException {
        // null check
        if (startDate == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        sdf1.setLenient(false);
        Date date = sdf1.parse(startDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        Date endDate = cal.getTime();

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf2.format(endDate);
    }

    /**
     * To check if mac address can be found in student table
     *
     * @param macAddr the mac address of the user
     * @return true if mac address can be found in database
     */
    public static boolean isValidMacAddress(String macAddr) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT mac_address FROM student "
                + "WHERE mac_address = '" + macAddr + "'";
        String returnMacAddr = "";

        try {

            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                returnMacAddr = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
            return !returnMacAddr.isEmpty();
        }
    }

    /**
     * To check if mac address can be found in location table
     *
     * @param macAddr the mac address of the user
     * @return true if it is found in the database
     */
    public static boolean macAddrInLocation(String macAddr) {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT student_mac_address FROM location "
                + "WHERE student_mac_address = '" + macAddr + "'";

        String returnMacAddr = "";

        try {

            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                returnMacAddr = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
            return !returnMacAddr.isEmpty();
        }

    }

    /**
     * To check if username can be found in database
     *
     * @param username the username of the user
     * @return true if username can be found in database
     */
    public static boolean compareUsernameWithDatabase(String username) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT email FROM student "
                + "WHERE email LIKE '" + username + "@%'";

        try {

            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
        }
        return false;
    }
}
