/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import java.util.ArrayList;

/**
 *
 * @author alice
 */
public class JSONMessage {

    private String status;
    private ArrayList<String> messages;
    private ArrayList<Object> breakdown;

    /**
     * This class container encapsulates the breakdown of groups and lists the
     * error message(s) and status that comes together with it
     *
     * @param status the status of the JSON query
     * @param message the error message(s) tabulated if any
     * @param breakdown the list of objects in which the user queried for view
     * demographics breakdown
     */
    public JSONMessage(String status, ArrayList<String> message, ArrayList<Object> breakdown) {
        this.status = status;
        this.messages = message;
        this.breakdown = breakdown;
    }
}
