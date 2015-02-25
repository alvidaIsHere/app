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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elvin
 */
public class LocationDAO {

    private static final String TBLNAME = "location_lookup";
    private static final String LOCATION_ID_COLUMN = "location_id";
    private static final String SEMANTIC_NAME_COLUMN = "semantic_name";

    public static ArrayList<String> getAllLocation() {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<String> resultList = new ArrayList<String>() {
        };

        String query = "SELECT * FROM " + TBLNAME;

        try {

            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            try {
                while (rs.next()) {
                    String semanticName = rs.getString(SEMANTIC_NAME_COLUMN);
                    if (!resultList.contains(semanticName)) {
                        resultList.add(semanticName);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionFactory.close(rs);
            }

        } catch (SQLException e) {
            //do something
        } finally {
            ConnectionFactory.close(connection, stmt);
        }

        return resultList;
    }

    public static ArrayList<Integer> getLocationID(String semanticName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Integer> resultList = new ArrayList<Integer>() {
        };

        String query = "SELECT " + LOCATION_ID_COLUMN + " FROM " + TBLNAME + " WHERE " + SEMANTIC_NAME_COLUMN + "=" + semanticName;

        try {

            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            try {
                while (rs.next()) {
                    int locationId = rs.getInt(LOCATION_ID_COLUMN);
                    resultList.add(locationId);

                }
            } catch (SQLException ex) {
                Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionFactory.close(rs);
            }

        } catch (SQLException e) {
            //do something
        } finally {
            ConnectionFactory.close(connection, stmt);
        }

        return resultList;
    }
}
