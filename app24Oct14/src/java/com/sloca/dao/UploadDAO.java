package com.sloca.dao;

import com.sloca.model.UploadResult;
import com.sloca.model.UploadErrorMessage;
import au.com.bytecode.opencsv.*;
import com.sloca.db.ConnectionFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author jeremy.kuah.2013
 */
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

public class UploadDAO {

    private String filename = null;	// store filename without extension

    private int lineCount;		// which line error happens
    private int uploadCount;        // how many lines successfully uploaded

    private String[] header;		// store header 

    private UploadResult uploadResult = null;					// store the final uploadResult
    private HashMap<Integer, UploadErrorMessage> errorLineMap = null;		// maps error messages to a line number 

    // table names in DB
    private static final String locationTableName = "location";
    private static final String locationLookupTableName = "location_lookup";
    private static final String demographicsTableName = "student";

    public UploadResult uploadFileToDatabase(String fileName, String path) throws Exception {

        uploadResult = new UploadResult(fileName);
        errorLineMap = new HashMap<Integer, UploadErrorMessage>();
        
        lineCount = 2;
        uploadCount = 0;

        filename = fileName.substring(0, (fileName.length() - 4));

        CSVReader csvReader = null;

        try {

            csvReader = new CSVReader(new BufferedReader(new FileReader(new File(path + File.separator + fileName))));

            Connection connection = null;
            PreparedStatement insertStatement = null;

            try {

                String insertString;

                switch (filename) {
                    case "location":
                        insertString = "insert into " + locationTableName + " values (?,?,?,?)";
                        break;

                    case "demographics":
                        insertString = "insert into " + demographicsTableName + " values (?,?,?,?,?)";
                        break;

                    case "location-lookup":
                        insertString = "insert into " + locationLookupTableName + " values (?,?)";
                        break;

                    default:
                        throw new Exception();
                }

                connection = ConnectionFactory.getConnection();
                connection.setAutoCommit(false);
                insertStatement = connection.prepareStatement(insertString);

                switch (filename) {
                    case "location":
                        uploadLocation(csvReader, connection, insertStatement);
                        break;

                    case "demographics":
                        uploadDemographics(csvReader, connection, insertStatement);
                        break;

                    case "location-lookup":
                        uploadLocationLookup(csvReader, connection, insertStatement);
                        break;

                    default:
                        throw new Exception();
                }

            } finally {
                ConnectionFactory.close(connection, insertStatement);
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Error occured while reading file. "
                    + e.getMessage());
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }

        }

