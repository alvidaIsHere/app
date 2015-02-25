/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.model;

import java.util.ArrayList;

/**
 *
 * @author jeremy.kuah.2013
 */
public class UploadErrorMessage {
    
    private final String lineNumber;
    private final ArrayList<String> errorMessages;
    
    public UploadErrorMessage(int lineNumber, String errorMessage){
        this.lineNumber = "" + lineNumber;
        errorMessages = new ArrayList<String>();
        errorMessages.add(errorMessage);
    }
    
    public void addErrorMessage(String errorMessage){
        errorMessages.add(errorMessage);
    }
    
    public String getLineNumber(){
        return lineNumber;
    }
    
    public ArrayList<String> getErrorMessages(){
        return errorMessages;
    }
    
}
