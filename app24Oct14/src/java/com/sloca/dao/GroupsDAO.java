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
import com.sloca.model.LocationInterval;
import com.sloca.model.LocationTime;
import com.sloca.model.LocationTimeSpent;
import com.sloca.model.Members;
import com.sloca.model.User;
import com.sloca.model.UserLocIntervals;
import com.sloca.model.UserLocationInterval;
import com.sloca.model.UsersInLocation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.joda.time.Interval;

/**
 *
 * @author alice
 */
public class GroupsDAO {
    
    private static String user_query_time;
    
    // get all users in each location
    public static AGDstatus getUsersInLocation(String startDate, String endDate)  {
        
        // set user query end time to be used in processing AGD
        user_query_time = startDate;
        
        // dbconn
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        // container to store users in a particular location - use hashmap?
        //ArrayList<UsersInLocation> usersInLocations = new ArrayList<UsersInLocation>();
        ArrayList<GroupLocations> results = new ArrayList<GroupLocations>();
        AGDstatus agd_status = new AGDstatus();
        // db query
        String query = "SELECT l.student_mac_address AS mac_address, l.time AS time, l.location_id AS location, s.email AS email FROM location l LEFT OUTER JOIN student s ON l.student_mac_address = s.student_mac_address "
                + "WHERE l.time <= ? AND l.time > ? ORDER BY l.student_mac_address, l.time";
        
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
            while(rs.next()) {
                String datetime = rs.getString("time");
                // substring because of some weird bug that adds .0 behind the results
                String new_datetime = datetime.substring(0, 19);
                // creation of UserLocationInterval object and store it into list
                UserLocationInterval user = new UserLocationInterval(rs.getString("mac_address"), rs.getString("email"), rs.getString("location"), new_datetime);
                
                // add user to list
                user_list.add(user);
            }
            
            // process list
            ArrayList<UserLocationInterval> processed_list = processList(user_list);
            
            System.out.println("Processed List Size: " + processed_list.size());
            // break for debugging
            
            // create hashmap and arraylist
            HashMap<String, GroupLocations> group_locations = new HashMap<>();
            ArrayList<GroupLocations> group_locations_list = new ArrayList<>();
            
            // iterate through
            for(int i = 0; i < processed_list.size(); i++)   {
                
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
                if(!group_locations.containsKey(key))    {
                    
                    // add user to list
                    users.add(loc_time_obj);
                    // calculate time duration in seconds
                    long duration = loc_time_obj.getInterval().toDurationMillis() / 1000;
                    // add location time to list
                    loc_time.add(new LocationTimeSpent(loc_time_obj.getLocation_id(), duration));
                    // create new group
                    GroupLocations new_group = new GroupLocations(users, loc_time);
                    
                    // place in hashmap and arraylist
                    group_locations.put(key, new_group);
                    group_locations_list.add(new_group);
                }
            }
            
            System.out.println(group_locations_list.size());
            
            // 'clone' group_locations_list
            ArrayList<GroupLocations> cloned_list = new ArrayList<>();
            for(GroupLocations gl : group_locations_list)   {
                cloned_list.add(gl);
            }
            
            // iterate through the previously created list
            for(int x = 0; x < group_locations_list.size(); x++)   {
                
                // arraylist to store time spent objects for this particular user
                ArrayList<LocationTimeSpent> loc_time = new ArrayList<>();
                
                for(int z = 0; z < group_locations_list.size(); z++)   {
                    if(x == z)    {
                        continue;
                    }
                    else    {
                                             
                        // get first user inside the first and second group
                        UserLocationInterval user1 = group_locations_list.get(x).getUsers().get(0);
                        UserLocationInterval user2 = group_locations_list.get(z).getUsers().get(0);
                        //System.out.println("user size " + group_locations_list.get(x).getUsers().size());
//                        System.out.println(user1.getMac_address() + " " + user1.getLocation_id());
//                        System.out.println(user2.getMac_address() + " " + user2.getLocation_id());
                        
                        // check for overlap & obtain overlap interval
                        Interval overlap = user1.getInterval().overlap(user2.getInterval());
                        
                        // if overlap and same location id
                        if(overlap != null && user1.getLocation_id().equals(user2.getLocation_id()))    {
                            
                            // form hashmap key
                            // get unique from both list
                            ArrayList<UserLocationInterval> group1 = group_locations_list.get(x).getUsers();
                            ArrayList<UserLocationInterval> group2 = group_locations_list.get(z).getUsers();
                            for(UserLocationInterval ulc : group2)   {
                                if(!group1.contains(ulc))    {
                                    group1.add(ulc);
                                }
                            }
                            
                            // form key
                            String key = "";
                            for(UserLocationInterval user : group1)   {
                                key += user.getMac_address();
                            }
                            
                            key += user1.getInterval().toString();
                            // problem here. it doesn't go in - everyone is existent.
                            //System.out.println(key);

                            // check for existence in hashmap
                            if(!group_locations.containsKey(key))    {
                                // debug: did not come in here
                                

                                // tabulate users in both lists
                                ArrayList<UserLocationInterval> list1 = group_locations_list.get(x).getUsers();
                                ArrayList<UserLocationInterval> list2 = group_locations_list.get(z).getUsers();

                                // merge lists without duplicates
                                for(UserLocationInterval user : list2)   {
                                    if(!list1.contains(user))    {
                                        list1.add(user);
                                    }
                                    //System.out.println(user);
                                }                            

                                // obtain time duration at a location
                                long timeSpent = user1.getInterval().toDurationMillis() / 1000;

                                // create time object
                                loc_time.add(new LocationTimeSpent(user1.getLocation_id(), timeSpent));

                                // if does not contain key, create new group with the overlap interval time with members of both groups inside it
                                GroupLocations new_group = new GroupLocations(list1, loc_time);                            

                                // add to hashmap and new list
                                group_locations.put(key, new_group);
                                cloned_list.add(new_group);
                            }
                            else    {
                                continue;
                            }
                        }
                    }
                }
            } // end of outer loop
            
            // collapsing of groups
            
            // initialise hashmap 2 this is to add everyone with the same place and time together
            HashMap<String, GroupLocations> groups = new HashMap<>();
            
            // iterate through new list
            for(int i = 0; i < cloned_list.size(); i++)   {
                
                // obtain member's names
                ArrayList<UserLocationInterval> members = cloned_list.get(i).getUsers();
                
                // key for hashmap
                String key = "";
                
                // iterate through users list to obtain concatenated list of user mac addresses for key for hashmap
                for(UserLocationInterval user : members)   {
                    key += user.getMac_address();
                }
                
                // check if appear in groups hashmap
                if(!groups.containsKey(key))    {
                    
                    // put object in hashmap
                    groups.put(key, cloned_list.get(i));
                    
                }
                else    {
                    
                    // retrieve the group out from hashmap and add group locations inside
                    GroupLocations groups_in_obj = groups.get(key);
                    groups_in_obj.addLocations(cloned_list.get(i).getLocations());
                    
                }
            } // end loop            
            
            // iterate through hashmap to filter 80% requirement duration together
            Iterator it = groups.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                GroupLocations gl = (GroupLocations)pairs.getValue();
                
                // check for 80% time together
                // wait. check for results first.
                // get time spent
                long timeSpent = gl.getTotalTimeSpent();
                if(timeSpent > 720)    {
                    // add results to results array
                    results.add(gl);
                }
            }
            
