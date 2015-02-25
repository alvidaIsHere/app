/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.GroupLocations;
import com.sloca.model.GroupPopularPlace;
import com.sloca.model.LocationTimeSpent;
import com.sloca.model.TopKNextPlaceResult;
import com.sloca.model.UserLocationInterval;
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

public class GroupReportingDAO13Nov14 {

    private static final String LOCATION_LOOKUP_TABLE = "location_lookup";

    private static ArrayList<GroupLocations> getFakeGroup1() {
        //2 groups from 15:16:00-15:30:00
        ArrayList<GroupLocations> result = new ArrayList<>();
        //group 1 : 2014-03-29 15:16:00 to 2014-03-29 15:20:00 @ 1010100037, SMUSISL1RECEPTION(NEW)
        String startTime = "2014-03-29 15:16:00";
        String endTime = "2014-03-29 15:20:00";
        String loc = "1010100037";//SMUSISL1RECEPTION(NEW)
        UserLocationInterval user1 = new UserLocationInterval("mac1", "email1", loc, startTime, endTime);
        UserLocationInterval user2 = new UserLocationInterval("mac2", "email3", loc, startTime, endTime);
        UserLocationInterval user3 = new UserLocationInterval("mac3", "email2", loc, startTime, endTime);
        ArrayList<UserLocationInterval> ULIs = new ArrayList<>();
        ULIs.add(user1);
        ULIs.add(user2);
        ULIs.add(user3);
        ArrayList<LocationTimeSpent> LTSs = new ArrayList<>();

        LocationTimeSpent LTS1 = new LocationTimeSpent(loc, 1200, startTime, endTime);

        LTSs.add(LTS1);
        //then procees to 1010100028,SMUSISL1LOBBY(NEW) (origin) from 2014-03-29 15:22:00 to 2014-03-29 15:29:00
        startTime = "2014-03-29 15:22:00";
        endTime = "2014-03-29 15:29:00";
        loc = "1010100028";
        LocationTimeSpent LTS2 = new LocationTimeSpent(loc, 1200, startTime, endTime);
        LTSs.add(LTS2);
        GroupLocations gl1 = new GroupLocations(ULIs, LTSs);
        result.add(gl1);

        //group 2 : 2014-03-29 15:16:00 to 2014-03-29 15:20:00 @ 1010100013, SMUSISL1WAITINGAREA(NEW)
        startTime = "2014-03-29 15:16:00";
        endTime = "2014-03-29 15:20:00";
        loc = "1010100013";
        user1 = new UserLocationInterval("mac4", "email4", loc, startTime, endTime);
        user2 = new UserLocationInterval("mac5", "email5", loc, startTime, endTime);
        ULIs = new ArrayList<>();
        ULIs.add(user1);
        ULIs.add(user2);

        LTSs = new ArrayList<>();
        LTS1 = new LocationTimeSpent(loc, 1200, startTime, endTime);
        LTSs.add(LTS1);
        //then procees to 1010100029,SMUSISL1LOBBY(NEW) (origin) from 2014-03-29 15:22:00 to 2014-03-29 15:29:00
        startTime = "2014-03-29 15:22:00";
        endTime = "2014-03-29 15:29:00";
        loc = "1010100029";
        LTS1 = new LocationTimeSpent(loc, 1200, startTime, endTime);
        LTSs.add(LTS1);
        gl1 = new GroupLocations(ULIs, LTSs);

        result.add(gl1);
        return result;

    }

