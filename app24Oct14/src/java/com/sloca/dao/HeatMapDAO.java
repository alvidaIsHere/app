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
 * @author alice
 */
public class HeatMapDAO {

    // method to obtain data from database (location & count)
    public static ArrayList<HeatMap> retrieveHeatMapForLocation(String location, String startDate, String endDate) {
        // db connection parameters
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT location_lookup.semantic_name AS place, count(*) AS students FROM \n"
                + "(SELECT max(time), student_mac_address, location_id, count(*) as REPEATED\n"
                + "FROM location\n"
                + "WHERE time > ? AND time <= ? \n"
                + "GROUP BY student_mac_address) AS latest_Time \n"
                + "INNER JOIN location_lookup ON latest_Time.location_id = location_lookup.location_id\n"
                + "WHERE location_lookup.semantic_name LIKE ? GROUP BY location_lookup.semantic_name\n"
                + "ORDER BY 1;";

        // array list to store results later
        ArrayList<HeatMap> results = new ArrayList<>();

        // execute
        try {
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);
            // set params
            stmt.setString(1, endDate);
            stmt.setString(2, startDate);
            
            if(location.equals("0")){
                stmt.setString(3, "%B1%");
            }else{
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
            return results;
        }
    }

    // method to derive crowd density
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
