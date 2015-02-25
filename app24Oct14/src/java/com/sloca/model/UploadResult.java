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
public class UploadResult {

    private String status = "";
    private final String filename;
    private int uploadCount;
    private final ArrayList<UploadErrorMessage> errorList;

    public UploadResult(String filename) {
        this.filename = filename;
        errorList = new ArrayList<UploadErrorMessage>();
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
        if (errorList.isEmpty()) {
            status = "success";
        } else {
            status = "failure";
        }
    }

    public int getErrorCount() {
        return errorList.size();
    }

    public void addErrorMessage(UploadErrorMessage errorMessage) {
        errorList.add(errorMessage);
    }
    
    public ArrayList<UploadErrorMessage> getErrorList(){
        return errorList;
    }

    @Override
    public String toString() {
        return "status : " + status + "\nnum-record-loaded : \n\t" + filename 
                + " : " + uploadCount + "\nError Count : " + errorList.size();
    }

}
