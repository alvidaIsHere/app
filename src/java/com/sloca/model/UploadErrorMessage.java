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
public class UploadErrorMessage implements Comparable<UploadErrorMessage> {

    private final int lineNumber;
    private final ArrayList<String> errorMessages;

    /**
     * Constructor for UploadErrorMessage with line number of the error in file
     * and error message
     *
     * @param lineNumber line number that the error was found
     * @param errorMessage error found
     */
    public UploadErrorMessage(int lineNumber, String errorMessage) {
        this.lineNumber = lineNumber;
        errorMessages = new ArrayList<String>();
        errorMessages.add(errorMessage);
    }

    /**
     * Adds an error message
     *
     * @param errorMessage Error message to be added
     */
    public void addErrorMessage(String errorMessage) {
        errorMessages.add(errorMessage);
    }

    /**
     *
     * @return line number for this object
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     *
     * @return ArrayList of error messages for this UploadErrorMessage object
     */
    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Sorts UploadErrorMessage in ascending order (based on line number)
     *
     * @param err UploadErrorMessage to compare to
     * @return order to be sorted
     */
    @Override
    public int compareTo(UploadErrorMessage err) {
        int compareTime = err.lineNumber;
        // Ascending order
        return this.lineNumber - compareTime;
    }
}
