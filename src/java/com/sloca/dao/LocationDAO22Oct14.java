/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.TopKNextPlaceResult;
import com.sloca.model.UserVisitRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elvin
 */
public class LocationDAO22Oct14 {

    private static final String TBLNAME = "location_lookup";
    private static final String LOCATION_ID_COLUMN = "location_id";
    private static final String SEMANTIC_NAME_COLUMN = "semantic_name";

    public static HashMap<Integer, ArrayList<TopKNextPlaceResult>> getTopKNextPlaces(String semanticPlace, String atTime) throws ParseException, SQLException {
        //prepare connection
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //Discover all users located at that place during the specified date / time using the previous 15 minute time window.
        String before15Min = Utility.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", -15);
        String after15Min = Utility.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 15);
        /*
         System.out.println(before15Min);
         System.out.println(after15Min);
         */
        String selectStudents
                = "SELECT DISTINCT student_mac_address "
                + "FROM location JOIN location_lookup ON location.location_id=location_lookup.location_id "
                + "AND semantic_name = '" + semanticPlace + "'"
                + "AND time >'" + before15Min + "'"
                + "AND time <='" + atTime + "'";

        ArrayList<String> macAddresses = new ArrayList<>();

        //retrieve all users located at that place from database
        stmt = connection.prepareStatement(selectStudents);
        rs = stmt.executeQuery();

