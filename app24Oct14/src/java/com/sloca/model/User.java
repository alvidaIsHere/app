/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

/**
 *
 * @author alice
 */
public class User {
    String mac_address;
    String date_time;
    public User(String mac_address, String date_time)   {
        this.mac_address = mac_address;
        this.date_time = date_time;
    }
}
