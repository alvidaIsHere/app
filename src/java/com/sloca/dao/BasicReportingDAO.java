/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.Companion;
import com.sloca.model.CompanionTimeSpent;
import com.sloca.model.PopularPlace;
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
import java.util.Map;
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
     * returns a K number of popular places within the previous 15 minutes
     * windows query of the date specified.
     *
     *
     * @param topK the number of results user wants to see
     * @param startDateTime the start date and time of the query the user
     * specified
     * @return An ArrayList of rank results of the Top K Popular places
     * @see PopularPlace
     */
    public static ArrayList<PopularPlace> retrieveTopKPopularPlaces(int topK, String startDateTime) {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<PopularPlace> resultList = new ArrayList<PopularPlace>();

        String endDateTime = "";

        try {
            endDateTime = DateUtilityDAO.getEndDate(startDateTime, "yyyy-MM-dd HH:mm:ss", -15);
        } catch (ParseException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "SELECT semantic_name, count(max_time.student_mac_address) "
                + "FROM location_lookup INNER JOIN "
                + "(SELECT rawTable.time, rawTable.student_mac_address, rawTable.location_id "
                + "FROM "
                + "(SELECT time, student_mac_address, location_id "
                + "FROM location "
                + "WHERE time > '" + endDateTime + "' AND time <= '" + startDateTime + "' "
                + "ORDER BY time desc) AS rawTable "
                + "GROUP BY rawTable.student_mac_address) AS max_time "
                + "ON location_lookup.location_id = max_time.location_id "
                + "GROUP BY semantic_name "
                + "ORDER BY 2 desc, 1";

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
                        PopularPlace temp = new PopularPlace(rank, rs.getString(1), currentScore);
                        resultList.add(temp);
                        enterCount++;
                        continue;
                    }
                    if (rs.getInt(2) == currentScore) {
                        PopularPlace temp = new PopularPlace(rank, rs.getString(1), currentScore);
                        resultList.add(temp);
                    } else {
                        if (++rank > topK) {
                            break;
                        }
                        currentScore = rs.getInt(2);
                        PopularPlace temp = new PopularPlace(rank, rs.getString(1), currentScore);
                        resultList.add(temp);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ConnectionFactory.close(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);
        }

        return resultList;

    }

    /**
     * Gets all the names of locations in the database
     *
     * @return an ArrayList of locations from location-lookup
     */
    public static ArrayList<String> getAllLocationNames() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>();

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
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            ConnectionFactory.close(connection, stmt);
        }
        return resultList;
    }

    /**
     * Returns all location ID associated with the given semantic place
     *
     * @param semanticName name of location
     * @return an ArrayList of location IDs
     */
    public static ArrayList<Integer> getLocationIDs(String semanticName) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> resultList = new ArrayList<Integer>();

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
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            ConnectionFactory.close(connection, stmt);
        }
        return resultList;
    }

    /**
     * Gets all users located at a particular place during the specified time
     * using previous 15 minutes window
     *
     * @param semanticPlace name of location
     * @param atTime specific time in query window
     * @return an ArrayList of users at a particular location
     * @throws ParseException Signals that an error has been reached
     * unexpectedly while parsing.
     * @throws SQLException An exception that provides information on a database
     * access error or other errors
     * @throws NullPointerException An application attempts to use null in a
     * case where an object is required
     */
    public static ArrayList<String> getUsersLocatedAt(String semanticPlace, String atTime) throws ParseException, SQLException, NullPointerException {
        ArrayList<String> macAddresses = new ArrayList<String>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String before15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", -15);
            String selectStudents
                    = "SELECT DISTINCT student_mac_address "
                    + "FROM location JOIN location_lookup ON location.location_id=location_lookup.location_id "
                    + "AND semantic_name = '" + semanticPlace + "' "
                    + "AND time >'" + before15Min + "' "
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
                        = "SELECT semantic_name FROM location l "
                        + "JOIN location_lookup lu ON l.location_id=lu.location_id "
                        + "AND student_mac_address='" + macAddr + "' "
                        + "AND time<='" + atTime + "' "
                        + "AND time>'" + before15Min + "' "
                        + "ORDER BY time DESC "
                        + "LIMIT 1";

                stmt = connection.prepareStatement(selectLastVisitPlace);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    String lastLocation = rs.getString("semantic_name");
                    //remove this user if the last location is not the specified place
                    if (!lastLocation.equalsIgnoreCase(semanticPlace)) {
                        iter.remove();
                    }
                }
            }
            ConnectionFactory.close(rs);
            ConnectionFactory.close(connection, stmt);
        }
        return macAddresses;
    }

    /**
     *
     * Track each of these discovered users (i.e., located at the specified
     * place) for the next 15 minutes to see if they visit another semantic
     * place.
     * <p>
     * To "visit a place", a user has to spend at least 5 minutes continously at
     * that place. If a user visited multiple places for 5 minutes within the
     * next 15 minutes, only the last semantic place will be returned and next
     * place can be the same as the original semantic place (in the previous and
     * next window)
     *
     * @param macAddresses mac address of user
     * @param semanticPlace name of location
     * @param atTime specified time in query window
     * @return a HashMap of mac address that corresponds to the user's last
     * visited location
     * @throws ParseException Signals that an error has been reached
     * unexpectedly while parsing.
     * @throws SQLException An exception that provides information on a database
     * access error or other errors
     * @throws NullPointerException An application attempts to use null in a
     * case where an object is required
     */
    public static HashMap<String, String> getLastVisitForUsers(ArrayList<String> macAddresses, String semanticPlace, String atTime) throws ParseException, SQLException, NullPointerException {

        HashMap<String, String> nextPlaces = new HashMap<String, String>();
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String after15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 15);
            for (String macAddr : macAddresses) {

                String selectNextVisitPlace
                        = "SELECT * FROM location "
                        + "JOIN location_lookup on location.location_id = location_lookup.location_id "
                        + "AND student_mac_address='" + macAddr + "' "
                        + "AND time >'" + atTime + "' "
                        + "AND time <='" + after15Min + "' "
                        + "ORDER BY time ASC ";
                stmt = connection.prepareStatement(selectNextVisitPlace);
                rs = stmt.executeQuery();

                ArrayList<UserVisitRecord> userVisits = new ArrayList<UserVisitRecord>();
                String lastPlace = "";
                UserVisitRecord lastUvr = null;
                String lastEndTime = "";
                boolean firstRecord = true;
                while (rs.next()) {
                    String currentPlace = rs.getString("semantic_name");
                    String currentTime = rs.getString("time");

                    // calculate the time spent
                    //To "visit a place", a user has to spend at least 5 minutes at that place.
                    //do new class (UserVisitRecord): semanticPlace, timeSpent, startTime
                    if (firstRecord) {
                        if (DateUtilityDAO.getTimeDistanceSecond(atTime, currentTime) > 9 * 60) {
                            //if the first record is more than 9 mins after user specified time, 
                            //user assumed to be nowhere before current time. just reset initial place and time counter

                        } else {
                            //continue first record from initial place
                            String startTime = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 0.0167);//add 1 second to start time of next 15 mins window
                            UserVisitRecord newVisit = new UserVisitRecord(semanticPlace, startTime, currentTime);
                            userVisits.add(newVisit);
                            lastUvr = newVisit;
                        }
                        firstRecord = false;

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
                            } else {
                                String lastVisitPlace = lastUvr.getSemanticPlace();
                                if (lastVisitPlace.equals(lastPlace)) {//continuation of last visit
                                    lastUvr.setEndTime(currentTime);//prolong the visit time
                                } else { //user visit new place
                                    UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, currentTime);
                                    userVisits.add(newVisit);
                                    lastUvr = newVisit;
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
                        if (lastPlace.equals(lastUvr.getSemanticPlace())) {
                            //continuation of the last visit
                            lastUvr.setEndTime(currentEndTime);
                        } else {
                            UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, currentEndTime);
                            userVisits.add(newVisit);
                        }
                    } else {
                        //last record falls within last 9 mins of 15 mins window
                        if (lastPlace.equals(lastUvr.getSemanticPlace())) {
                            //continuation of the last visit
                            lastUvr.setEndTime(after15Min);
                        } else {
                            UserVisitRecord newVisit = new UserVisitRecord(lastPlace, lastEndTime, after15Min);
                            userVisits.add(newVisit);
                        }
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
                    //lastVisitPlace can be the same as semanticPlace
                    nextPlaces.put(macAddr, lastVisitPlace);
                }
            }
            ConnectionFactory.close(connection, stmt, rs);
        }
        return nextPlaces;
    }

    /**
     * Returns top k rank of next places.
     * <p>
     * rank will be sorted in descending order while places with a same rank
     * will be sorted lexicographically
     *
     * @param nextPlaces a list of last-visited-place that each user visits
     * within a specified time window
     * @param kValue an integer from 1 to 10 which displays the top k value when
     * selected in the query window
     * @return a HashMap of the top k next places that users are likely to visit
     * after visiting a particular place
     * @throws NullPointerException An application attempts to use null in a
     * case where an object is required
     */
    public static HashMap<Integer, ArrayList<TopKNextPlaceResult>> getTopKNextPlaces(HashMap<String, String> nextPlaces, int kValue) throws NullPointerException {
        //for top K next place
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

        return resultSet;
    }

    /**
     * return all unique names of students in demographics database
     *
     * @return an arraylist contains all students' name
     */
    public static ArrayList<String> retrieveAllStudents() {

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<String> nameList = new ArrayList<String>();

        String query = "SELECT DISTINCT student_mac_address FROM location";

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

    /**
     * Returns top-k other users who were co-located with a specified user
     * (using MAC address) in a specified query window.Two users are considered
     * to be co-located if their location updates have the same location id at
     * any time during the specified query window.
     * <p>
     * List of co-located users would be ranked based on the time spent together
     * with the specified user. Multiple users with the same amount of
     * co-located time would be assigned the same rank
     *
     * @param topK
     * @param startDateTime
     * @param endDateTime
     * @param macAddr
     * @return ArrayList of objects containing companion and the time spent of
     * this companion with the given mac address
     * @throws ParseException
     */
    public static ArrayList<CompanionTimeSpent> retrieveTopKCompanions(int topK, String startDateTime, String endDateTime, String macAddr) throws ParseException {

        ArrayList<CompanionTimeSpent> sortedCompanionList = new ArrayList<CompanionTimeSpent>();
        ArrayList<CompanionTimeSpent> filteredListToReturn = new ArrayList<CompanionTimeSpent>();

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
                    = "SELECT time, student_mac_address, location_id "
                    + "FROM location "
                    + "WHERE student_mac_address = '" + macAddr + "' "
                    + "AND time > '" + endDateTime + "' "
                    + "AND time <= '" + startDateTime + "' "
                    + "ORDER BY 1, 3;";

            stmt = connection.prepareStatement(queryListOfLocForSpecifiedUser);
            rs = stmt.executeQuery();
            ArrayList<Companion> my_listOfLocationsAndTime = new ArrayList<Companion>();

            while (rs.next()) {
                String time = rs.getString(1);
                String mac_add = rs.getString(2);
                String loc_id = rs.getString(3);

                String endTime = Utility.getEndDate(time, "yyyy-MM-dd HH:mm:ss", 9);
                if (DateUtilityDAO.isAfter(endTime, startDateTime)) {
                    endTime = startDateTime;
                }

                Companion c = new Companion(loc_id, mac_add, time, endTime, "");
                my_listOfLocationsAndTime.add(c);
            }

            stmt.close();

            ArrayList<Companion> processed_mylistOfLocationsAndTime = processList(my_listOfLocationsAndTime);

            if (processed_mylistOfLocationsAndTime.isEmpty()) {
                return sortedCompanionList;
            }

            HashMap<String, Integer> companionTime = new HashMap<String, Integer>();
            //string = macAdd (of companion), int = time spent with selected user in seconds 

            /* 
             1) Loop through processed_mylistOfLocationsAndTime
             2) For each record inside there, do the 15 minute query to find other users at the specified location
             and store them in an ArrayList<Companion>
             3) Process this ArrayList<Companion>
             4) For each record in this processed list, compute time spent together and store in HashMap
             */
            for (int i = 0; i < processed_mylistOfLocationsAndTime.size(); i++) {

                Companion user = processed_mylistOfLocationsAndTime.get(i);

                String locationID = user.getLocation_ID();
                String endTime = user.getEndTime();

                ArrayList<Companion> companion_listOfLocationsAndTime = new ArrayList<Companion>();

                String companions
                        = "SELECT l.time, l.student_mac_address, l.location_id, s.email FROM location l "
                        + "LEFT OUTER JOIN student s ON l.student_mac_address = s.mac_address "
                        + "WHERE l.student_mac_address != '" + macAddr + "' "
                        + "AND l.time > '" + endDateTime + "' "
                        + "AND l.time < '" + endTime + "' "
                        + "AND l.location_id = '" + locationID + "' "
                        + "ORDER BY 2, 1";

                stmt = connection.prepareStatement(companions);

                rs = stmt.executeQuery();

                while (rs.next()) {
                    String time = rs.getString(1);
                    String mac_add = rs.getString(2);
                    String loc_id = rs.getString(3);
                    String email = rs.getString(4);

                    if (email == null || email.equals("null")) {
                        email = "";
                    }

                    String procEndTime = Utility.getEndDate(time, "yyyy-MM-dd HH:mm:ss", 9);

                    if (DateUtilityDAO.isAfter(procEndTime, startDateTime)) {
                        procEndTime = startDateTime;
                    }

                    Companion c = new Companion(loc_id, mac_add, time, procEndTime, email);
                    companion_listOfLocationsAndTime.add(c);
                }

                stmt.close();

                if (companion_listOfLocationsAndTime.isEmpty()) {
                    continue;
                }

                ArrayList<Companion> processed_cListOfLocationsAndTime = processList(companion_listOfLocationsAndTime);

                for (int j = 0; j < processed_cListOfLocationsAndTime.size(); j++) {
                    Companion company = processed_cListOfLocationsAndTime.get(j);

                    String c_macAddr = company.getMac_Address();

                    if (companionTime.get(c_macAddr) == null) {
                        int timeSpent = computeTimeSpent(user, company);
                        companionTime.put(c_macAddr, timeSpent);
                    } else {
                        int timeAlrSpent = companionTime.get(c_macAddr);
                        int totalTimeSpent = timeAlrSpent + computeTimeSpent(user, company);
                        companionTime.put(c_macAddr, totalTimeSpent);
                    }
                }
            }

            Iterator entries = companionTime.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String keyMacAddr = (String) entry.getKey();
                Integer valueTimeTogether = (Integer) entry.getValue();
                sortedCompanionList.add(new CompanionTimeSpent(keyMacAddr, valueTimeTogether));
            }

            Collections.sort(sortedCompanionList);

            int rank = 0;
            int prevCount = 0;

            for (int i = 0; i < sortedCompanionList.size(); i++) {
                CompanionTimeSpent companion = sortedCompanionList.get(i);

                if (rank == 0) {
                    if (companion.getTimeSpent() > 0) {
                        rank++;
                        prevCount = companion.getTimeSpent();
                        companion.setRank(rank);
                        filteredListToReturn.add(companion);
                    } else {
                        break;
                    }
                } else {
                    if (companion.getTimeSpent() < prevCount) {
                        rank++;
                        if (rank > topK || companion.getTimeSpent() == 0) {
                            break;
                        }
                        prevCount = companion.getTimeSpent();
                        companion.setRank(rank);
                        filteredListToReturn.add(companion);
                    } else {
                        companion.setRank(rank);
                        filteredListToReturn.add(companion);
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < filteredListToReturn.size(); i++) {
            CompanionTimeSpent c = filteredListToReturn.get(i);
        }

        // Add email to all filteredListToReturn
        for (int i = 0; i < filteredListToReturn.size(); i++) {
            CompanionTimeSpent c = filteredListToReturn.get(i);
            String email = "";
            String getEmail = "SELECT email FROM student WHERE mac_address = \"" + c.getMac_Address() + "\"";
            try {
                stmt = connection.prepareStatement(getEmail);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    String student_email = rs.getString(1);
                    email = student_email;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            c.setEmail(email);
        }

        ArrayList<CompanionTimeSpent> filteredAndOrderedByMacAddr = new ArrayList<CompanionTimeSpent>();
        int numOfDiffRanks = 0;
        // Map mac address to email
        HashMap<String, CompanionTimeSpent> macEmailMapping = new HashMap<String, CompanionTimeSpent>();
        // Map rank to ArrayList of mac addresses
        HashMap<Integer, ArrayList<String>> rankToMacMapping = new HashMap<Integer, ArrayList<String>>();

        for (int i = 0; i < filteredListToReturn.size(); i++) {
            CompanionTimeSpent cts = filteredListToReturn.get(i);
            int rank = cts.getRank();
            String mac = cts.getMac_Address();

            if (rankToMacMapping.get(rank) == null) {
                numOfDiffRanks++;
                ArrayList<String> macAddresses = new ArrayList<String>();
                macAddresses.add(mac);
                rankToMacMapping.put(rank, macAddresses);
                macEmailMapping.put(mac, cts);
            } else {
                ArrayList<String> macAddresses = rankToMacMapping.get(rank);
                macAddresses.add(mac);
                macEmailMapping.put(mac, cts);
            }
        }

        for (int i = 1; i <= numOfDiffRanks; i++) {
            ArrayList<String> macList = rankToMacMapping.get(i);
            Collections.sort(macList);

            for (int j = 0; j < macList.size(); j++) {
                filteredAndOrderedByMacAddr.add(macEmailMapping.get(macList.get(j)));
            }
        }
        return filteredAndOrderedByMacAddr;
    }

    /**
     * returns a list of processed companions where multiple records retrieved
     * from database has been combined.
     * <p>
     * For example, if user has 2 records; 1400 and 1402, Companion object for
     * startTme = 1402 is removed; Companion object for startTime 1400 has its
     * end time changed to 1411 instead of 1409
     *
     * @param unprocessedList arraylist of raw list retrieved
     * @return an arraylist of companion which has been processed
     */
    private static ArrayList<Companion> processList(ArrayList<Companion> unprocessedList) {

        // stores the processed list of user (the time they stay in location will be accurate)
        ArrayList<Companion> processedList = new ArrayList<Companion>();
        outer:
        for (int i = 0; i < unprocessedList.size(); i++) {

            Companion c = unprocessedList.get(i);

            String c_macAddress = c.getMac_Address();
            String c_locationID = c.getLocation_ID();
            String c_startTime = c.getStartTime();
            String c_endTime = c.getEndTime();
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
                                processed_Companion.setEndTime(c_startTime);
                                processedList.add(new Companion(c_locationID, c_macAddress, c_startTime, c_endTime, c_email));

                                continue outer;
                            }

                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(BasicReportingDAO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // if a particular macAddress hasn't been entered in processedList yet or processedList size is 0
            processedList.add(new Companion(c_locationID, c_macAddress, c_startTime, c_endTime, c_email));
        }
        return processedList;

    }

    /**
     * Returns the time spent in seconds between the user and his companion
     *
     * @param user
     * @param companion
     * @return time spent in seconds
     */
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