        while (rs.next()) {
            String macAddr = rs.getString("student_mac_address");
            macAddresses.add(macAddr);
        }
        Iterator iter = macAddresses.iterator();
        while (iter.hasNext()) {
            String macAddr = (String) iter.next();
            String selectLastVisitPlace
                    = "SELECT "
                    + "MAX(time) AS time, semantic_name "
                    + "FROM location JOIN location_lookup "
                    + "ON location.location_id = location_lookup.location_id "
                    + "WHERE student_mac_address='" + macAddr + "' "
                    + "AND time >'" + before15Min + "' "
                    + "AND time <='" + atTime + "' "
                    + "GROUP BY semantic_name";

            stmt = connection.prepareStatement(selectLastVisitPlace);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String lastLocation = rs.getString("semantic_name");
                //remove this user if the last location is not the specified place
                if (!lastLocation.equalsIgnoreCase(semanticPlace)) {
                    iter.remove();
                    break;
                }
            }
        }

        //count how many of distinct users last located in the semanticPlace during the 15 mins query windows
        int numberOfUserFound = macAddresses.size();

        //3.Track each of these discovered users (i.e., located at the specified place)
        //for the next  15  minutes to see if they visit another semantic place
        //If a user visited multiple places within the next 15 minutes, consider the *last* one as the next visited place.
        //elvin: assume next visit place cannot be the same as the original place
        HashMap<String, String> nextPlaces = new HashMap<String, String>(); //HashMap<userMac, lastVisitSemanticLocation>

        for (String macAddr : macAddresses) {
            String selectNextVisitPlace
                    = "SELECT * FROM location JOIN location_lookup on location.location_id = location_lookup.location_id "
                    + "AND student_mac_address='" + macAddr + "' "
                    + "AND time >'" + atTime + "' "
                    + "AND time <='" + after15Min + "' "
                    //+ "AND location_lookup.semantic_name !='" + semanticPlace + "' "
                    + "ORDER BY time";
            stmt = connection.prepareStatement(selectNextVisitPlace);
            rs = stmt.executeQuery();

            //HashMap<userMac, semanticPlace>
            ArrayList<UserVisitRecord> userVisits = new ArrayList<UserVisitRecord>();
            String initialTime = atTime;
            String initialNextPlace = "";
            String lastVisitTime = "";
//            System.out.println(macAddr);
            int countRow = 0;
            while (rs.next()) {
                countRow++;
                String semanticPlaceUserVisit = rs.getString("semantic_name");
                String currentTime = rs.getString("time");
//                System.out.println(semanticPlaceUserVisit);
//                System.out.println(currentTime);
                //4. calculate the time spent 
                //To "visit a place", a user has to spend at least 5 minutes at that place.
                //do new class (UserVisitRecord): semanticPlace, timeSpent, startTime
                if (countRow > 1) { //skip first record as there is no time 

                    if (userVisits.isEmpty()) {
                        UserVisitRecord newVisit = new UserVisitRecord(initialNextPlace, atTime, currentTime);
//                        System.out.println("empty" + newVisit);
                        userVisits.add(newVisit);
                        lastVisitTime = currentTime;
                    } else {
                        if (initialNextPlace.equalsIgnoreCase(semanticPlaceUserVisit)) {//continuation of the visiting place
                            for (UserVisitRecord uvr : userVisits) {
                                if (uvr.getSemanticPlace().equals(semanticPlaceUserVisit)) {
                                    uvr.setEndTime(currentTime); //prolong the visit time
//                                    System.out.println("setendtime" + uvr);
                                    lastVisitTime = currentTime;

                                }
                            }
                        } else { //this record is not continuation of previous time frame but is still same semantic place
                            UserVisitRecord newVisit = new UserVisitRecord(initialNextPlace, lastVisitTime, currentTime);
                            userVisits.add(newVisit);
                            lastVisitTime = currentTime;
//                            System.out.println("newRecord" + newVisit);

                        }
                    }
                }
                //reset the place and time fro next record
                initialTime = currentTime;
                initialNextPlace = semanticPlaceUserVisit;
            }

            //add time for last record until the end of 15 mins window
            if (countRow == 1) {//there are record retrieved but skipped
                UserVisitRecord newVisit = new UserVisitRecord(initialNextPlace, lastVisitTime, after15Min);
                userVisits.add(newVisit);
//                System.out.println("only once record" + newVisit);

            } else if (countRow > 1) {//more that one record retrieve
                UserVisitRecord uvr = userVisits.get(userVisits.size() - 1);//get the last record
                String semanticPlaceUserVisit = uvr.getSemanticPlace();

                if (initialNextPlace.equalsIgnoreCase(semanticPlaceUserVisit)) {//continuation of the visiting place
                    uvr.setEndTime(after15Min); //prolong the visit time
//                    System.out.println("last record continue" + uvr);

                } else { //this record is not continuation of previous time frame but is still same semantic place
                    UserVisitRecord newVisit = new UserVisitRecord(initialNextPlace, lastVisitTime, after15Min);
                    userVisits.add(newVisit);
//                    System.out.println("last newRecord" + newVisit);

                }

            }

            //eliminate those that does not reach 5 min, get the last semantic place visited
            lastVisitTime = atTime;
            String lastVisitPlace = "";
            for (UserVisitRecord uvr : userVisits) {
                int seconds = uvr.getTimeSpentInSeconds();
                String fromTime = uvr.getStartTime();
                if (seconds >= 5 * 60 && DateUtilityDAO.isAfter(fromTime, lastVisitTime)) {
                    lastVisitPlace = uvr.getSemanticPlace();
                    lastVisitTime = fromTime;
                }
            }
            //if there is lastVisitPlace at all
            if (!lastVisitPlace.equals("")) {
                nextPlaces.put(macAddr, lastVisitPlace);

            }
        }//end macAddressIteration

        //5. Rank order each "visited place" by the number of users
        //(from the queried semantic location) who visited it.
        //get all the distinct semanticPlace all users visited
        int totalUserWhoVisitedNextPlace = nextPlaces.size();
        int numberOfUsersNotCounted = numberOfUserFound - totalUserWhoVisitedNextPlace;

        HashSet<String> places = new HashSet<String>();
        places.addAll(nextPlaces.values());
        //<SemanticPlace, Count>
        HashMap<String, Integer> countPlace = new HashMap<String, Integer>();
        for (String sp : places) { //to count the number of occurence of SP in values()
            int occurence = Collections.frequency(nextPlaces.values(), sp);
            countPlace.put(sp, occurence);
        }
        System.out.println(countPlace);
        //to sort map based on the count
        SortedSet<Map.Entry<String, Integer>> sortedEntries = new TreeSet<Map.Entry<String, Integer>>(
                new Comparator<Map.Entry<String, Integer>>() {
                    @Override
                    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                }
        );
        sortedEntries.addAll(countPlace.entrySet());

        return null;
    }

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
                Logger.getLogger(BasicReportingDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
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

        ArrayList<Integer> resultList = new ArrayList<Integer>();

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
                Logger.getLogger(BasicReportingDAO.class
                        .getName()).log(Level.SEVERE, null, ex);
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
