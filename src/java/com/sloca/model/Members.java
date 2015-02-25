/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author jeremy.kuah.2013
 */
public class Members implements Comparable<Members> {

    @Expose
    private final String email;
    @Expose
    @SerializedName("mac-address")
    private final String mac_address;

    /**
     * This constructor creates a member with an email and mac address
     *
     * @param email the email address for this member
     * @param mac_address the mac address for this member
     */
    public Members(String email, String mac_address) {
        this.email = email;
        this.mac_address = mac_address;
    }

    /**
     *
     * @return returns the email address of this member
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return returns the mac address of this member
     */
    public String getMac_address() {
        return mac_address;
    }

    //e: for group top K next places
    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Members) {
            Members m = (Members) object;
            if (this.email.equals(m.email) && this.mac_address.equals(m.mac_address)) {
                sameSame = true;
            } else {
                sameSame = false;
            }
        }

        return sameSame;
    }

    /**
     *
     * @return the member's email and mac address
     */
    @Override
    public String toString() {
        return "Members{" + "email=" + email + ", mac_address=" + mac_address + '}';
    }

    /**
     *
     * @param otherMember another member to compare with
     * @return 0 if the mac addresses are similar, a value less than 0 if the
     * argument is a string lexicographically greater than this string; and a
     * value greater than 0 if the argument is a string lexicographically less
     * than this string.
     */
    @Override
    public int compareTo(Members otherMember) {

        // Ascending
        int compareEmail = email.compareTo(otherMember.getEmail());

        if (compareEmail == 0) {
            return mac_address.compareTo(otherMember.getMac_address());
        }

        return compareEmail;
    }
}