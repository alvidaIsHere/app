/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.AGDstatus;
import com.sloca.model.GroupLocations;
import com.sloca.model.GroupPopularPlace;
import com.sloca.model.GroupsFound;
import com.sloca.model.LocationTimeSpent;
import com.sloca.model.Members;
import com.sloca.model.TopKNextPlaceResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class GroupReportingDAO {

    private static final String LOCATION_LOOKUP_TABLE = "location_lookup";

    /**
     * for topKNextPlaceForGroup To retrieve all location id associated with a
     * particular semantic place
     *
     * @param semanticPlace
     * @return
     * @throws SQLException
     */
    public static ArrayList<String> getLocationId(String semanticPlace) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        if (semanticPlace == null) {
            return result;
        }
        String query = "SELECT * FROM " + LOCATION_LOOKUP_TABLE + " WHERE semantic_name='" + semanticPlace + "'";
        //prepare statement and connection to db
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int locationId = rs.getInt("location_id");
                result.add(locationId + "");
            }
            //close connection
            ConnectionFactory.close(connection, stmt, rs);
        }
        return result;
    }

    //forTopKNextPlace
    public static boolean isSameGroup(GroupsFound group1, GroupsFound group2) {
        if (group1 == null || group2 == null) {
            return false;
        }
        ArrayList<Members> members1 = group1.getMembers();
        ArrayList<Members> members2 = group2.getMembers();
        if (members1.size() != members2.size()) {
            return false;
        }

        for (Members member : members1) {
            if (!members2.contains(member)) {
                return false;
            }
        }
        return true;
    }

    //forTopKNextPlace
    //Discover all groups located at that place during the specified date / time using the previous 15 minute time window.
    public static ArrayList<GroupsFound> getGroupsLocatedAt(String semanticPlace, String atTime) throws ParseException, NullPointerException, SQLException {
        ArrayList<GroupsFound> groupsFound = new ArrayList<>();

        String before15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", -15);

        AGDstatus results = GroupsDAO.getUsersInLocation(before15Min, atTime);
        ArrayList<String> locationIds = getLocationId(semanticPlace);
        ArrayList<GroupsFound> groups = results.getGroups();

        //check if the location visit is the last visit within the 15 mins window
        //wiki: If a user has more than one location update within this previous 15 minute window, consider the most recent update only.
        if (groups != null) {
            for (GroupsFound gf : groups) {
                ArrayList<LocationTimeSpent> locations = gf.getLocations();
                //get the last location group visit in the next 15 mins
                LocationTimeSpent location = locations.get(-1);
                String locId = location.getLocation();

                if (locationIds.contains(locId)) {
                    groupsFound.add(gf);
                    break;
                }
            }
        }
        return groupsFound;
    }

    //3.Track each of these discovered users (i.e., located at the specified place)