    private static ArrayList<GroupLocations> getFakeGroup2() {
        //2 groups from 15:16:00-15:30:00
        ArrayList<GroupLocations> result = new ArrayList<>();
        //group 1 :  @ 1010100037, SMUSISL1RECEPTION(NEW)
        String startTime = "2014-03-29 15:30:10";
        String endTime = "2014-03-29 15:36:00";
        String loc = "1010100037";//SMUSISL1RECEPTION(NEW)
        UserLocationInterval user1 = new UserLocationInterval("mac1", "email1", loc, startTime, endTime);
        UserLocationInterval user2 = new UserLocationInterval("mac2", "email3", loc, startTime, endTime);
        UserLocationInterval user3 = new UserLocationInterval("mac3", "email2", loc, startTime, endTime);
        ArrayList<UserLocationInterval> ULIs = new ArrayList<>();
        ULIs.add(user1);
        ULIs.add(user2);
        ULIs.add(user3);
        ArrayList<LocationTimeSpent> LTSs = new ArrayList<>();
        LocationTimeSpent LTS1 = new LocationTimeSpent(loc, 3600, startTime, endTime);
        LTSs.add(LTS1);

//        then procees to 1010100028,SMUSISL1LOBBY(NEW) (origin) 
        startTime = "2014-03-29 15:37:00";
        endTime = "2014-03-29 15:43:00";
        loc = "1010100028";
        LocationTimeSpent LTS2 = new LocationTimeSpent(loc, 1200, startTime, endTime);
        LTSs.add(LTS2);

        GroupLocations gl1 = new GroupLocations(ULIs, LTSs);
        result.add(gl1);

//        group 2 : @ 1010100013, SMUSISL1WAITINGAREA(NEW)
        startTime = "2014-03-29 15:31:00";
        endTime = "2014-03-29 15:36:20";
        loc = "1010100013";
        user1 = new UserLocationInterval("mac4", "email4", loc, startTime, endTime);
        user2 = new UserLocationInterval("mac5", "email5", loc, startTime, endTime);
        ULIs = new ArrayList<>();
        ULIs.add(user1);
        ULIs.add(user2);

        LTSs = new ArrayList<>();
        LTS1 = new LocationTimeSpent(loc, 1200, startTime, endTime);
        LTSs.add(LTS1);
        //then procees to 1010100029,SMUSISL1LOBBY(NEW) (origin) 
        startTime = "2014-03-29 15:37:00";
        endTime = "2014-03-29 15:38:00";
        loc = "1010100029";
        LTS1 = new LocationTimeSpent(loc, 1200, startTime, endTime);
        LTSs.add(LTS1);
        gl1 = new GroupLocations(ULIs, LTSs);

        result.add(gl1);
        return result;

    }

    private static ArrayList<GroupLocations> getFakeGroup3() {
//        groups in fakeGroup1 merged into 1 group

        ArrayList<GroupLocations> result = new ArrayList<>();
        //group 1 :  @ 1010100037, SMUSISL1RECEPTION(NEW)
        String startTime = "2014-03-29 15:30:10";
        String endTime = "2014-03-29 15:36:00";
        String loc = "1010100037";//SMUSISL1RECEPTION(NEW)
        UserLocationInterval user1 = new UserLocationInterval("mac1", "email1", loc, startTime, endTime);
        UserLocationInterval user2 = new UserLocationInterval("mac2", "email3", loc, startTime, endTime);
        UserLocationInterval user3 = new UserLocationInterval("mac3", "email2", loc, startTime, endTime);
        ArrayList<UserLocationInterval> ULIs = new ArrayList<>();
        ULIs.add(user1);
        ULIs.add(user2);
        ULIs.add(user3);

        user1 = new UserLocationInterval("mac4", "email4", loc, startTime, endTime);
        user2 = new UserLocationInterval("mac5", "email5", loc, startTime, endTime);
        ULIs.add(user1);
        ULIs.add(user2);

        ArrayList<LocationTimeSpent> LTSs = new ArrayList<>();
        LocationTimeSpent LTS1 = new LocationTimeSpent(loc, 3600, startTime, endTime);
        LTSs.add(LTS1);

        GroupLocations gl1 = new GroupLocations(ULIs, LTSs);
        result.add(gl1);

        return result;

    }

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
    public static boolean isSameGroup(GroupLocations group1, GroupLocations group2) {
        if (group1 == null || group2 == null) {
            return false;
        }
        ArrayList<UserLocationInterval> users1 = group1.getUsers();
        ArrayList<UserLocationInterval> users2 = group2.getUsers();
        if (users2.size() != users1.size()) {
            return false;
        }

        for (UserLocationInterval user : users1) {
            if (!users2.contains(user)) {
                return false;
            }
        }
        return true;
    }

    //forTopKNextPlace
    //Discover all groups located at that place during the specified date / time using the previous 15 minute time window.
    public static ArrayList<GroupLocations> getGroupsLocatedAt(String semanticPlace, String atTime) throws ParseException, NullPointerException, SQLException {
        ArrayList<GroupLocations> groupsFound = new ArrayList<>();

        String before15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", -15);
//        System.out.println("time : from " + before15Min + " to " + atTime);

        ArrayList<String> locationIds = getLocationId(semanticPlace);
//        for(String id: locationIds){
//            System.out.println(id);
//        }

        ArrayList<GroupLocations> groups = GroupsDAO.getGroups(atTime, before15Min);
//        groups = getFakeGroup1();//TODO test data

//        System.out.println("topNextAGD: " + groups.size());
        //check if the location visit is the last visit within the 15 mins window
        //wiki: If a user has more than one location update within this previous 15 minute window, consider the most recent update only.
        if (groups != null) {
            for (GroupLocations gf : groups) {
//                System.out.println("gf: " + gf);
                ArrayList<LocationTimeSpent> locations = gf.getLocations();
//                for(LocationTimeSpent loc:locations){
//                    System.out.println(loc);
//                }
                //get the last location group visit in the next 15 mins
                String lastTime = locations.get(0).getStart_time();
                LocationTimeSpent lastLocation = new LocationTimeSpent("", 0, "", "");
                for (LocationTimeSpent lts : locations) {
                    String endTime = lts.getEnd_time();
                    if (DateUtilityDAO.isAfter(endTime, lastTime)) {
                        lastLocation = lts;
                        lastTime = endTime;
                    }
                }
                //LocationTimeSpent location = locations.get(locations.size() - 1);
//                System.out.println("last location :" + lastTime + " " + lastLocation);
                String locId = lastLocation.getLocation();

                if (locationIds.contains(locId)) {
//                    System.out.println("GNP "+gf);
                    groupsFound.add(gf);

                }
            }
        }
        return groupsFound;
    }

