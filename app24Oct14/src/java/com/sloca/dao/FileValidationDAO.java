package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Pattern;


public class FileValidationDAO {

    private static final String locationTableName = "location";
    private static final String locationLookupTableName = "location_lookup";
    private static final String demographicsTableName = "student";

    /**
     *
     * @param fileName
     * @return
     */
    
    // checks if it is a .csv file
    public static boolean checkValidUploadFile(String filename) {
        return ".csv".equals(filename.substring(filename.length() - 4));
    }

    // checks if it is a .zip file
    public static boolean checkValidBootstrap(String filename) {
        return ".zip".equals(filename.substring(filename.length() - 4));
    }



    // For location-lookup, check for valid positive integer value
    // error message expected : invalid location id
    public static boolean validateLocationID(String LocID) {
        try {
            int intValue = Integer.parseInt(LocID);
            return intValue >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // method to validate password for demographics.csv
    // error message expeced : invalid password
    public static boolean validatePassword(String pass) {
        return !pass.contains(" ") && pass.length() >= 8;
    }

    // method to validate email. the email address of the student (2010 onwards,
    // only for
    // undergraduates)
    // error message expected : invalid email
    public static boolean validateEmail(String email) {
        String regex = "(^[A-Za-z]+\\.)([A-Za-z]+\\.)*(20[1-9]\\d)@(sis|law|business|socsc|accountancy|economics){1}\\.smu\\.edu\\.sg";

        return Pattern.matches(regex, email);
    }
    
    public static boolean validateSemanticPlace(String semanticPlace){
        String regex = "SMUSIS(L|B)\\d(.*)";
        
        return Pattern.matches(regex, semanticPlace);
    }

    // method to validate gender
    // error message expected : invalid gender
    public static boolean validateGender(String gender) {
        // trim() used here, but will you trim() before invoking this method?
        // make gender capital before input to database? just to make it
        // constant
        String regex = "([fF]|[mM])";
        return Pattern.matches(regex, gender);
    }

    // method to validate date for location.csv
    // error message expected : invalid timestamp
    public static boolean validateDate(String dateTime) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            format.parse(dateTime);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // method to validate mac address
    // error message expected : invalid mac address
    public static boolean validateMacAddFormat(String macAdd) {
        String regex = "[A-Fa-f0-9]{40}";
        
        return Pattern.matches(regex, macAdd);
    }

    // method to validate mac address?? for demographic.csv (unique mac addresses)
    public static boolean validateMacAddDB(Connection conn, String macAdd) {

        // initialise db conn
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // check database
        String query = "SELECT COUNT(mac_address) AS val FROM " + demographicsTableName + " WHERE mac_address = '" + macAdd + "'";
        try {
            stmt = conn.prepareStatement(query);
            stmt.setString(1, macAdd);
            rs = stmt.executeQuery();

            int rows = 0;
            // check how many rows returned from DB (0 if non-exist, 1 if exists)
            while (rs.next()) {
                rows = rs.getInt("val");
            }

            if (rows < 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(stmt, rs);
        }

        return true;
    }

    // method to validate location ID for location.csv
    // returns true if location id can be found in location-lookup table
    // error message expected : invalid location
    public static boolean validateLocID(HashMap<Integer, String> validLocationID, String locID) {
        return !((validLocationID.get(Integer.parseInt(locID))) == null );
    }

}
