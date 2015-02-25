/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.HeatMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Jimmy
 */
public class HeatMapDAO {

    /**
     * retrieves the user location information within the 15 minutes windows at
     * a particular floor
     *
     * @param location the target floor queried (B1, L1 - L5)
     * @param userQueryDate the start datetime of query window (inclusive)
     * @param endDate the end date time of query window (exclusive)
     * @return the user location in Heatmap object
     * @see HeatMap
     */
    public static ArrayList<HeatMap> retrieveHeatMapForLocation(String location, String userQueryDate, String endDate) {
        // db connection parameters
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT DISTINCT location_lookup.semantic_name AS place, count(student_mac_address) AS students FROM location_lookup LEFT OUTER JOIN (SELECT max(time), student_mac_address, location_id, count(*) as REPEATED FROM location WHERE"
                + " time > ? AND time <= ? GROUP BY student_mac_address) AS locationTime ON locationTime.location_id = location_lookup.location_id "
                + "WHERE location_lookup.semantic_name LIKE ? GROUP BY location_lookup.semantic_name ORDER BY 1";

        // array list to store results later
        ArrayList<HeatMap> results = new ArrayList<>();

        // execute
        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            // set params
            stmt.setString(1, endDate);
            stmt.setString(2, userQueryDate);

            if (location.equals("0")) {
                stmt.setString(3, "%B1%");
            } else {
                stmt.setString(3, "%L" + location + "%");
            }

            rs = stmt.executeQuery();

            // obtain results
            while (rs.next()) {
                String place = rs.getString("place");
                int students = rs.getInt("students");
                // get density threshold first
                int density = getDensityThreshold(students);
                // create new Heat Map object
                HeatMap map = new HeatMap(place, students, density);
                // add to list of results
                results.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);
        }
        return results;
    }

    /**
     * To get value of crowd density based on number of people
     *
     * @param num_people number of people in that location
     * @return an integer value that represents the number of people in that
     * location (i.e.: 0 for 0 person, 1 for 1 or 2 people, 2 for 3 to 5 people,
     * 3 for 6 to 10 people, 4 for 11 to 20 people, 5 for 21 to 20 people, and 6
     * for more than 20 people)
     */
    private static int getDensityThreshold(int num_people) {
        if (num_people == 0) {
            return 0;
        } else if (num_people == 1 || num_people == 2) {
            return 1;
        } else if (num_people >= 3 && num_people <= 5) {
            return 2;
        } else if (num_people >= 6 && num_people <= 10) {
            return 3;
        } else if (num_people >= 11 && num_people <= 20) {
            return 4;
        } else if (num_people >= 21 && num_people <= 30) {
            return 5;
        } else {
            return 6;
        }
    }
}
