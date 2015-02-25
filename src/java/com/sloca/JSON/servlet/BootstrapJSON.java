/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.sloca.dao.FileDAO;
import com.sloca.dao.UploadDAO;
import com.sloca.db.ConnectionFactory;
import com.sloca.model.UploadErrorMessage;
import com.sloca.model.UploadResult;
import com.sloca.servlet.uploadServlet;
import is203.JWTException;
import is203.JWTUtility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jeremy.kuah.2013
 */
@MultipartConfig
public class BootstrapJSON extends HttpServlet {

    private final static Logger LOGGER
            = Logger.getLogger(BootstrapJSON.class.getCanonicalName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter printOut = response.getWriter();

        final Part filePart = request.getPart("bootstrap-file");

        String token = request.getParameter("token");

        JSONObject obj = new JSONObject();

        boolean hasError = false;

        ArrayList<String> errList = new ArrayList<String>();

        if (token == null) {
            hasError = true;
            errList.add("missing token");
        } else if (token.isEmpty()) {
            hasError = true;
            errList.add("blank token");
        } else {
            try {
                if (!"admin".equals(JWTUtility.verify(token, "1234567890qwerty"))) {
                    hasError = true;
                    errList.add("invalid token");
                }
            } catch (JWTException ex) {
                hasError = true;
                errList.add("invalid token");
            }
        }

        if (filePart == null) {
            hasError = true;
            errList.add("no file selected");
        }

        Collections.sort(errList);

        if (hasError) {
            obj.put("status", "error");
            JSONArray errorArray = new JSONArray();

            for (String err : errList) {
                errorArray.add(err);
            }

            obj.put("messages", errorArray);
            printOut.println(obj);
            return;
        }

        UploadResult locationLookupError = null;
        UploadResult locationError = null;
        UploadResult demographicsError = null;

        // Create path components to save the file
        final String path;
        //path = getServletContext().getRealPath("") + File.separator + "uploads";
        path = "./uploads";

        File fileUploadPath = new File(path);
        fileUploadPath.mkdir();

        final String fileName = getFileName(filePart);

        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        //only proceed for zip file
        if (".zip".equals(fileName.substring(fileName.length() - 4))) {

            try {
                //upload zip file
                out = new FileOutputStream(new File(path + File.separator
                        + fileName));
                filecontent = filePart.getInputStream();

                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                System.out.println("New file " + fileName + " created at " + path);
                LOGGER.log(Level.INFO, "File{0}being uploaded to {1}",
                        new Object[]{fileName, path});
            } catch (FileNotFoundException fne) {
                writer.println("You either did not specify a file to upload or are "
                        + "trying to upload a file to a protected or nonexistent "
                        + "location.");
                writer.println("<br/> ERROR: " + fne.getMessage());

                LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                        new Object[]{fne.getMessage()});
            } finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
                if (writer != null) {
                    //writer.close();
                }
            }

            // .zip filename without extension
            String filename = fileName.substring(0, fileName.length() - 4);

            // location of the .zip file
            String filePath = path + File.separator + fileName;

            // location to unzip to
            String unzipPath = path + File.separator + filename;

            //unzip zip file
            boolean unzipped = FileDAO.unzipFile(unzipPath, filePath);
            if (unzipped == true) {
                System.out.println("unzipped");
            } else {
                System.out.println("zipped false");
            }

            Connection connection = null;
            PreparedStatement stmt = null;

            try {
                //truncate the database
                connection = ConnectionFactory.getConnection();

                String truncLocationLookup = "TRUNCATE TABLE location_lookup";
                PreparedStatement truncateLocationLookup = connection.prepareStatement(truncLocationLookup);
                truncateLocationLookup.execute();

                String truncLocation = "TRUNCATE TABLE location";
                PreparedStatement truncateLocation = connection.prepareStatement(truncLocation);
                truncateLocation.execute();

                String truncDemographics = "TRUNCATE TABLE student";
                PreparedStatement truncateDemographics = connection.prepareStatement(truncDemographics);
                truncateDemographics.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionFactory.close(connection, stmt);
            }

            UploadDAO upload = new UploadDAO();

            try {
                locationLookupError = upload.uploadFileToDatabase("location-lookup.csv", unzipPath, false);
            } catch (Exception ex) {
                Logger.getLogger(uploadServlet.class.getName()).log(Level.SEVERE, "servlet", ex);
            }