            System.out.println("results size " + results.size());
            
            // package for json output            
            int total_users = 0;
            // new array list with proper structure
            ArrayList<GroupsFound> groups_found = new ArrayList<GroupsFound>();
            
            // iterate and package
            for(GroupLocations user_groups : results)   {
                
                // create new members list
                ArrayList<Members> members_list = new ArrayList<Members>();
                
                // iterate and collate all members
                ArrayList<UserLocationInterval> old_members_list = user_groups.getUsers();
                for(UserLocationInterval user : old_members_list)   {
                    
                    // populate new members list 
                    Members m = new Members(user.getEmail(), user.getMac_address());
                    members_list.add(m);
                }
                total_users += members_list.size();
                // populate new group structure
                GroupsFound new_group = new GroupsFound(members_list, user_groups.getLocations());
                groups_found.add(new_group);
            }
            
            // json outer 'cover' - create new AGDstatus object
            // set params
            agd_status.setGroups(groups_found);
            agd_status.setTotal_group(groups_found.size());
            agd_status.setStatus("success");
            agd_status.setTotal_users(total_users);
            
            // return result
            
        } catch (SQLException e)    {
            e.printStackTrace();
        } finally   {
            ConnectionFactory.close(connection, stmt);
            // return
            return agd_status;
        }
    }
    
    // method to process list
    public static ArrayList<UserLocationInterval> processList(ArrayList<UserLocationInterval> unsorted_list) {
        
        // container
        ArrayList<UserLocationInterval> new_list = new ArrayList<UserLocationInterval>();
        
        // iterate through and check for if overlap. if overlap and same mac_add and location, change the end_time of the user object
        outer:
        for(int i = 0; i < unsorted_list.size(); i++)   {
            // first object
            UserLocationInterval unsorted_obj = unsorted_list.get(i);
            
            // get attributes
            String mac_address = unsorted_obj.getMac_address();
            String location = unsorted_obj.getLocation_id();
            String email = unsorted_obj.getEmail();
            String start_time = unsorted_obj.getStart_time();

            for(int j = 0; j < new_list.size(); j++)   {               
                // object in new list
                UserLocationInterval new_obj = new_list.get(j);                
                // check for same user
                if(unsorted_obj.getMac_address().equals(new_obj.getMac_address()))    {
                    
                    // if same user, check for timing overlap
                    Interval overlap = unsorted_obj.getInterval().overlap(new_obj.getInterval());   
                    
                    if(overlap != null)    {   
                        
                        // check if same location
                        if(unsorted_obj.getLocation_id().equals(new_obj.getLocation_id()))    {
                            
                            // check if unsorted_obj's end time is more than query window time, if so, use query window time instead
                            // obtain both date objects (convert to java date)
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                
                                Date query_window = sdf.parse(user_query_time);
                                Date unsorted_obj_et = sdf.parse(unsorted_obj.getEnd_time());
                                
                                // use calendar to check which is later
                                Calendar q_window = Calendar.getInstance();
                                Calendar usr_et = Calendar.getInstance();
                                q_window.setTime(query_window);
                                usr_et.setTime(unsorted_obj_et);                                
                                
                                // check if user's end_time is later than query window
                                if(usr_et.after(q_window))    {
                                    // if user's end time is later than query window, use query window end time
                                    new_obj.setEnd_time(user_query_time);
                                    continue outer;
                                }
                                else    {
                                    
                                    // else set to the user's start time instead
                                    new_obj.setEnd_time(unsorted_obj.getEnd_time());                                    
                                    continue outer;
                                }                                
                            } catch(ParseException e)   {
                                e.printStackTrace();
                            }               
                        }
                        else    {
                            
                            // else if different location
                            
                            // 50 - 59 (location b) modify previous location end_time as well to the start time of unsorted.
                            new_obj.setEnd_time(unsorted_obj.getStart_time());
                            // create another object with another location with the start time 50 - 59
                            new_list.add(new UserLocationInterval(unsorted_obj.getMac_address(), unsorted_obj.getEmail(), unsorted_obj.getLocation_id(), unsorted_obj.getStart_time()));
                            
                            continue outer;
                        }
                    }                    
                }
            }            
            // add to new list
            new_list.add(new UserLocationInterval(mac_address, email, location, start_time));
        }        
        return new_list;
    }
}
