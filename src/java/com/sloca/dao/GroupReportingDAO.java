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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author alice
 */
public class GroupReportingDAO {

    //for Top K Next Place Json
    /**
     * This method takes in the list of top next places that students visit and
     * ranks them accordingly. Semantic places with the same rank will be sorted
     * lexicographically
     *
     * @param input containing hasMap with rank as key and arraylist of semantic
     * places
     * @return the list of groups ranked accordingly to their popularity output
     * suitable for json output
     */
    public static ArrayList<GroupPopularPlace> convertGroupNextPlaceResult(HashMap<Integer, ArrayList<TopKNextPlaceResult>> input) {
        //for Top K Next Place Json
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

    /**
     * This method takes in the list of places that students visit and ranks
     * them accordingly. Semantic places with the same rank will be sorted
     * lexicographically. Only k number of rank will be returned
     *
     * @param groupList list of groups and their respective location
     * @param k number of rank to be returned
     * @return the list of groups ranked accordingly to their popularity output
     * suitable for json output
     */
        public static ArrayList<GroupPopularPlace> retrieveGroupTopKPopularPlaces(ArrayList<GroupLocations> groupList, int k) {
                
        ArrayList<GroupPopularPlace> groupPopularPlaces = new ArrayList<>();

        // Store how many groups went to each location id
        HashMap<String, Integer> locationID_Map = new HashMap<>();
        
        // Store each locationID consist of how many people
        HashMap<String, Integer> locIDUserCount_Map = new HashMap<>();

        // Store how many groups went to each semantic place
        HashMap<String, Integer> semanticPlace_Map = new HashMap<>();
        
        // Store each semantic place consist of how many people
        HashMap<String, Integer> semanticPlaceUserCount_Map = new HashMap<>();

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
                locIDUserCount_Map.put(locationID, g.getSize());
            } else {
                int count = locationID_Map.get(locationID);
                locationID_Map.put(locationID, ++count);
                locIDUserCount_Map.put(locationID, locIDUserCount_Map.get(locationID) + g.getSize());
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
                    semanticPlaceUserCount_Map.put(semanticName, locIDUserCount_Map.get(locID));
                } else {
                    int currCount = semanticPlace_Map.get(semanticName);
                    int newCount = currCount + count;
                    semanticPlace_Map.put(semanticName, newCount);
                    
                    int newUserCount = semanticPlaceUserCount_Map.get(semanticName) + locIDUserCount_Map.get(locID);
                    semanticPlaceUserCount_Map.put(semanticName, newUserCount);
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
            Integer userCount = semanticPlaceUserCount_Map.get(semanticPlace);
            
            tempListWithoutRank.add(new GroupPopularPlace(semanticPlace, count, userCount));
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
