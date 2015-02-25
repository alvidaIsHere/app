/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

/**
 *
 * @author jeremy.kuah.2013
 */
public class Members {

    private final String email;
    private final String mac_address;

    public Members(String email, String mac_address) {
        this.email = email;
        this.mac_address = mac_address;
    }

    public String getEmail() {
        return email;
    }

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
}
