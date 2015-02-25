/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.Companion;
import com.sloca.model.CompanionTimeSpent;
import com.sloca.model.TopKNextPlaceResult;
import com.sloca.model.UserVisitRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class BasicReportingDAO {

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

            endDateTime = DateUtilityDAO.getEndDate(startDateTime, "yyyy-MM-dd HH:mm:ss", -4320);
            System.out.println(endDateTime);

        } catch (ParseException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "SELECT location_lookup.semantic_name, count(*) "
                + "FROM "
                + "("
                + "SELECT max(time), student_mac_address, location_id, count(*) as REPEATED "
                + "FROM location "
                + "WHERE time > '" + endDateTime + "' AND time <= '" + startDateTime + "' "
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

    public static ArrayList<String> retrieveAllStudents() {

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

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
        }
        return nameList;
    }

    public static ArrayList<CompanionTimeSpent> retrieveTopKCompanions(int topK, String startDateTime, String endDateTime, String specifiedUser) throws ParseException {

        ArrayList<CompanionTimeSpent> sortedCompanionList = new ArrayList<CompanionTimeSpent>();

        //prepare connection
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //arraylist to store specified user at certain locations between specified time
        try {
            connection = ConnectionFactory.getConnection();

            //get specifiedUser's list of locations and timestamps between specified time window
            //start time -9 minute from specified window because there could be a 
            //possibility of the user coming at 1358 and be companion with someone from 1358 to 1403
            //but system might only update timestamp at 1403
            String queryListOfLocForSpecifiedUser
                    = "SELECT l.time, l.student_mac_address, l.location_id "
                    + "FROM location l INNER JOIN "
                    + "( SELECT mac_address FROM student WHERE name = '" + specifiedUser + "' ) as userMacAdd "
                    + "ON l.student_mac_address = userMacAdd.mac_address "
                    + "AND l.time BETWEEN DATE_SUB('" + startDateTime + "', INTERVAL 9 MINUTE) AND '" + endDateTime + "';";

            stmt = connection.prepareStatement(queryListOfLocForSpecifiedUser);

            rs = stmt.executeQuery();

            ArrayList<Companion> my_listOfLocationsAndTime = new ArrayList<Companion>();

            System.out.println(startDateTime);
            System.out.println(endDateTime);

            while (rs.next()) {
                String time = rs.getString(1);
                String mac_add = rs.getString(2);
                String loc_id = rs.getString(3);

                Companion c = new Companion(loc_id, mac_add, time);
                my_listOfLocationsAndTime.add(c);
            }

            stmt.close();

            ArrayList<Companion> processed_mylistOfLocationsAndTime = processList(my_listOfLocationsAndTime);

            System.out.println("processed my list size : " + processed_mylistOfLocationsAndTime.size());

            HashMap<String, Integer> companionTime = new HashMap<String, Integer>();
            //string = macAdd, int = time spent in seconds

            ArrayList<Companion> companion_listOfLocationsAndTime = new ArrayList<Companion>();
            ArrayList<Companion> processed_cListOfLocationsAndTime = new ArrayList<Companion>();

            for (int i = 0; i < processed_mylistOfLocationsAndTime.size(); i++) {

                String startTime = processed_mylistOfLocationsAndTime.get(i).getStartTime();

                if (startTime == null) {
                    startTime = "";
                }

                String processedEndTime = Utility.getEndDate(startTime, "yyyy-MM-dd HH:mm:ss", 9);

                if (DateUtilityDAO.isAfter(processedEndTime, endDateTime)) {
                    processedEndTime = endDateTime;
                }

                System.out.println("processed end time : " + processedEndTime);

                String matchingCompanions = "SELECT l.time, l.student_mac_address, l.location_id, s.email FROM location l "
                        + "LEFT OUTER JOIN student s on l.student_mac_address = s.mac_address "
                        + "WHERE l.student_mac_address != '" + processed_mylistOfLocationsAndTime.get(i).getMac_Address() + "' "
                        + "AND l.time BETWEEN DATE_SUB('" + startDateTime + "', INTERVAL 9 MINUTE) "
                        + "AND '" + processedEndTime + "' "
                        + "AND l.location_id = '" + processed_mylistOfLocationsAndTime.get(i).getLocation_ID() + "'";

                stmt = connection.prepareStatement(matchingCompanions);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String time = rs.getString(1);
                    String mac_add = rs.getString(2);
                    String loc_id = rs.getString(3);
                    String email = rs.getString(4);

                    Companion c = new Companion(loc_id, mac_add, time, email);
                    companion_listOfLocationsAndTime.add(c);
                }

                System.out.println("unprocessed companion list size : " + companion_listOfLocationsAndTime.size());

                processed_cListOfLocationsAndTime = processList(companion_listOfLocationsAndTime);

                System.out.println("processed companion list size : " + processed_cListOfLocationsAndTime.size());

                for (int j = 0; j < processed_cListOfLocationsAndTime.size(); j++) {
                    Companion user = processed_mylistOfLocationsAndTime.get(i);
                    Companion companion = processed_cListOfLocationsAndTime.get(j);

                    int timeSpent = computeTimeSpent(user, companion);

                    String companion_macAdd = companion.getMac_Address();

                    if (companionTime.get(companion_macAdd) == null) {
                        companionTime.put(companion_macAdd, timeSpent);
                    } else {
                        int timeAlrSpent = companionTime.get(companion_macAdd);
                        int totalTimeSpent = timeAlrSpent + timeSpent;
                        companionTime.put(companion_macAdd, totalTimeSpent);
                    }

//                    Iterator<String> iter = companionTime.keySet().iterator();
//                    
//                    while (iter.hasNext()) {
//                        String current_macAdd = iter.next();
//                        if (current_macAdd.equals(companion_macAdd)) {
//                            timeSpent += companionTime.get(current_macAdd);
//                            companionTime.remove(current_macAdd);
//                            companionTime.put(current_macAdd, timeSpent);
//                        } else {
//                            companionTime.put(companion.getMac_Address(), timeSpent);
//                        }
//                    }
                }
            }

            Iterator<String> iter = companionTime.keySet().iterator();
            while (iter.hasNext()) {
                String macAdd = iter.next();
                String email = null;
                for (int i = 0; i < companion_listOfLocationsAndTime.size(); i++) {
                    if (macAdd.equals(companion_listOfLocationsAndTime.get(i).getMac_Address())) {
                        email = companion_listOfLocationsAndTime.get(i).getEmail();
                    }
                }
                int timeSpent = companionTime.get(macAdd);
                CompanionTimeSpent c = new CompanionTimeSpent(macAdd, timeSpent, email);
                sortedCompanionList.add(c);
            }
            Collections.sort(sortedCompanionList);
            return sortedCompanionList;
        } catch (SQLException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sortedCompanionList;
    }

    //if user has 2 records; 1400 and 1402, Companion object for startTme = 1402 is
    //removed; Companion object for startTime 1400 has its end time changed to 1411
    //instead of 1409
    private static ArrayList<Companion> processList(ArrayList<Companion> unprocessedList) {

        // stores the processed list of user (the time they stay in location will be accurate)
        ArrayList<Companion> processedList = new ArrayList<Companion>();

        outer:
        for (int i = 0; i < unprocessedList.size(); i++) {

            Companion c = unprocessedList.get(i);

            String c_macAddress = c.getMac_Address();
            String c_locationID = c.getLocation_ID();
            String c_startTime = c.getStartTime();
            String c_email = c.getEmail();

            for (int j = 0; j < processedList.size(); j++) {

                Companion processed_Companion = processedList.get(j);

                String pc_macAddress = processed_Companion.getMac_Address();
                String pc_locationID = processed_Companion.getLocation_ID();
                String pc_startTime = processed_Companion.getStartTime();
                String pc_endTime = processed_Companion.getEndTime();

                if (c_macAddress.equals(pc_macAddress)) {
                    try {
                        // if macAddress matches, check if the time window clashes
                        if (DateUtilityDAO.isAfter(c_startTime, pc_startTime) && DateUtilityDAO.isAfter(pc_endTime, c_startTime)) {

                            // if time clash, need to see if it's same or different location
                            if (c_locationID.equals(pc_locationID)) {
                                // This is for Scenario 1, time clash and same location
                                // update new endTime

                                // get time difference; timeDiff = (c_startTime - pc_startTime) in seconds;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date c_startDateTime = sdf.parse(c_startTime);
                                Date pc_startDateTime = sdf.parse(pc_startTime);

                                int timeDiffinSeconds = (int) (c_startDateTime.getTime() - pc_startDateTime.getTime()) / 1000;

                                String processedEndTime = DateUtilityDAO.getEndDate(pc_endTime, "yyyy-MM-dd HH:mm:ss", (timeDiffinSeconds / 60));
                                processed_Companion.setEndTime(processedEndTime);

                                continue outer;
                            } else {
                                // This is for Scenario 2, time clash but location changed
                                // update new endTime

                                // then create a new object to store in processedList
                                processedList.add(new Companion(c_locationID, c_macAddress, c_startTime, c_email));

                                continue outer;
                            }

                        } else {
                            // This is for Scenario 3, no clash in timing (new location update)

                            processedList.add(new Companion(c_locationID, c_macAddress, c_startTime, c_email));

                            continue outer;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            // if a particular macAddress hasn't been entered in processedList yet or processedList size is 0
            processedList.add(new Companion(c_locationID, c_macAddress, c_startTime, c_email));

        }

        return processedList;

    }

    private static int computeTimeSpent(Companion user, Companion companion) {
        int timeSpent = 0;
        String uStartTime = user.getStartTime();
        String uEndTime = user.getEndTime();
        String cStartTime = companion.getStartTime();
        String cEndTime = companion.getEndTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date user_startTime = sdf.parse(uStartTime);
            Date user_endTime = sdf.parse(uEndTime);
            Date companion_startTime = sdf.parse(cStartTime);
            Date companion_endTime = sdf.parse(cEndTime);

            //scenario 1 (arrive at same time)
            if (user_startTime.compareTo(companion_startTime) == 0) {

                if (user_endTime.compareTo(companion_endTime) <= 0) {
                    timeSpent = (int) ((user_endTime.getTime() - user_startTime.getTime()) / 1000);
                } else if (user_endTime.compareTo(companion_endTime) > 0) {
                    timeSpent = (int) ((companion_endTime.getTime() - user_startTime.getTime()) / 1000);
                }
                //scenario 2 (i arrive later than companion)
            } else if (user_startTime.compareTo(companion_startTime) > 0) {
                if (companion_endTime.compareTo(user_startTime) < 0) {
                    return 0;
                }

                if (user_endTime.compareTo(companion_endTime) == 0 || user_endTime.compareTo(companion_endTime) < 0) {
                    timeSpent = (int) ((user_endTime.getTime() - user_startTime.getTime()) / 1000);
                } else if (user_endTime.compareTo(companion_endTime) > 0) {
                    timeSpent = (int) ((companion_endTime.getTime() - user_startTime.getTime()) / 1000);
                }
                //scenario 3 (i arrive earlier than companion)
            } else if (user_startTime.compareTo(companion_startTime) < 0) {
                if (companion_startTime.compareTo(user_endTime) >= 0) {
                    return 0;
                }

                if (user_endTime.compareTo(companion_endTime) <= 0) {
                    timeSpent = (int) ((user_endTime.getTime() - companion_startTime.getTime()) / 1000);
                } else if (user_endTime.compareTo(companion_endTime) > 0) {
                    timeSpent = (int) ((companion_endTime.getTime() - companion_startTime.getTime()) / 1000);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return timeSpent;
    }
}
