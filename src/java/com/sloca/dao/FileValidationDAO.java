package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FileValidationDAO {

    private static final String demographicsTableName = "student";

    /**
     * Check if the file to be uploaded is a csv file type
     *
     * @param filename the file to be uploaded
     * @return true is the file is a csv file, else return false
     */
    public static boolean checkValidUploadFile(String filename) {
        return ".csv".equals(filename.substring(filename.length() - 4));
    }

    /**
     * Check if the file to be bootstrapped is a zip file
     *
     * @param filename the file to be bootstrap
     * @return true if file is a zip file, else return false
     */
    public static boolean checkValidBootstrap(String filename) {
        return ".zip".equals(filename.substring(filename.length() - 4));
    }

    /**
     * Check if the location ID within the file is integer
     *
     * @param LocID the location id within the file
     * @return true if the location ID is valid, else return false
     */
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

    /**
     * Check if the password is valid. A valid password does not contain space
     * and at least 8 characters long
     *
     * @param pass the password within the file belonging to each user
     * @return true is the password is in the correct format, else return false
     */
    // method to validate password for demographics.csv
    // error message expeced : invalid password
    public static boolean validatePassword(String pass) {
        return !pass.contains(" ") && pass.length() >= 8;
    }

    /**
     * Check that the email is in the correct format and belongs to current
     * undergraduates (intake 2010 - 2014). Only undergraduates under current
     * schools (i.e.: sis, law, business, socsc, accountancy, or economics)
     *
     * @param email the email within the file belonging to each user
     * @return true if the email is in the correct format and belongs to current
     * undergraduates, else return false
     */
    public static boolean validateEmail(String email) {
        // method to validate email. the email address of the student (2010 onwards,
        // only for undergraduates)
        // error message expected : invalid email
        String regex = "(^[A-Za-z0-9]+\\.)([A-Za-z0-9]+\\.)*(201[0-4])@(sis|law|business|socsc|accountancy|economics){1}\\.smu\\.edu\\.sg";

        return Pattern.matches(regex, email);
    }

    /**
     * Check that id semantic place name is within SIS building
     *
     * @param semanticPlace the name of the location
     * @return true if semantic place is valid, else return false
     */
    public static boolean validateSemanticPlace(String semanticPlace) {
        String regex = "SMUSIS(L|B)\\d(.*)";

        return Pattern.matches(regex, semanticPlace);
    }

    /**
     * Check that each user is of valid gender
     *
     * @param gender the gender of the users
     * @return true if gender is M,m,F,f, else return false
     */
    // method to validate gender
    // error message expected : invalid gender
    public static boolean validateGender(String gender) {
        // trim() used here, but will you trim() before invoking this method?
        // make gender capital before input to database? just to make it
        // constant
        String regex = "([fF]|[mM])";
        return Pattern.matches(regex, gender);
    }

    /**
     * Check that the timestamp provided is of correct format, "yyyy-MM-dd
     * HH:mm:ss"
     *
     * @param dateTime the timestamp within in the file
     * @return true if the time stamp is in correct format, else return false
     */
    // method to validate date for location.csv
    // error message expected : invalid timestamp
    public static boolean validateDate(String dateTime) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            format.setLenient(false);
            format.parse(dateTime);
            return (dateTime.length() == 19);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check that the mac address is in the correct format (i.e.: 40 characters
     * hexadecimal)
     *
     * @param macAdd the mac address for each user
     * @return true is the mac address format is correct, else return false
     */
    // method to validate mac address
    // error message expected : invalid mac address
    public static boolean validateMacAddFormat(String macAdd) {
        String regex = "[A-Fa-f0-9]{40}";
        return Pattern.matches(regex, macAdd);
    }

    /**
     * Check that the mac address is not a duplicate from student table
     *
     * @param conn the connection needed to the database
     * @param macAdd the mac address to be validated against the database
     * @return true if the there is no duplicate in mac address in database,
     * else return false
     */
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

    /**
     * Check that the location ID in location csv can be found in
     * location-lookup
     *
     * @param validLocationID the location IDs within location-lookup
     * @param locID the location ID to be validated
     * @return true is thhe location ID is valid, else return false
     */
    // method to validate location ID for location.csv
    // returns true if location id can be found in location-lookup table
    // error message expected : invalid location
    public static boolean validateLocID(HashMap<Integer, String> validLocationID, String locID) {
        try {
            Integer.parseInt(locID);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return !((validLocationID.get(Integer.parseInt(locID))) == null);
    }

}
