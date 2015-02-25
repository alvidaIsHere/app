/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jeremy.kuah.2013
 */
public class UploadResult {

    private String status = "";
    private final String filename;
    private int uploadCount;
    private final ArrayList<UploadErrorMessage> errorList;

    /**
     * Creates a new UploadResult object
     *
     * @param filename the filename used was instantiated
     */
    public UploadResult(String filename) {
        this.filename = filename;
        errorList = new ArrayList<UploadErrorMessage>();
    }

    /**
     * get the number of successful upload
     *
     * @return the number of successful upload
     */
    public int getUploadCount() {
        return uploadCount;
    }

    /**
     * Get the name of the file uploaded
     *
     * @return the name of the file
     */
    public String getFileName() {
        return filename;
    }

    /**
     * Count the number of successful upload
     *
     * @param uploadCount the number of successful upload
     */
    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
        Collections.sort(errorList);
        if (errorList.isEmpty() && uploadCount > 0) {
            status = "success";
        } else {
            status = "failure";
        }
    }

    /**
     * Get the number of errors
     *
     * @return the number of errors found
     */
    public int getErrorCount() {
        return errorList.size();
    }

    /**
     * Get the status of the upload
     *
     * @return the status of the upload
     */
    public String getStatus() {
        return status;
    }

    /**
     * Add error message if error is found
     *
     * @param errorMessage the error message to be used when error is found
     */
    public void addErrorMessage(UploadErrorMessage errorMessage) {
        errorList.add(errorMessage);
    }

    /**
     * Get an list of errors
     *
     * @return ArrayList of error messages
     */
    public ArrayList<UploadErrorMessage> getErrorList() {
        return errorList;
    }

    /**
     * To override the toString method
     *
     * @return a String with status, filename, uploadCount and list of errors
     */
    @Override
    public String toString() {
        return "status : " + status + "\nnum-record-loaded : \n\t" + filename
                + " : " + uploadCount + "\nError Count : " + errorList.size();
    }

}
