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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jeremy.kuah.2013
 * @author baoyi.soh.2011
 * @author eprananta.2013
 */
public class BasicReportingDAO12Oct14 {

    private static final String LOCATION_LOOKUP_TABLE = "location_lookup";

    /**
     * Takes in the Top Number of Popular Place results to be displayed given
     * the time window specified by user
     *
     *
     * @param topK the number of results user wants to see
     * @param startDateTime the start date and time of the query the user
     * specified
     * @return An ArrayList of rank results of the Top K Popular places
     */
    public static ArrayList<ArrayList<String>> retrieveTopKPopularPlaces(int topK, String startDateTime) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
        String endDateTime = "";
        try {
            // test date now is from '2014-3-1 00:00:45' to '2014-3-22 23:52:00'
            // need to invoker method to get endDateTime
            // String endDateTime = Utility.getEndDate(startDateTime, "MM/dd/yyyy hh:mm a", -15);
            endDateTime = DateUtilityDAO.getEndDate(startDateTime, "yyyy-MM-dd HH:mm:ss", -15);
            System.out.println(endDateTime);
        } catch (ParseException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT location_lookup.semantic_name, count(*) "
                + "FROM "
                + "("
                + "SELECT max(time), student_mac_address, location_id, count(*) as REPEATED "
                + "FROM location "
                + "WHERE time > '" + endDateTime + "' AND time <= '" + startDateTime + "' " // to be replaced with endDateTime and startDateTime
                + "GROUP BY student_mac_address"
                + ") AS latest_Time "
                + "INNER JOIN location_lookup ON latest_Time.location_id = location_lookup.location_id "
                + "GROUP BY location_lookup.semantic_name "
                + "ORDER BY 2 desc, 1;";
        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            int currentScore = 0;
            int rank = 1;
            int enterCount = 1;
            try {
                while (rs.next()) {
                    if (enterCount == 1) {
                        currentScore = rs.getInt(2);
                        ArrayList<String> result = new ArrayList<String>();
                        result.add(Integer.toString(rank));
                        result.add(rs.getString(1));
                        resultList.add(result);
                        enterCount++;
                        continue;
                    }
                    if (rs.getInt(2) == currentScore) {
                        ArrayList<String> result = resultList.get(rank - 1);
                        result.add(rs.getString(1));
                    } else {
                        if (++rank > topK) {
                            break;
                        }
                        currentScore = rs.getInt(2);
                        ArrayList<String> result = new ArrayList<String>();
                        result.add(Integer.toString(rank));
                        result.add(rs.getString(1));
                        resultList.add(result);
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

    public static ArrayList<String> getAllLocationNames() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>() {
        };
        String query = "SELECT * FROM " + LOCATION_LOOKUP_TABLE;
        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            try {
                while (rs.next()) {
                    String semanticName = rs.getString("semantic_name");
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

    public static ArrayList<Integer> getLocationIDs(String semanticName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Integer> resultList = new ArrayList<Integer>() {
        };

        String query = "SELECT " + "location_id" + " FROM " + LOCATION_LOOKUP_TABLE + " WHERE " + "semantic_name" + "= '" + semanticName + "'";

        try {

            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            try {
                while (rs.next()) {
                    int locationId = rs.getInt("location_id");
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

    //forTopKNextPlace
    //Discover all users located at that place during the specified date / time using the previous 15 minute time window.
    public static ArrayList<String> getUsersLocatedAt(String semanticPlace, String atTime) throws ParseException, SQLException, NullPointerException {
        ArrayList<String> macAddresses = new ArrayList<String>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String before15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", -15);
            String selectStudents
                    = "SELECT DISTINCT student_mac_address "
                    + "FROM location JOIN location_lookup ON location.location_id=location_lookup.location_id "
                    + "AND semantic_name = '" + semanticPlace + "'"
                    + "AND time >'" + before15Min + "'"
                    + "AND time <='" + atTime + "'";
            macAddresses = new ArrayList<>();
            stmt = connection.prepareStatement(selectStudents);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String macAddr = rs.getString("student_mac_address");
                macAddresses.add(macAddr);
//                System.out.println(macAddr);
            }

            //If a user has more than one location update within this previous 15 minute window, consider the most recent update only.
            Iterator iter = macAddresses.iterator();
            while (iter.hasNext()) {
                String macAddr = (String) iter.next();
                String selectLastVisitPlace
                        = "SELECT "
                        + "MAX(time) AS time, semantic_name "
                        + "FROM location JOIN location_lookup "
                        + "ON location.location_id = location_lookup.location_id "
                        + "AND student_mac_address='" + macAddr + "' "
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
            ConnectionFactory.close(rs);
            ConnectionFactory.close(connection, stmt);
        }
        return macAddresses;
    }

    //3.Track each of these discovered users (i.e., located at the specified place)
    //for the next  15  minutes to see if they visit another semantic place
    //If a user visited multiple places within the next 15 minutes, consider the *last* one as the next visited place.
    //elvin: assume next visit place cannot be the same as the original place
    //user visit time start counting from inputTime
    public static HashMap<String, String> getLastVisitForUsers(ArrayList<String> macAddresses, String semanticPlace, String atTime) throws ParseException, SQLException, NullPointerException {
        HashMap<String, String> nextPlaces = new HashMap<String, String>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String after15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 15);
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
//                String initialTime = atTime;
                String lastPlace = "";
                UserVisitRecord lastUvr = null;
                String lastEndTime = "";
//                System.out.println(macAddr);
                boolean firstRecord = true;
                while (rs.next()) {
                    String currentPlace = rs.getString("semantic_name");
                    String currentTime = rs.getString("time");
//                    System.out.println();
//                    System.out.println(macAddr);
//                    System.out.println(semanticPlaceUserVisit);
//                    System.out.println(currentTime);
                    //4. calculate the time spent
                    //To "visit a place", a user has to spend at least 5 minutes at that place.
                    //do new class (UserVisitRecord): semanticPlace, timeSpent, startTime
                    if (firstRecord) {
                        if (DateUtilityDAO.getTimeDistanceSecond(atTime, currentTime) > 9 * 60) {
                            //if the first record is more than 9 mins after user specified time, 
                            //user assumed to be nowhere before current time. just reset initial place and time counter

                        } else {
                            //continue first record from initial place
                            UserVisitRecord newVisit = new UserVisitRecord(semanticPlace, atTime, currentTime);
                            userVisits.add(newVisit);
                            lastUvr = newVisit;

                        }
                        firstRecord = false;
//                        System.out.println("first record" + lastUvr);

                    } else {
                        if (DateUtilityDAO.getTimeDistanceSecond(lastEndTime, currentTime) > 9 * 60) {
                            String currentEndTime = DateUtilityDAO.getEndDate(lastEndTime, "yyyy-MM-dd HH:mm:ss", 9);
                            if (lastUvr == null) {
                                UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, currentEndTime);
                                userVisits.add(newVisit);

                            } else {
                                lastUvr.setEndTime(currentEndTime); //add 9 mins to last visit record and set the record to null again
                            }
                            lastUvr = null;
                        } else {
                            if (lastUvr == null) {
                                UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, currentTime);
                                userVisits.add(newVisit);
                                lastUvr = newVisit;
//                                System.out.println("after null" + lastUvr);
                            } else {
                                String lastVisitPlace = lastUvr.getSemanticPlace();
                                if (lastVisitPlace.equals(lastPlace)) {//continuation of last visit
                                    lastUvr.setEndTime(currentTime);//prolong the visit time
//                                    System.out.println("notNull same place" + lastUvr);
                                } else { //user visit new place
                                    UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, currentTime);
                                    userVisits.add(newVisit);
                                    lastUvr = newVisit;
//                                    System.out.println("notNull new place" + lastUvr);
                                }
                            }
                        }

                    }
                    //reset the place and time for next record
                    lastPlace = currentPlace;
                    lastEndTime = currentTime;

                }

                //add time for last record until the end of 15 mins window
                if (firstRecord) {
                    continue; //if firstRecord is true, means there are no records retrieved for this user, continue with other macAddr
                }
                //else
                if (lastUvr == null) {
                    if (DateUtilityDAO.getTimeDistanceSecond(lastEndTime, after15Min) > 9 * 60) {
                        //1. only one record found and it falls within first 9 mins of 15 mins window
                        String currentEndTime = DateUtilityDAO.getEndDate(lastEndTime, "yyyy-MM-dd HH:mm:ss", 9);
                        UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, currentEndTime);
                        userVisits.add(newVisit);
                    } else {
                        //2. only one record found and it falls within last 9 mins of 15 mins window
                        UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, after15Min);
                        userVisits.add(newVisit);
                    }
                } else {
                    if (DateUtilityDAO.getTimeDistanceSecond(lastEndTime, after15Min) > 9 * 60) {
                        //3. last record falls within first 9 mins of 15 mins window
                        String currentEndTime = DateUtilityDAO.getEndDate(lastEndTime, "yyyy-MM-dd HH:mm:ss", 9);
                        lastUvr.setEndTime(currentEndTime);
                    } else {
                        //4. last record falls within last 9 mins of 15 mins window
                        lastUvr.setEndTime(after15Min);
                    }
                }

                //eliminate those that does not reach 5 min, get the last semantic place visited
                lastEndTime = atTime;
                String lastVisitPlace = "";
                for (UserVisitRecord uvr : userVisits) {
                    int seconds = uvr.getTimeSpentInSeconds();
                    String fromTime = uvr.getStartTime();
                    if (seconds >= 5 * 60 && DateUtilityDAO.isAfter(fromTime, lastEndTime)) {
                        lastVisitPlace = uvr.getSemanticPlace();
                        lastEndTime = fromTime;
                    }
                }
                //if there is lastVisitPlace at all
                if (!lastVisitPlace.equals("")) {
                    //if lastVisitPlace is not the same as semanticPlace
                    if (!lastVisitPlace.equals(semanticPlace)) {
                        nextPlaces.put(macAddr, lastVisitPlace);
                    }
                }

            }
            ConnectionFactory.close(connection, stmt, rs);

        }
        return nextPlaces;
    }

    //for top K next place
    public static HashMap<Integer, ArrayList<TopKNextPlaceResult>> getTopKNextPlaces(HashMap<String, String> nextPlaces, int kValue) throws NullPointerException {

        HashMap<Integer, ArrayList<TopKNextPlaceResult>> resultSet = new HashMap<Integer, ArrayList<TopKNextPlaceResult>>();
        HashSet<String> places = new HashSet<String>();
        places.addAll(nextPlaces.values());
        //<SemanticPlace, Count>
        HashMap<String, Integer> countPlace = new HashMap<String, Integer>();
        for (String sp : places) { //to count the number of occurence of SP in values()
            int occurence = Collections.frequency(nextPlaces.values(), sp);
            countPlace.put(sp, occurence);
        }

        //to get all occurence of count and create arrayList for that count.
        //to know how many ranks are there
        HashSet<Integer> counts = new HashSet<Integer>();
        counts.addAll(countPlace.values());
        for (int i = 1; i <= counts.size(); i++) {
            resultSet.put(i, new ArrayList<TopKNextPlaceResult>());
        }
        //to get descending sorted arrayList for number of count 
        ArrayList<Integer> countList = new ArrayList<Integer>(counts);
        Collections.sort(countList, Collections.reverseOrder());
        //populate the resultSet

        for (Integer rank : resultSet.keySet()) {
            int countIndex = rank - 1;
            ArrayList<TopKNextPlaceResult> results = resultSet.get(rank);
            for (Entry<String, Integer> entry : countPlace.entrySet()) {
                if (entry.getValue() == countList.get(countIndex)) {
                    TopKNextPlaceResult result = new TopKNextPlaceResult(entry.getKey(), entry.getValue());
                    results.add(result);

                }
            }
            //to have the result in alphabetical order
            Collections.sort(results);
        }

        System.out.println(resultSet);

        //TODO arrange resultSet
        return resultSet;
    }

    public static ArrayList<String> retrieveAllStudents() throws SQLException {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<String> nameList = new ArrayList<String>();

        String query = "SELECT name from student;";

        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                nameList.add(rs.getString(1));
            }
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
        }
        return nameList;

    }

    public static ArrayList<ArrayList<String>> retrieveTopKCompanions(int topK, String startDateTime, String endDateTime, String specifiedUser) throws ParseException {

        //prepare connection
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //arraylist to store specified user at certain locations between specified time
        ArrayList<ArrayList<String>> userLocList = new ArrayList<ArrayList<String>>();

        try {
            connection = ConnectionFactory.getConnection();

            //query to get time, macAdd & loc ID for specified user between the specified window
            String queryListOfLocForSpecifiedUser = "SELECT * FROM location l "
                    + "WHERE student_mac_address = " + specifiedUser
                    + " AND l.time > " + startDateTime
                    + " AND l.time < " + endDateTime
                    + "GROUP BY l.location_id, l.time;";
            stmt = connection.prepareStatement(queryListOfLocForSpecifiedUser);
            rs = stmt.executeQuery();
            ArrayList<String> eachRecord = new ArrayList<String>();

            while (rs.next()) {
                eachRecord.add(rs.getString(1));
                eachRecord.add(rs.getString(2));
                eachRecord.add(rs.getString(3));

                userLocList.add(eachRecord);
            }

            for (int i = 0; i < userLocList.size(); i++) {
                String timestamp = userLocList.get(i).get(0);

                //*****need to cater to only 8.99minutes or 9 minutes?
                //String maxPossibleNextUpdateTimestamp = Utility.getEndDate(timestamp, "yyyy-MM-dd HH:mm:ss", 9);
                //to find the list of companions who were co-located with user within the specified window
                String matchingCompanions = "SELECT * from location "
                        + "WHERE student_mac_address != " + userLocList.get(i).get(1)
                        + "AND time > DATE_ADD(" + startDateTime + ", INTERVAL -9 MINUTE)"
                        + "AND time < DATE_ADD(" + endDateTime + ", INTERVAL 9 MINUTE);";

                stmt = connection.prepareStatement(matchingCompanions);
                rs = stmt.executeQuery();

            }

        } catch (SQLException e) {
            //do something
        }

        ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();

        String before15Min = "";
        String after15Min = "";

        try {
            before15Min = DateUtilityDAO.getEndDate(startDateTime, "yyyy-MM-dd HH:mm:ss", -15);
            after15Min = DateUtilityDAO.getEndDate(startDateTime, "yyyy-MM-dd HH:mm:ss", 15);

        } catch (ParseException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error : datetime input error");
        }

        //to get all location ids of this semanticPlace
        ArrayList<Integer> locationIds = getLocationIDs(startDateTime);

        HashMap<Integer, Integer> locationCounts = new HashMap<Integer, Integer>();
        //TODO to be revised
        try {
            for (int locationId : locationIds) {

                String query = //number of student visit this locationID after visiting semantic name
                        "SELECT COUNT(student_mac_address), locationCount.location_id, location_lookup.semantic_name FROM("
                        + "SELECT location.student_mac_address, MAX(location.time), location.location_id FROM location JOIN"
                        + "(SELECT student_mac_address, MAX(time)"
                        + " FROM location"
                        + " WHERE location.location_id = '" + locationId + "'"
                        + " AND location.time "
                        + " BETWEEN '" + before15Min + "' "
                        + " AND '" + startDateTime + "'"
                        + " GROUP BY student_mac_address) student "
                        + " ON location.student_mac_address=student.student_mac_address "
                        + " AND location.time BETWEEN '" + startDateTime + "' "
                        + " AND '" + after15Min + "' "
                        + " AND location.location_id!= '" + locationId + "'"
                        + "GROUP BY location.student_mac_address) locationCount "
                        + "JOIN location_lookup ON locationCount.location_id = location_lookup.location_id GROUP BY location_id";

                stmt = connection.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {

                }
            }

            int currentScore = 0;
            int rank = 1;
            int enterCount = 1;

            try {
                while (rs.next()) {
                    if (enterCount == 1) {
                        currentScore = rs.getInt(2);

                        ArrayList<String> result = new ArrayList<String>();

                        result.add(Integer.toString(rank));
                        result.add(rs.getString(1));

                        resultList.add(result);

                        enterCount++;
                        continue;
                    }

                    if (rs.getInt(2) == currentScore) {

                        ArrayList<String> result = resultList.get(rank - 1);
                        result.add(rs.getString(1));

                    } else {

                        if (++rank > topK) {
                            break;
                        }

                        currentScore = rs.getInt(2);

                        ArrayList<String> result = new ArrayList<String>();

                        result.add(Integer.toString(rank));
                        result.add(rs.getString(1));

                        resultList.add(result);
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

}