        uploadResult.setUploadCount(uploadCount);
        return uploadResult;

    }

    private boolean validateLocationFields(HashMap<Integer, String> validLocationID, String[] line) {
        boolean isValid = true;
        boolean doRemainingValidation = true;

        for (int i = 0; i < line.length; i++) {
            String col = line[i].trim();

            if (col.isEmpty()) {
                String errorMessage = header[i] + " is blank";
                isValid = false;
                doRemainingValidation = false;
                addErrorMessage(errorMessage);
            }
        }

        if (line.length != 3) {
            return false;
        }

        if (doRemainingValidation) {

            if (FileValidationDAO.validateDate(line[0])) {
            } else {
                isValid = false;
                String errorMessage = "invalid timestamp";
                addErrorMessage(errorMessage);
            }

            if (FileValidationDAO.validateMacAddFormat(line[1])) {
            } else {
                isValid = false;
                String errorMessage = "invalid mac address";
                addErrorMessage(errorMessage);
            }

            if (FileValidationDAO.validateLocID(validLocationID, line[2])) {
            } else {
                isValid = false;
                String errorMessage = "invalid location";
                addErrorMessage(errorMessage);
            }
        }

        return isValid;
    }

    private boolean validateLocationLookup(String[] line) {
        boolean isValid = true;
        boolean doRemainingValidation = true;

        for (int i = 0; i < line.length; i++) {
            String col = line[i].trim();

            if (col.isEmpty()) {
                String errorMessage = header[i] + " is blank";
                isValid = false;
                doRemainingValidation = false;
                addErrorMessage(errorMessage);
            }
        }

        if (line.length != 2) {
            return false;
        }

        if (doRemainingValidation) {

            if (FileValidationDAO.validateLocationID(line[0])) {
            } else {
                isValid = false;
                String errorMessage = "invalid location id";
                addErrorMessage(errorMessage);
            }

            if (FileValidationDAO.validateSemanticPlace(line[1])) {
            } else {
                isValid = false;
                String errorMessage = "invalid semantic name";
                addErrorMessage(errorMessage);
            }
        }

        return isValid;
    }

    private boolean validateDemographicsFields(String[] line) {
        boolean isValid = true;
        boolean doRemainingValidation = true;

        for (int i = 0; i < line.length; i++) {
            String col = line[i].trim();

            if (col.isEmpty()) {
                String errorMessage = header[i] + " is blank";
                isValid = false;
                doRemainingValidation = false;
                addErrorMessage(errorMessage);
            }
        }

        if (line.length != 5) {
            return false;
        }

        if (doRemainingValidation) {

            if (FileValidationDAO.validateMacAddFormat(line[0])) {
            } else {
                isValid = false;
                String errorMessage = "invalid mac address";
                addErrorMessage(errorMessage);
            }

            if (FileValidationDAO.validatePassword(line[2])) {
            } else {
                isValid = false;
                String errorMessage = "invalid password";
                addErrorMessage(errorMessage);
            }

            if (FileValidationDAO.validateEmail(line[3])) {
            } else {
                isValid = false;
                String errorMessage = "invalid email";
                addErrorMessage(errorMessage);
            }

            if (FileValidationDAO.validateGender(line[4])) {
            } else {
                isValid = false;
                String errorMessage = "invalid gender";
                addErrorMessage(errorMessage);
            }
        }

        return isValid;
    }

    private void uploadLocation(CSVReader reader, Connection connection, PreparedStatement insertStatement) {

        try {
            if ((header = reader.readNext()) == null) {
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] line = null;

        HashMap<Integer, String> validLocationID = populateValidLocationID(connection);

        try {
            try {
                while ((line = reader.readNext()) != null) {

                    boolean isValid = true;
                    isValid = validateLocationFields(validLocationID, line);
                    System.out.println(lineCount + " : " + isValid);

                    if (isValid) {
                        int index = 2;
                        
                        insertStatement.setInt(1, lineCount);

                        for (int i = 0; i < line.length; i++) {
                            line[i] = line[i].trim();

                            if (i == 0) { //column time stamp
                                insertStatement.setString(index++, line[i]);
                            }
                            if (i == 1) { //column mac addr
                                insertStatement.setString(index++, line[i]);
                            }
                            if (i == 2) { //columm location id
                                insertStatement.setInt(index++, Integer.parseInt(line[i]));
                            }
                        }

                        try {
                            insertStatement.execute();
                            uploadCount++;
                        } catch (SQLException e) {
                            System.out.println("line " + lineCount + " duplicate row");
                            
                            // retrieve the row number where mac addr/time matches
                            // store that row number to print as duplicate row
                            // update that row to update location id and row number with current number
                            
                            String retrieveDuplicateRowNumber = 
                                    "SELECT rowNumber FROM " + locationTableName + " WHERE "
                                    + "time = '" + line[0] + "' AND student_mac_address = '" + line[1] + "'";
                            
                            PreparedStatement getRowNumber = connection.prepareStatement(retrieveDuplicateRowNumber);
                            ResultSet rs = getRowNumber.executeQuery();
                            
                            int rowNum = 0;
                            
                            while(rs.next()){
                                rowNum = rs.getInt(1);
                            }
                            
                            String updateStatement = 
                                    "UPDATE " + locationTableName + " SET rowNumber = ?, location_id = ? WHERE rowNumber = ?";
                            PreparedStatement updateDB = connection.prepareStatement(updateStatement);
                            updateDB.setInt(1, lineCount);
                            updateDB.setInt(2, Integer.parseInt(line[2]));
                            updateDB.setInt(3, rowNum);
                            
                            updateDB.execute();
                            
                            addErrorMessage(rowNum, "duplicate row");
                        }

                    }

                    lineCount++;

                }

                connection.commit();

            } catch (IOException ex) {
                Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void uploadLocationLookup(CSVReader reader, Connection connection, PreparedStatement insertStatement) {

        try {
            if ((header = reader.readNext()) == null) {
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] line = null;

        try {
            try {
                while ((line = reader.readNext()) != null) {

                    boolean isValid = true;
                    isValid = validateLocationLookup(line);
                    System.out.println(lineCount + " : " + isValid);

                    if (isValid) {
                        int index = 1;

                        for (int i = 0; i < line.length; i++) {
                            line[i] = line[i].trim();

                            if (i == 0) {
                                insertStatement.setInt(index++, Integer.parseInt(line[i]));
                            } else {
                                insertStatement.setString(index++, line[i]);
                            }
                        }

                        try {
                            insertStatement.execute();
                            uploadCount++;
                        } catch (SQLException e) {
                            System.out.println("line " + lineCount + " location id already exists");
                            addErrorMessage("location id already exists");
                        }

                    }

                    lineCount++;

                }

                connection.commit();

            } catch (IOException ex) {
                Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void uploadDemographics(CSVReader reader, Connection connection, PreparedStatement insertStatement) {

        try {
            if ((header = reader.readNext()) == null) {
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] line = null;

        try {
            try {
                while ((line = reader.readNext()) != null) {

                    boolean isValid = true;
                    isValid = validateDemographicsFields(line);
                    System.out.println(lineCount + " : " + isValid);

                    if (isValid) {
                        int index = 1;

                        for (int i = 0; i < line.length; i++) {
                            line[i] = line[i].trim();
                            
                            insertStatement.setString(index++, line[i]);
                        }

                        try {
                            insertStatement.execute();
                            uploadCount++;
                        } catch (SQLException e) {
                            System.out.println("line " + lineCount + " mac address already exists");
                            addErrorMessage("mac address already exists");
                        }

                    }

                    lineCount++;

                }

                connection.commit();

            } catch (IOException ex) {
                Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private HashMap<Integer, String> populateValidLocationID(Connection connection) {

        HashMap<Integer, String> validLocationID = new HashMap<Integer, String>();

        Statement stmt = null;
        ResultSet rs = null;

        String query = "SELECT location_id FROM " + locationLookupTableName;

        try {
            stmt = connection.createStatement();

            if (stmt != null) {
                rs = stmt.executeQuery(query);
            }

            while (rs.next()) {
                int locationID = rs.getInt(1);
                validLocationID.put(locationID, "EXIST");
            }

        } catch (SQLException ex) {
            Logger.getLogger(UploadDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.close(stmt, rs);
        }

        return validLocationID;

    }

    private void addErrorMessage(int lineNumber, String errMsg) {
        if (errorLineMap.get(lineNumber) == null) {

            // Create hew UploadErrorMessage (error message for this line don't exist yet)
            UploadErrorMessage err = new UploadErrorMessage(lineNumber, errMsg);

            // Put the UploadErrorMessage in HashMap [Key: line number, Value: error message object]
            errorLineMap.put(lineNumber, err);

            // Add the error message object into upload result
            uploadResult.addErrorMessage(err);

        } else {

            UploadErrorMessage err = errorLineMap.get(lineNumber);
            err.addErrorMessage(errMsg);

        }
    }
    
    private void addErrorMessage(String errMsg) {
        if (errorLineMap.get(lineCount) == null) {

            // Create hew UploadErrorMessage (error message for this line don't exist yet)
            UploadErrorMessage err = new UploadErrorMessage(lineCount, errMsg);

            // Put the UploadErrorMessage in HashMap [Key: line number, Value: error message object]
            errorLineMap.put(lineCount, err);

            // Add the error message object into upload result
            uploadResult.addErrorMessage(err);

        } else {

            UploadErrorMessage err = errorLineMap.get(lineCount);
            err.addErrorMessage(errMsg);

        }
    }

}