    //for top K Next Place
    //to cater for:
//    {Faith, Ginny} was a group at a location 123, {Henry, Jake} was a group at another location 456. All 4 of them move to the location 789 next and spend 5 min there. i.e. “merged” into 1 group. For calculation of next location, should I consider location 789 as the original two groups’ next location? i.e. location 789 as next place has a group count of 2.
//Yes, consider as two groups (as in previous window).
    public static ArrayList<GroupLocations> splitGroup(ArrayList<GroupLocations> groupsInPrev15Min, ArrayList<GroupLocations> groupsAfter15Min) {
        if (groupsInPrev15Min == null || groupsAfter15Min == null) {
            return null;
        }
        ArrayList<GroupLocations> oGroups = new ArrayList<>(groupsInPrev15Min);
        ArrayList<GroupLocations> newGroups = new ArrayList<>();
        boolean split = false;
        for (GroupLocations newGroup : groupsAfter15Min) {
            ArrayList<UserLocationInterval> newUsers = newGroup.getUsers();
            for (GroupLocations oGroup : oGroups) {
                ArrayList<UserLocationInterval> oUsers = oGroup.getUsers();
                if (newUsers.containsAll(oUsers)) {
                    //if new group contains all members of the old group
                    //split the group and add to newGroups arraylist
                    split = true;
                    ArrayList<LocationTimeSpent> locations = newGroup.getLocations();
                    ArrayList<UserLocationInterval> group1 = new ArrayList<>();
                    ArrayList<UserLocationInterval> group2 = new ArrayList<>();

                    for (UserLocationInterval user : newUsers) {

                        if (oUsers.contains(user)) {
                            group1.add(user);
                        } else {
                            group2.add(user);
                        }
                    }
                    GroupLocations newGroup1 = new GroupLocations(group1, locations);
                    GroupLocations newGroup2 = new GroupLocations(group2, locations);
                    newGroups.add(newGroup1);
                    newGroups.add(newGroup2);
                    oGroups.remove(oGroup);//TODO note here, concurrent exception may happen
                }
            }
            if (!split) {
                newGroups.add(newGroup);
            }
        }
        return newGroups;
    }

    //3.Track each of these discovered users (i.e., located at the specified place)
//for the next  15  minutes to see if they visit another semantic place
//If a user visited multiple places within the next 15 minutes, consider the *last* one as the next visited place.
//edit: next visit place CAN be the same as the original place
//user visit time start counting from inputTime
    public static HashMap<GroupLocations, String> getLastVisitForGroups(ArrayList<GroupLocations> groupsInPrev15Min, String semanticPlace, String atTime) throws ParseException, SQLException, NullPointerException {
        HashMap<GroupLocations, String> nextPlaces = new HashMap<>(); //HashMap<group, lastVisitSemanticLocation>
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String after15Min = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 15);
//            System.out.println("time : from " + atTime + " to " +after15Min);
//            System.out.println("group in prev 15 min: "+groupsInPrev15Min.size());

