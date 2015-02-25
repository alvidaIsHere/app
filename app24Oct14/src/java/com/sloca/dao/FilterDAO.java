/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.SchoolFilter;
import com.sloca.model.YearFilter;
import com.sloca.model.YearGenderFilter;
import com.sloca.model.YearSchoolFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author alice
 */
public class FilterDAO {

    // method to retrieve amount of students for a particular year in a given timestamp (- 15 min)

    public static int retrieveCountByYear(String timestamp, String timestamp2, String year) {
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(DISTINCT email) AS val FROM student s RIGHT OUTER JOIN location l ON s.mac_address = l.student_mac_address WHERE l.time < ? AND l.time > ? AND s.email LIKE ?";
        // '%.2010%' , 1: '2014-03-15 22:43:25', 2: '2014-03-15 15:20:25'
        /* static date input
        String timestampt = "2014-03-15 22:43:25";
        String timestamp2t = "2014-03-12 22:13:25";
        */
        int rows = 0;
        try {            
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, timestamp);
            stmt.setString(2, timestamp2);
            stmt.setString(3, "%." + year + "@%");
            rs = stmt.executeQuery();            
            // check how many rows returned from DB (0 if non-exist, 1 if exists)
            while (rs.next()) {
                rows = rs.getInt("val");
            }            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);
            return rows;
        }
    }
    
    // method to rerieve gender
    public static int retrieveCountByGender(String timestamp, String timestamp2, String gender) {
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(DISTINCT email) AS val FROM student s RIGHT OUTER JOIN location l ON s.mac_address = l.student_mac_address WHERE l.time < ? AND l.time > ? AND gender LIKE ?";
        // '%.2010%' , 1: '2014-03-15 22:43:25', 2: '2014-03-15 15:20:25'
        /* removal of static date
        String timestampt = "2014-03-15 22:43:25";
        String timestamp2t = "2014-03-12 22:13:25";
        */
        int rows = 0;
        try {            
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, timestamp);
            stmt.setString(2, timestamp2);
            stmt.setString(3, gender);
            rs = stmt.executeQuery();            
            // check how many rows returned from DB (0 if non-exist, 1 if exists)
            while (rs.next()) {
                rows = rs.getInt("val");
            }            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);
            return rows;
        }
    }
    
    public static int retrieveCountByYearAndGender(String timestamp, String timestamp2, String year, String gender) {
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(DISTINCT name) AS val FROM student s RIGHT OUTER JOIN location l ON s.mac_address = l.student_mac_address WHERE l.time < ? AND l.time > ? AND s.email LIKE ? AND s.gender LIKE ?";
        // '%.2010%' , 1: '2014-03-15 22:43:25', 2: '2014-03-15 15:20:25'
        /* removal of static date
        String timestampt = "2014-03-15 22:43:25";
        String timestamp2t = "2014-03-12 22:13:25";    
        */
        int rows = 0;
        try {            
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, timestamp);
            stmt.setString(2, timestamp2);
            stmt.setString(3, "%." + year + "@%");
            stmt.setString(4, gender);
            rs = stmt.executeQuery();            
            // check how many rows returned from DB (0 if non-exist, 1 if exists)
            while (rs.next()) {
                rows = rs.getInt("val");
            }            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);
            return rows;
        }
    }
    
    public static int retrieveCountByYearGenderAndSchool(String timestamp, String timestamp2, String year, String gender, String school) {
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(DISTINCT name) AS val FROM student s RIGHT OUTER JOIN location l ON s.mac_address = l.student_mac_address WHERE l.time < ? AND l.time > ? AND s.email LIKE ? AND s.gender LIKE ? AND s.email LIKE ?";
        // '%.2010%' , 1: '2014-03-15 22:43:25', 2: '2014-03-15 15:20:25'
        /* removal of static date
        String timestampt = "2014-03-15 22:43:25";
        String timestamp2t = "2014-03-12 22:13:25";  
        */
        //String gendert = gender;
        String sch = school;
        int rows = 0;
        try {            
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, timestamp);
            stmt.setString(2, timestamp2);
            stmt.setString(3, "%." + year + "@%");
            stmt.setString(4, gender);
            stmt.setString(5, "%@" + sch + ".%");
            rs = stmt.executeQuery();            
            // check how many rows returned from DB (0 if non-exist, 1 if exists)
            while (rs.next()) {
                rows = rs.getInt("val");
            }            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);            
        }
        return rows;
    }
    
    public static ArrayList<YearFilter> filterYear(String startDate, String endDate) {
        ArrayList<YearFilter> yearCount = new ArrayList<YearFilter>();
        for(int i = 0; i < 5; i++)   {
            yearCount.add(new YearFilter(2010 + i, retrieveCountByYear(startDate, endDate, "201" + i)));
        }
        return yearCount;
    }
    
    public static ArrayList<YearFilter> filterYearGender(String startDate, String endDate) {
        ArrayList<YearFilter> yearCount = new ArrayList<YearFilter>();
        for(int i = 0; i < 5; i++)   {
            // get gender for respective year
            int males =  retrieveCountByYearAndGender(startDate, endDate, "201" + i, "m");
            YearGenderFilter maleCount = new YearGenderFilter("M", males);
            int females =  retrieveCountByYearAndGender(startDate, endDate, "201" + i, "f");
            //System.out.println("female count: "+ females);
            YearGenderFilter femaleCount = new YearGenderFilter("F", females);
            // store in arraylist
            ArrayList<YearGenderFilter> breakdown = new ArrayList<YearGenderFilter>();
            breakdown.add(maleCount);
            breakdown.add(femaleCount);
            yearCount.add(new YearFilter(2010 + i, retrieveCountByYear(startDate, endDate, "201" + i), breakdown));
        }
        return yearCount;
    }
    
    public static ArrayList<YearGenderFilter> filterYearGenderSchool(String startDate, String endDate)   {
        // obtain gender first
        int males = retrieveCountByGender(startDate, endDate, "m");
        int females = retrieveCountByGender(startDate, endDate, "f");
        // string array to store schools
        String[] schools = {"business", "accountancy", "sis", "economics", "law", "socsc"};        
        // create year gender filter object
        YearGenderFilter malesTotal = new YearGenderFilter("m", males);
        YearGenderFilter femalesTotal = new YearGenderFilter("f", females);
        // okay, create arraylist to store females and males total objects and send it awayyyy
        ArrayList<YearGenderFilter> result = new ArrayList<>();
        // prepare breakdown of years --> schools
        // create a list of yearschoolfilters
        ArrayList<YearSchoolFilter> schoolBreakdownForYears = new ArrayList<YearSchoolFilter>();
        // loop through years
        for(int i = 0; i < 5; i++)   {
            // obtain total number of students for that year
            int yearTotalMales = retrieveCountByYearAndGender(startDate, endDate, "201" + i, "m");
            //System.out.println("year total males --> " + yearTotalMales);
            // retrieve number of students in different schools (6) for the year
            // create arraylist to store school objects
            ArrayList<SchoolFilter> schoolBreakdown = new ArrayList<SchoolFilter>();
            for(int j = 0; j < schools.length; j++)   {                
                // retrieve male students for particular school first
                int temp = retrieveCountByYearGenderAndSchool(startDate, endDate, "201" + i, "m", schools[j]);
                // add first school to list of schools
                System.out.println("count for male : " + j + " " + temp);
                schoolBreakdown.add(new SchoolFilter(schools[j], temp));
            }
            // update year school filter for this year
            YearSchoolFilter temp2 = new YearSchoolFilter("201" + i, yearTotalMales, schoolBreakdown);
            // add to list of school breakdown for years
            System.out.println(temp2);
            schoolBreakdownForYears.add(temp2);
        }
        // add to year school filter object (set breakdown)
        malesTotal.setBreakdown(schoolBreakdownForYears);
        result.add(malesTotal);
        // ----------- males done. now to work on the females (sry for repetition, i'm lazy. :|)
        // clear breakdown array for females
        //schoolBreakdownForYears.clear();
        //System.out.println("Arr size " + schoolBreakdownForYears.size());
        // create a list of yearschoolfilters
        ArrayList<YearSchoolFilter> schoolBreakdownForYears2 = new ArrayList<YearSchoolFilter>();
        // loop through years
        for(int i = 0; i < 5; i++)   {
            // obtain total number of students for that year
            int yearTotalFemales = retrieveCountByYearAndGender(startDate, endDate, "201" + i, "f");
            //System.out.println("year total females --> " + yearTotalFemales);
            // retrieve number of students in different schools (6) for the year
            // create arraylist to store school objects
            ArrayList<SchoolFilter> schoolBreakdown2 = new ArrayList<SchoolFilter>();
            for(int j = 0; j < schools.length; j++)   {                
                // retrieve female students for particular school first
                int temp = retrieveCountByYearGenderAndSchool(startDate, endDate, "201" + i, "f", schools[j]);
                System.out.println("count for female : " + j + " " + temp);
                // add first school to list of schools
                schoolBreakdown2.add(new SchoolFilter(schools[j], temp));
            }
            // update year school filter for this year
            YearSchoolFilter temp2 = new YearSchoolFilter("201" + i, yearTotalFemales, schoolBreakdown2);
            System.out.println(temp2);
            // add to list of school breakdown for years
            schoolBreakdownForYears2.add(temp2);
        }
        // add to year school filter object (set breakdown)
        femalesTotal.setBreakdown(schoolBreakdownForYears2);
        result.add(femalesTotal);
        
        return result;
    }
}
