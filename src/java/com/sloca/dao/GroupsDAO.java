/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.AGDstatus;
import com.sloca.model.GroupLocations;
import com.sloca.model.GroupsFound;
import com.sloca.model.LocationTimeSpent;
import com.sloca.model.Members;
import com.sloca.model.UserLocationInterval;
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
import org.joda.time.Interval;

/**
 * This class handles the processing of groups in locations
 *
 * @author alice
 */
public class GroupsDAO {

    /**
     * Returns a number of distinct mac address(es) found in location database
     * within a given time range.
     *
     * @param startDate the start datetime of a range
     * @param endDate the end datetime of a range
     * @return a number of mac address(es) found, inclusive of startDate,
     * exclusive of endDate
     */
    public static int getTotalNumOfUsers(String startDate, String endDate) {
        int total_users = 0;
        String query = "SELECT DISTINCT student_mac_address FROM location WHERE time > ? AND time <= ?";

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, endDate);
            stmt.setString(2, startDate);

            rs = stmt.executeQuery();

            while (rs.next()) {
                total_users++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt, rs);
        }
        return total_users;
    }
    // get all users in each location

    /**
     * This method takes in a date time range (start and end date) and returns
     * the list of groups within the specified time range in a format suitable
     * for json output
     *
     * @param startDate the start datetime of the time range
     * @param endDate the end datetime of the time range
     * @return an object contains the list of groups within the specified time
     * range, inclusive of startDate, exclusive of endDate
     * @see AGDstatus
     */
    public static AGDstatus getUsersInLocation(String startDate, String endDate) {

        AGDstatus agd_status = new AGDstatus();
        // obtain groups found 
        ArrayList<GroupLocations> group = getGroups(startDate, endDate);
        // package for json output            
        int total_users = 0;
        // new array list with proper structure
        ArrayList<GroupsFound> groups_found = new ArrayList<>();

        // iterate and package
        for (GroupLocations user_groups : group) {

            // create new members list
            ArrayList<Members> members_list = new ArrayList<Members>();

            // iterate and collate all members
            ArrayList<UserLocationInterval> old_members_list = user_groups.getUsers();
            for (UserLocationInterval user : old_members_list) {
                String email = "";
                if (user.getEmail() != null) {
                    email = user.getEmail();
                }
                // populate new members list 
                Members m = new Members(email, user.getMac_address());
                members_list.add(m);
            }

            // populate new group structure
            GroupsFound new_group = new GroupsFound(members_list, user_groups.getLocations());
            groups_found.add(new_group);
        }

        total_users = getTotalNumOfUsers(startDate, endDate);

        // json outer 'cover' - create new AGDstatus object
        // set params
        agd_status.setGroups(groups_found);
        agd_status.setTotal_group(groups_found.size());
        agd_status.setStatus("success");
        agd_status.setTotal_users(total_users);

        // return result
        return agd_status;
    }

    // overloading method to returns a group location object
    /**
     * This method takes in a date time range (start and end date) and returns
     * the list of groups within the specified time range in a format suitable
     * for further internal processing
     *
     * @param startDate the start datetime of the time range
     * @param endDate the end datetime of the time range
     * @return the list of groups within the specified time range, inclusive of
     * startDate, exclusive of endDate
     * @see GroupLocations
     */
    public static ArrayList<GroupLocations> getGroups(String startDate, String endDate) {

        // dbconn
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        // container to store users in a particular location - use hashmap?
        //ArrayList<UsersInLocation> usersInLocations = new ArrayList<UsersInLocation>();
        ArrayList<GroupLocations> results = new ArrayList<GroupLocations>();

        // db query
        String query = "SELECT l.student_mac_address AS mac_address, l.time AS time, l.location_id AS location, s.email AS email "
                + "FROM location l LEFT OUTER JOIN student s ON l.student_mac_address = s.mac_address "
                + "WHERE l.time <= ? AND l.time > ? "
                + "ORDER BY l.student_mac_address, l.time";

        // execute
        try {
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);

            // set params
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);

            // send
            rs = stmt.executeQuery();

            // create arraylist of UserLocationInterval objects to store results
            ArrayList<UserLocationInterval> user_list = new ArrayList<UserLocationInterval>();

            // obtain and process results
            while (rs.next()) {
                String datetime = rs.getString("time");
                // substring because of some weird bug that adds .0 behind the results
                String new_datetime = datetime.substring(0, 19);
                // creation of UserLocationInterval object and store it into list

                // add 9 minutes to obtain end time by invoking utility method
                String endTime = "";
                try {
                    endTime = (Utility.getEndDate(datetime, "yyyy-MM-dd HH:mm:ss", 9));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (DateUtilityDAO.isAfter(endTime, startDate)) {
                    endTime = startDate;
                }

                UserLocationInterval user = new UserLocationInterval(rs.getString("mac_address"), rs.getString("email"), rs.getString("location"), new_datetime, endTime);

                // add user to list
                user_list.add(user);
            }

            // process list
            ArrayList<UserLocationInterval> processed_list = processList(user_list);
            // break for debugging

            // create hashmap and arraylist
            HashMap<String, GroupLocations> group_locations = new HashMap<>();
            ArrayList<GroupLocations> group_locations_list = new ArrayList<>();

            // see what is in processed list
            for (int i = 0; i < processed_list.size(); i++) {
                UserLocationInterval ul = processed_list.get(i);
            }

            // iterate through
            for (int i = 0; i < processed_list.size(); i++) {

                // reset the arrayList
                ArrayList<UserLocationInterval> users = new ArrayList<>();
                ArrayList<LocationTimeSpent> loc_time = new ArrayList<>();

                // obtain location_id and time interval 
                UserLocationInterval loc_time_obj = processed_list.get(i);

                String location = loc_time_obj.getLocation_id();
                String interval_str = loc_time_obj.getInterval().toString();

                // form hashmap key
                String key = location + interval_str;

                // check for existence (inexistent)               
                if (group_locations.get(key) == null) {

                    // add user to list
                    users.add(loc_time_obj);
                    // calculate time duration in seconds
                    long duration = loc_time_obj.getInterval().toDurationMillis() / 1000;
                    // add location time to list
                    loc_time.add(new LocationTimeSpent(loc_time_obj.getLocation_id(), duration, loc_time_obj.getStart_time(), loc_time_obj.getEnd_time()));
                    // create new group
                    GroupLocations new_group = new GroupLocations(users, loc_time);

                    // place in hashmap and arraylist
                    group_locations.put(key, new_group);
                    group_locations_list.add(new_group);
                } else {
                    // add the additional user to this location interval
                    GroupLocations gl = group_locations.get(key);
                    ArrayList<UserLocationInterval> ult = gl.getUsers();
                    ult.add(loc_time_obj);
                    Collections.sort(ult);
                }
            }

            for (int i = 0; i < group_locations_list.size(); i++) {
                GroupLocations gl = group_locations_list.get(i);

                ArrayList<UserLocationInterval> users = gl.getUsers();
                ArrayList<LocationTimeSpent> locations = gl.getLocations();

                long totalTimeSpent = gl.getTotalTimeSpent();
                int groupSize = gl.getSize();

                for (int j = 0; j < users.size(); j++) {
                    String macAdd = users.get(j).getMac_address();
                }

                for (int j = 0; j < locations.size(); j++) {
                    LocationTimeSpent lts = locations.get(j);
                }

            }

            // 'clone' group_locations_list
            ArrayList<GroupLocations> cloned_list = new ArrayList<>();
            for (GroupLocations gl : group_locations_list) {
                cloned_list.add(gl);

            }

            // iterate through the previously created list
            for (int x = 0; x < group_locations_list.size(); x++) {

                // arraylist to store time spent objects for this particular user
                ArrayList<LocationTimeSpent> loc_time = new ArrayList<>();

                for (int z = (x + 1); z < group_locations_list.size(); z++) {
                    if (x == z) {
                        continue;
                    } else {

                        // get first user inside the first and second group
                        UserLocationInterval user1 = group_locations_list.get(x).getUsers().get(0);
                        UserLocationInterval user2 = group_locations_list.get(z).getUsers().get(0);

                        // check for overlap & obtain overlap interval
                        Interval overlap = user1.getInterval().overlap(user2.getInterval());

                        // if overlap and same location id
                        if (overlap != null && user1.getLocation_id().equals(user2.getLocation_id())) {

                            String key = "";
                            key += user1.getLocation_id();
                            key += overlap.toString();

                            if (!group_locations.containsKey(key)) {

                                // form hashmap key
                                // get unique from both list
                                ArrayList<UserLocationInterval> group1 = group_locations_list.get(x).getUsers();
                                ArrayList<UserLocationInterval> group2 = group_locations_list.get(z).getUsers();

                                Collections.sort(group1);
                                Collections.sort(group2);

                                ArrayList<UserLocationInterval> new_group = new ArrayList<>();

                                for (UserLocationInterval ulc : group1) {
                                    if (!new_group.contains(ulc)) {
                                        new_group.add(ulc);
                                    }
                                }

                                for (UserLocationInterval ulc : group2) {
                                    if (!new_group.contains(ulc)) {
                                        new_group.add(ulc);
                                    }
                                }

                                Collections.sort(new_group);

                                long timeSpent = overlap.toDurationMillis() / 1000;

                                String overlapStart = overlap.getStart().toString();
                                String overlapEnd = overlap.getEnd().toString();

                                overlapStart = overlapStart.replace('T', ' ');
                                overlapEnd = overlapEnd.replace('T', ' ');

                                overlapStart = overlapStart.substring(0, 19);
                                overlapEnd = overlapEnd.substring(0, 19);

                                // create time object
                                loc_time.add(new LocationTimeSpent(user1.getLocation_id(), timeSpent, overlapStart, overlapEnd));
                                //loc_time.add(new LocationTimeSpent(user1.getLocation_id(), timeSpent, overlap.getStart().toString(), overlap.getEnd().toString()));

                                // if does not contain key, create new group with the overlap interval time with members of both groups inside it
                                GroupLocations new_grouploc = new GroupLocations(new_group, loc_time);

                                // add to hashmap and new list
                                group_locations.put(key, new_grouploc);
                                cloned_list.add(new_grouploc);
                            } else {
                                GroupLocations gl = group_locations.get(key);

                                ArrayList<UserLocationInterval> ul_forNewGroup = new ArrayList<>();
                                ArrayList<LocationTimeSpent> lts_forNewGroup = new ArrayList<>();

                                ArrayList<UserLocationInterval> ul = gl.getUsers();
                                ArrayList<LocationTimeSpent> lts = gl.getLocations();

                                for (LocationTimeSpent loc : lts) {
                                    lts_forNewGroup.add(loc);
                                }

                                for (UserLocationInterval user : ul) {
                                    ul_forNewGroup.add(user);
                                }

                                ArrayList<UserLocationInterval> group1 = group_locations_list.get(x).getUsers();
                                ArrayList<UserLocationInterval> group2 = group_locations_list.get(z).getUsers();

                                for (UserLocationInterval ulc : group1) {
                                    if (!ul_forNewGroup.contains(ulc)) {
                                        ul_forNewGroup.add(ulc);
                                    }
                                }

                                for (UserLocationInterval ulc : group2) {
                                    if (!ul_forNewGroup.contains(ulc)) {
                                        ul_forNewGroup.add(ulc);
                                    }
                                }

                                cloned_list.add(new GroupLocations(ul_forNewGroup, lts_forNewGroup));
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < cloned_list.size(); i++) {
                GroupLocations gl = cloned_list.get(i);

                ArrayList<UserLocationInterval> users = gl.getUsers();
                ArrayList<LocationTimeSpent> locations = gl.getLocations();

                long totalTimeSpent = gl.getTotalTimeSpent();
                int groupSize = gl.getSize();

                for (int j = 0; j < users.size(); j++) {
                    String macAdd = users.get(j).getMac_address();
                }

                for (int j = 0; j < locations.size(); j++) {
                    LocationTimeSpent lts = locations.get(j);
                }

            }

            // add location from supergroup to subgroup
            // let group 1 be supergroup 
            addLocationsToSubgroup(cloned_list);

            // after adding locations to subgroup
            for (int i = 0; i < cloned_list.size(); i++) {
                GroupLocations gl = cloned_list.get(i);

                ArrayList<UserLocationInterval> users = gl.getUsers();
                ArrayList<LocationTimeSpent> locations = gl.getLocations();

                long totalTimeSpent = gl.getTotalTimeSpent();
                int groupSize = gl.getSize();

                for (int j = 0; j < users.size(); j++) {
                    String macAdd = users.get(j).getMac_address();
                }

                for (int j = 0; j < locations.size(); j++) {
                    LocationTimeSpent lts = locations.get(j);
                }

            }

            // collapsing of groups
            // initialise hashmap 2 this is to add everyone with the same place and time together
            HashMap<String, GroupLocations> groups = new HashMap<>();
            // iterate through new list
            for (int i = 0; i < cloned_list.size(); i++) {

                // obtain member's names
                ArrayList<UserLocationInterval> members = cloned_list.get(i).getUsers();
                Collections.sort(members);
                // key for hashmap
                String key = "";

                // iterate through users list to obtain concatenated list of user mac addresses for key for hashmap
                for (UserLocationInterval user : members) {
                    key += user.getMac_address();
                }

                // check if appear in groups hashmap
                if (!groups.containsKey(key)) {

                    // put object in hashmap
                    groups.put(key, cloned_list.get(i));

                } else {

                    // retrieve the group out from hashmap and add group locations inside
                    GroupLocations groups_in_obj = groups.get(key);
                    groups_in_obj.addLocations(cloned_list.get(i).getLocations());

                }
            } // end loop            

            // iterate through hashmap to filter 80% requirement duration together
            Iterator it = groups.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                GroupLocations gl = (GroupLocations) pairs.getValue();

                // check for 80% time together
                // wait. check for results first.
                // get time spent
                long timeSpent = gl.getTotalTimeSpent();
                gl.tabulateUsers();
                gl.tabulateLocations();
                if (timeSpent >= 720 && gl.getSize() > 1) {
                    // add results to results array
                    results.add(gl);
                }
            }

            // remove all subgroups 
            removeSubGroups(results);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection, stmt);
            // return     
            return results;
        }
    }

    // method to process list
    /**
     * This method takes in a raw unprocessed records and combine The recordS if
     * they overlap each others (i.e.: is userA stays in locationA from 12:01 to
     * 12:03 and locationA from 12:03 to 12:05, these records will be combined
     * to one where userA stays in location A from 12:01 to 12:05)
     *
     * @param unsorted_list contains all unprocessed records retrieved from
     * database
     * @return returns the processed user list whereby each user is tagged to a
     * location for a specific time period
     * @see UserLocationInterval
     */
    public static ArrayList<UserLocationInterval> processList(ArrayList<UserLocationInterval> unsorted_list) {

        // container
        ArrayList<UserLocationInterval> new_list = new ArrayList<UserLocationInterval>();

        // iterate through and check for if overlap. if overlap and same mac_add and location, change the end_time of the user object
        outer:
        for (int i = 0; i < unsorted_list.size(); i++) {
            // first object
            UserLocationInterval unsorted_obj = unsorted_list.get(i);

            // get attributes
            String mac_address = unsorted_obj.getMac_address();
            String location = unsorted_obj.getLocation_id();
            String email = unsorted_obj.getEmail();
            String start_time = unsorted_obj.getStart_time();
            String end_time = unsorted_obj.getEnd_time();

            for (int j = 0; j < new_list.size(); j++) {
                // object in new list
                UserLocationInterval new_obj = new_list.get(j);
                // check for same user
                if (unsorted_obj.getMac_address().equals(new_obj.getMac_address())) {

                    // if same user, check for timing overlap
                    Interval overlap = unsorted_obj.getInterval().overlap(new_obj.getInterval());

                    if (overlap != null) {

                        // check if same location
                        if (unsorted_obj.getLocation_id().equals(new_obj.getLocation_id())) {

                            new_obj.setEnd_time(unsorted_obj.getEnd_time());

                            continue outer;
                        } else {

                            // else if different location
                            // 50 - 59 (location b) modify previous location end_time as well to the start time of unsorted.
                            new_obj.setEnd_time(unsorted_obj.getStart_time());
                            // create another object with another location with the start time 50 - 59
                            new_list.add(new UserLocationInterval(unsorted_obj.getMac_address(), unsorted_obj.getEmail(), unsorted_obj.getLocation_id(), unsorted_obj.getStart_time(), unsorted_obj.getEnd_time()));

                            continue outer;
                        }
                    }
                }
            }
            // add to new list
            new_list.add(new UserLocationInterval(mac_address, email, location, start_time, end_time));
        }
        return new_list;
    }

    /**
     * Add locations to the groups that is identified as subgroups.
     * <p>
     * This method is used internally to process group list
     *
     * @param cloned_list raw list of unprocessed group list
     * @return the list of groups with updated locations
     * @see GroupLocations
     */
    public static ArrayList<GroupLocations> addLocationsToSubgroup(ArrayList<GroupLocations> cloned_list) {
        for (int i = 0; i < cloned_list.size(); i++) {
            for (int j = 0; j < cloned_list.size(); j++) {
                if (i == j) {
                    continue;
                } else {
                    GroupLocations group1 = cloned_list.get(i);
                    GroupLocations group2 = cloned_list.get(j);

                    ArrayList<UserLocationInterval> group1_Users = group1.getUsers();
                    ArrayList<LocationTimeSpent> group1_Locations = group1.getLocations();
                    LocationTimeSpent group1_locationToCompare = group1_Locations.get(0);

                    ArrayList<UserLocationInterval> group2_Users = group2.getUsers();
                    ArrayList<LocationTimeSpent> group2_Locations = group2.getLocations();
                    LocationTimeSpent group2_locationToCompare = group2_Locations.get(0);
                    String group2_startTime = group2_locationToCompare.getStart_time();

                    if (group2_Users.size() > group1_Users.size()) {
                        continue;
                    } else {

                        boolean isSubGroup = false;
                        int superGroupCount = 0;

                        for (int k = 0; k < group2_Users.size(); k++) {

                            UserLocationInterval subUser = group2_Users.get(k);
                            String subMac = subUser.getMac_address();

                            for (int l = 0; l < group1_Users.size(); l++) {

                                UserLocationInterval superUser = group1_Users.get(l);
                                String superMac = superUser.getMac_address();

                                if (subMac.equals(superMac)) {
                                    superGroupCount++;
                                }
                            }
                        }

                        if (superGroupCount == group2_Users.size()) {
                            isSubGroup = true;
                        }

                        if (isSubGroup) {
                            boolean addLocation = true;

                            for (int k = 0; k < group2_Locations.size(); k++) {
                                String g1_locID = group1_locationToCompare.getLocation();
                                String g2_locID = group2_Locations.get(k).getLocation();

                                if (g1_locID.equals(g2_locID)) {
                                    if (group1_locationToCompare.getInterval().overlap(group2_Locations.get(k).getInterval()) != null) {
                                        addLocation = false;
                                    }
                                }
                            }
                            if (addLocation) {

                                String loc = group1_locationToCompare.getLocation();
                                long timespent = group1_locationToCompare.getTime_spent();
                                String startTime = group1_locationToCompare.getStart_time();
                                String endTime = group1_locationToCompare.getEnd_time();

                                group2_Locations.add(new LocationTimeSpent(loc, timespent, startTime, endTime));
                            }
                        }
                    }
                }
            }
        }
        return cloned_list;
    }

    /**
     * Takes in a list of groups and locations, and remove duplicate group(s)
     * <p>
     * This method is used internally to process group list
     *
     * @param cloned_list raw list of unprocessed group list
     * @return updated list of groups and locations with sub groups removed
     * @see GroupLocations
     *
     */
    public static ArrayList<GroupLocations> removeSubGroups(ArrayList<GroupLocations> cloned_list) {
        for (int i = 0; i < cloned_list.size(); i++) {
            for (int j = 0; j < cloned_list.size(); j++) {
                if (i == j) {
                    continue;
                } else {
                    GroupLocations group1 = cloned_list.get(i);
                    GroupLocations group2 = cloned_list.get(j);

                    ArrayList<UserLocationInterval> group1_Users = group1.getUsers();
                    ArrayList<LocationTimeSpent> group1_Locations = group1.getLocations();
                    LocationTimeSpent group1_locationToCompare = group1_Locations.get(0);
                    String group1_startTime = group1_locationToCompare.getStart_time();

                    ArrayList<UserLocationInterval> group2_Users = group2.getUsers();
                    ArrayList<LocationTimeSpent> group2_Locations = group2.getLocations();
                    LocationTimeSpent group2_locationToCompare = group2_Locations.get(0);
                    String group2_startTime = group2_locationToCompare.getStart_time();

                    if (group2_Users.size() > group1_Users.size()) {
                        continue;
                    } else if (!group2_startTime.equals(group1_startTime)) {
                        continue;
                    } else {

                        boolean userIsSubGroup = false;
                        int superGroupUserCount = 0;

                        for (int k = 0; k < group2_Users.size(); k++) {

                            UserLocationInterval subUser = group2_Users.get(k);
                            String subMac = subUser.getMac_address();

                            for (int l = 0; l < group1_Users.size(); l++) {

                                UserLocationInterval superUser = group1_Users.get(l);
                                String superMac = superUser.getMac_address();

                                if (subMac.equals(superMac)) {
                                    superGroupUserCount++;
                                }
                            }
                        }

                        if (superGroupUserCount == group2_Users.size()) {
                            userIsSubGroup = true;
                        }

                        boolean locationIsSubGroup = false;
                        int superGroupLocCount = 0;

                        for (int k = 0; k < group2_Locations.size(); k++) {

                            LocationTimeSpent subLoc = group2_Locations.get(k);
                            String subLocID = subLoc.getLocation();

                            for (int l = 0; l < group1_Locations.size(); l++) {

                                LocationTimeSpent supLoc = group1_Locations.get(l);
                                String supLocID = supLoc.getLocation();

                                if (subLocID.equals(supLocID)) {
                                    superGroupLocCount++;
                                }
                            }
                        }

                        if (superGroupLocCount == group2_Locations.size()) {
                            locationIsSubGroup = true;
                        }

                        if (userIsSubGroup && locationIsSubGroup) {
                            cloned_list.remove(j);
                            i--;
                            j--;
                        }
                    }
                }
            }
        }
        return cloned_list;
    }
}
