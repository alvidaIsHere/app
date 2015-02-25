/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.sloca.dao.Utility;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Langxin
 */
public class Companion {

    private String location_id;
    private String mac_address;
    private String startTime;
    private String endTime;
    private String email;
    

    public Companion(String location_id, String mac_address, String startTime) {
        this.location_id = location_id;
        this.mac_address = mac_address;
        this.startTime = startTime;
        
        try {
            endTime = Utility.getEndDate(startTime, "yyyy-MM-dd HH:mm:ss", 9);
        } catch (ParseException ex) {
            Logger.getLogger(Companion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Companion(String location_id, String mac_address, String startTime, String email) {
        this.location_id = location_id;
        this.mac_address = mac_address;
        this.startTime = startTime;
        this.email = email;
        
        try {
            endTime = Utility.getEndDate(startTime, "yyyy-MM-dd HH:mm:ss", 9);
        } catch (ParseException ex) {
            Logger.getLogger(Companion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Companion(String location_id, String mac_address, String startTime, String endTime, String email) {
        this.location_id = location_id;
        this.mac_address = mac_address;
        this.startTime = startTime;
        this.endTime = endTime;
        this.email = email;
    }

    public String getLocation_ID() {
        return location_id;
    }

    public String getMac_Address() {
        return mac_address;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    public String getEmail() {
        return email;
    }

}
