/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import java.util.ArrayList;
import com.sloca.model.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jeremy.kuah.2013
 */
public class AutomaticGroupIdentificationDAO {

    // Identify all location_id within time frame
    // Identify all users within time frame
    // See how much time people spending together (try to see if can use codes from View Top K Companion)
    public static ArrayList<Group> identifyGroup(String startDateTime) {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String endDateTime = "";

        try {
            // -4320 seconds (15 minutes)
            endDateTime = DateUtilityDAO.getEndDate(startDateTime, "yyyy-MM-dd HH:mm:ss", -4320);
            System.out.println(endDateTime);
        } catch (ParseException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query
                = "SELECT COUNT(DISTINCT student_mac_address) AS total_user, MAX(temp.groups) AS total_groups "
                + "FROM location, "
                + "     (SELECT COUNT(location_id) AS groups "
                + "     FROM location "
                + "     WHERE time > '2014-03-23 13:40:00' AND time < '2014-03-23 13:55:00' "
                + "     GROUP BY student_mac_address) AS temp "
                + "WHERE time > '2014-03-23 13:40:00' AND time < '2014-03-23 13:55:00'";

        return new ArrayList<Group>();
    }

}