            //all groups found in the later 15 min window
            ArrayList<GroupLocations> groupsAfter15Min = GroupsDAO.getGroups(after15Min, atTime);
//            groupsAfter15Min = getFakeGroup3(); //TODO testData
            groupsAfter15Min = splitGroup(groupsInPrev15Min, groupsAfter15Min);
//            AGDstatus agdStats = GroupsDAO.getUsersInLocation(atTime, after15Min);
//            ArrayList<GroupsFound> groupsAfter15Min = agdStats.getGroups();
//            System.out.println("after15MinGroupSize : "+groupsAfter15Min.size());
            //to find the location of same groups in the next 15 min
            for (GroupLocations group : groupsInPrev15Min) {
                for (GroupLocations anotherGroup : groupsAfter15Min) {
                    if (isSameGroup(group, anotherGroup)) {
//                        System.out.println(group.getUsers());
//                        System.out.println("before "+group);
//                        System.out.println("after  "+anotherGroup);
//                        System.out.println(group.getLocations());
                        //4. calculate the time spent
                        //To "visit a place", a user has to spend at least 5 minutes at that place.
                        //wiki :  5 minutes continuously at that place. 
                        //get the last location group visit in the next 15 mins
                        ArrayList<LocationTimeSpent> locations = anotherGroup.getLocations();
                        String lastTime = DateUtilityDAO.getEndDate(atTime, "yyyy-MM-dd HH:mm:ss", 0.0167);
                        LocationTimeSpent lastLocation = null;
                        for (LocationTimeSpent lts : locations) {
//                            System.out.println(lts);
                            String startTime = lts.getStart_time();
                            String endTime = lts.getEnd_time();
                            int timeDistanceInSecond = DateUtilityDAO.getTimeDistanceSecond(startTime, endTime);
                            if (timeDistanceInSecond > 300 && DateUtilityDAO.isAfter(endTime, lastTime)) {
//                                System.out.println("timespent " + timeDistanceInSecond);
                                lastLocation = lts;
                                lastTime = endTime;
                            }
                        }
                        if (lastLocation != null) {
                            String loc_id = lastLocation.getLocation();
                            String query = "SELECT semantic_name FROM " + LOCATION_LOOKUP_TABLE + " WHERE location_id=" + loc_id;
                            stmt = connection.prepareStatement(query);
                            rs = stmt.executeQuery();
                            if (rs.next()) {
                                String nextSemanticPlace = rs.getString("semantic_name");
//                                if (!nextSemanticPlace.equals(semanticPlace)) { //only consider the nextPlace if it not the same as previous place
                                nextPlaces.put(group, nextSemanticPlace);
//                                }
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

    //for Group Top K Next Place Json
    public static ArrayList<GroupPopularPlace> convertGroupNextPlaceResult(HashMap<Integer, ArrayList<TopKNextPlaceResult>> input) {
        ArrayList<GroupPopularPlace> groupPopularPlaces = new ArrayList();
        if (input == null || input.isEmpty()) {
            return groupPopularPlaces;
        }

        Iterator it = input.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            int rank = (int) pairs.getKey();
            ArrayList<TopKNextPlaceResult> tknpResults = (ArrayList<TopKNextPlaceResult>) pairs.getValue();
            for (TopKNextPlaceResult item : tknpResults) {
                String semanticPlace = item.getSemanticPlace();
                int count = item.getCount();
                GroupPopularPlace nextPlace = new GroupPopularPlace(rank, semanticPlace, count);
                groupPopularPlaces.add(nextPlace);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        Collections.sort(groupPopularPlaces);
        return groupPopularPlaces;
    }

    //for Group Top K Place
    public static ArrayList<GroupPopularPlace> retrieveGroupTopKPopularPlaces(ArrayList<GroupLocations> groupList, int k) {

        ArrayList<GroupPopularPlace> groupPopularPlaces = new ArrayList<>();

        // Store how many groups went to each location id
        HashMap<String, Integer> locationID_Map = new HashMap<>();

        // Store how many groups went to each semantic place
        HashMap<String, Integer> semanticPlace_Map = new HashMap<>();

        for (int i = 0; i < groupList.size(); i++) {
            GroupLocations g = groupList.get(i);
            ArrayList<LocationTimeSpent> locationList = g.getLocations();

            // Stores which location at what time
            String locationID = "";
            String locationTime = "";

            for (int j = 0; j < locationList.size(); j++) {
                if (j == 0) {
                    locationID = locationList.get(j).getLocation();
                    locationTime = locationList.get(j).getStart_time();
                    continue;
                }

                String nextLocationID = locationList.get(j).getLocation();
                String nextLocationTime = locationList.get(j).getStart_time();

                try {
                    if (DateUtilityDAO.isAfter(nextLocationTime, locationTime)) {
                        locationID = nextLocationID;
                        locationTime = nextLocationTime;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (locationID_Map.get(locationID) == null) {
                locationID_Map.put(locationID, 1);
            } else {
                int count = locationID_Map.get(locationID);
                locationID_Map.put(locationID, ++count);
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

        ArrayList<GroupPopularPlace> tempListWithoutRank = new ArrayList<>();

        Iterator semanticEntries = semanticPlace_Map.entrySet().iterator();

        while (semanticEntries.hasNext()) {
            Map.Entry entry = (Map.Entry) semanticEntries.next();
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