            try {
                demographicsError = upload.uploadFileToDatabase("demographics.csv", unzipPath, false);
            } catch (Exception ex) {
                Logger.getLogger(uploadServlet.class.getName()).log(Level.SEVERE, "servlet", ex);
            }

            try {
                locationError = upload.uploadFileToDatabase("location.csv", unzipPath, false);
            } catch (Exception ex) {
                Logger.getLogger(uploadServlet.class.getName()).log(Level.SEVERE, "servlet", ex);
            }

            if (locationError.getStatus().equals("success") && demographicsError.getStatus().equals("success")
                    && locationLookupError.getStatus().equals("success")) {
                obj.put("status", "success");
                JSONArray loadedArray = new JSONArray();

                JSONObject demographObj = new JSONObject();
                demographObj.put(demographicsError.getFileName(), demographicsError.getUploadCount());
                loadedArray.add(demographObj);

                JSONObject locLookupObj = new JSONObject();
                locLookupObj.put(locationLookupError.getFileName(), locationLookupError.getUploadCount());
                loadedArray.add(locLookupObj);

                JSONObject locObj = new JSONObject();
                locObj.put(locationError.getFileName(), locationError.getUploadCount());
                loadedArray.add(locObj);

                obj.put("num-record-loaded", loadedArray);

                printOut.println(obj);
            } else {

                obj.put("status", "error");

                JSONArray loadedArray = new JSONArray();

                JSONObject demographObj = new JSONObject();
                demographObj.put(demographicsError.getFileName(), demographicsError.getUploadCount());
                loadedArray.add(demographObj);

                JSONObject locLookupObj = new JSONObject();
                locLookupObj.put(locationLookupError.getFileName(), locationLookupError.getUploadCount());
                loadedArray.add(locLookupObj);

                JSONObject locObj = new JSONObject();
                locObj.put(locationError.getFileName(), locationError.getUploadCount());
                loadedArray.add(locObj);

                obj.put("num-record-loaded", loadedArray);

                ArrayList<UploadErrorMessage> demographMsg = demographicsError.getErrorList();
                ArrayList<UploadErrorMessage> locLookupMsg = locationLookupError.getErrorList();
                ArrayList<UploadErrorMessage> locMsg = locationError.getErrorList();

                JSONArray errorArray = new JSONArray();

                for (int i = 0; i < demographMsg.size(); i++) {
                    UploadErrorMessage currMsg = demographMsg.get(i);
                    ArrayList<String> listOfErrors = currMsg.getErrorMessages();

                    JSONObject tempObj = new JSONObject();

                    tempObj.put("file", demographicsError.getFileName());
                    tempObj.put("line", currMsg.getLineNumber());

                    JSONArray errorList = new JSONArray();

                    for (int j = 0; j < listOfErrors.size(); j++) {
                        errorList.add(listOfErrors.get(j));
                    }
                    tempObj.put("message", errorList);
                    errorArray.add(tempObj);
                }

                for (int i = 0; i < locLookupMsg.size(); i++) {
                    UploadErrorMessage currMsg = locLookupMsg.get(i);
                    ArrayList<String> listOfErrors = currMsg.getErrorMessages();

                    JSONObject tempObj = new JSONObject();

                    tempObj.put("file", locationLookupError.getFileName());
                    tempObj.put("line", currMsg.getLineNumber());

                    JSONArray errorList = new JSONArray();

                    for (int j = 0; j < listOfErrors.size(); j++) {
                        errorList.add(listOfErrors.get(j));
                    }
                    tempObj.put("message", errorList);
                    errorArray.add(tempObj);
                }

                for (int i = 0; i < locMsg.size(); i++) {
                    UploadErrorMessage currMsg = locMsg.get(i);
                    ArrayList<String> listOfErrors = currMsg.getErrorMessages();

                    JSONObject tempObj = new JSONObject();

                    tempObj.put("file", locationError.getFileName());
                    tempObj.put("line", currMsg.getLineNumber());

                    JSONArray errorList = new JSONArray();

                    for (int j = 0; j < listOfErrors.size(); j++) {
                        errorList.add(listOfErrors.get(j));
                    }
                    tempObj.put("message", errorList);
                    errorArray.add(tempObj);
                }

                obj.put("error", errorArray);

                printOut.println(obj);
            }

        } else {
            obj.put("status", "error");
            obj.put("message", "not a zip file");

            printOut.println(obj);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}