//for the next  15  minutes to see if they visit another semantic place
//If a user visited multiple places within the next 15 minutes, consider the *last* one as the next visited place.
//elvin: assume next visit place cannot be the same as the original place
//user visit time start counting from inputTime
    public static HashMap<GroupsFound, String> getLastVisitForGroups(ArrayList<GroupsFound> groupsInPrev15Min, String semanticPlace, String atTime) throws ParseException, SQLException, NullPointerException {
        HashMap<GroupsFound, String> nextPlaces = new HashMap<>(); //HashMap<group, lastVisitSemanticLocation>
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String after15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 15);

            //all groups found in the later 15 min window
            AGDstatus agdStats = GroupsDAO.getUsersInLocation(atTime, after15Min);
            ArrayList<GroupsFound> groupsAfter15Min = agdStats.getGroups();

            //to find the location of same groups in the next 15 min
            for (GroupsFound group : groupsInPrev15Min) {
                for (GroupsFound anotherGroup : groupsAfter15Min) {
                    if (isSameGroup(group, anotherGroup)) {
                        //4. calculate the time spent
                        //To "visit a place", a user has to spend at least 5 minutes at that place.
                        if (anotherGroup.getTotal_time_spent() / 60 > 5) {
                            ArrayList<LocationTimeSpent> locations = anotherGroup.getLocations();
                            //get the last location group visit in the next 15 mins
                            LocationTimeSpent location = locations.get(-1);
                            String loc_id = location.getLocation();
                            String query = "SELECT semantic_name FROM " + LOCATION_LOOKUP_TABLE + " WHERE location_id=" + loc_id;
                            stmt = connection.prepareStatement(query);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                String nextSemanticPlace = rs.getString("semantic_name");
                                if (!nextSemanticPlace.equals(semanticPlace)) { //only consider the nextPlace if the group moved
                                    nextPlaces.put(group, nextSemanticPlace);
                                }
                            }
                        }
                    }
                }
            }

            ConnectionFactory.close(connection, stmt, rs);
        }
        return nextPlaces;
    }

    //for top K next place
    public static HashMap<Integer, ArrayList<TopKNextPlaceResult>> getTopKNextPlaces(Collection<String> nextPlaces, int kValue) throws NullPointerException {

        HashMap<Integer, ArrayList<TopKNextPlaceResult>> resultSet = new HashMap<>();
        HashSet<String> places = new HashSet<>();
        places.addAll(nextPlaces);
        //<SemanticPlace, Count>
        HashMap<String, Integer> countPlace = new HashMap<>();
        for (String sp : places) { //to count the number of occurence of SP in values()
            int occurence = Collections.frequency(nextPlaces, sp);
            countPlace.put(sp, occurence);
        }

        //to get all occurence of count and create arrayList for that count.
        //to know how many ranks are there
        HashSet<Integer> counts = new HashSet<>();
        counts.addAll(countPlace.values());
        for (int i = 1; i <= counts.size(); i++) {
            resultSet.put(i, new ArrayList<TopKNextPlaceResult>());
        }
        //to get descending sorted arrayList for number of count 
        ArrayList<Integer> countList = new ArrayList<>(counts);
        Collections.sort(countList, Collections.reverseOrder());
        //populate the resultSet

        for (Integer rank : resultSet.keySet()) {
            int countIndex = rank - 1;
            ArrayList<TopKNextPlaceResult> results = resultSet.get(rank);
            for (Map.Entry<String, Integer> entry : countPlace.entrySet()) {
                if (entry.getValue() == countList.get(countIndex)) {
                    TopKNextPlaceResult result = new TopKNextPlaceResult(entry.getKey(), entry.getValue());
                    results.add(result);

                }
            }
            //to have the result in alphabetical order

            Collections.sort(results);
        }

//        System.out.println(resultSet);
        return resultSet;
    }

    //for Group Top K Place
    public static ArrayList<GroupPopularPlace> retrieveGroupTopKPopularPlaces(ArrayList<GroupLocations> groupList, int k) {

        ArrayList<GroupPopularPlace> groupPopularPlaces = new ArrayList<GroupPopularPlace>();

        // Store how many groups went to each location id
        HashMap<String, Integer> locationID_Map = new HashMap<String, Integer>();

        // Store how many groups went to each semantic place
        HashMap<String, Integer> semanticPlace_Map = new HashMap<String, Integer>();

        for (int i = 0; i < groupList.size(); i++) {
            GroupLocations g = groupList.get(i);
            ArrayList<LocationTimeSpent> locationList = g.getLocations();

            for (int j = 0; j < locationList.size(); j++) {
                String locationID = locationList.get(j).getLocation();

                if (locationID_Map.get(locationID) == null) {
                    locationID_Map.put(locationID, 1);
                } else {
                    int count = locationID_Map.get(locationID);
                    locationID_Map.put(locationID, ++count);
                }
            }
        }

        // Combine location id into semantic places
        Iterator entries = locationID_Map.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String locID = (String) entry.getKey();
            Integer count = (Integer) entry.getValue();

            Connection connection = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            String insertString = "SELECT semantic_name FROM location_lookup WHERE location_id = '" + locID + "'";

            try {
                connection = ConnectionFactory.getConnection();
                stmt = connection.prepareStatement(insertString);
                rs = stmt.executeQuery();

                String semanticName = "";

                while (rs.next()) {
                    semanticName = rs.getString(1);
                }

                if (semanticPlace_Map.get(semanticName) == null) {
                    semanticPlace_Map.put(semanticName, count);
                } else {
                    int currCount = semanticPlace_Map.get(semanticName);
                    int newCount = currCount + count;
                    semanticPlace_Map.put(semanticName, newCount);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionFactory.close(connection, stmt, rs);
            }

        }

        ArrayList<GroupPopularPlace> tempListWithoutRank = new ArrayList<GroupPopularPlace>();

        Iterator semanticEntries = semanticPlace_Map.entrySet().iterator();

        while (semanticEntries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String semanticPlace = (String) entry.getKey();
            Integer count = (Integer) entry.getValue();

            tempListWithoutRank.add(new GroupPopularPlace(semanticPlace, count));
        }

        Collections.sort(tempListWithoutRank);

        int rank = 0;
        int prevCount = 0;

        for (int i = 0; i < tempListWithoutRank.size(); i++) {
            GroupPopularPlace currGroupPlace = tempListWithoutRank.get(i);

            if (rank == 0) {
                rank++;
                prevCount = currGroupPlace.getCount();
                currGroupPlace.setRank(rank);
                groupPopularPlaces.add(currGroupPlace);
            } else {
                if (currGroupPlace.getCount() < prevCount) {
                    rank++;

                    if (rank > k) {
                        break;
                    }

                    prevCount = currGroupPlace.getCount();
                    currGroupPlace.setRank(rank);
                    groupPopularPlaces.add(currGroupPlace);
                } else {
                    currGroupPlace.setRank(rank);
                    groupPopularPlaces.add(currGroupPlace);
                }
            }

        }

        return groupPopularPlaces;
    }

}
