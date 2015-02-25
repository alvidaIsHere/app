/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.sloca.dao.FileDAO;
import com.sloca.dao.FileValidationDAO;
import com.sloca.dao.UploadDAO;
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

public class UpdateJSON extends HttpServlet {

    private final static Logger LOGGER
            = Logger.getLogger(UpdateJSON.class.getCanonicalName());

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
        PrintWriter writer = response.getWriter();

        // Create path components to save the file
        final String path;
        //path = getServletContext().getRealPath("") + File.separator + "uploads";
        path = "./uploads";

        File fileUploadPath = new File(path);
        fileUploadPath.mkdir();

        final Part filePart = request.getPart("bootstrap-file");
        final String fileName = getFileName(filePart);

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
            writer.println(obj);
            return;
        }

        // validate for correct file name
        if (FileValidationDAO.checkValidUploadFile(fileName)) {

            UploadResult errors = null;

            OutputStream out = null;
            InputStream filecontent = null;

            try {
                out = new FileOutputStream(new File(path + File.separator
                        + fileName));
                filecontent = filePart.getInputStream();

                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                LOGGER.log(Level.INFO, "File {0} being uploaded to {1}",
                        new Object[]{fileName, path});

                try {
                    UploadDAO upload = new UploadDAO();
                    errors = upload.uploadFileToDatabase(fileName, path, true);
                } catch (Exception ex) {
                    Logger.getLogger(uploadServlet.class.getName()).log(Level.SEVERE, "servlet", ex);
                }

            } catch (FileNotFoundException fne) {
                LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                        new Object[]{fne.getMessage()});
            } finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            }

            if ("success".equals(errors.getStatus())) {
                obj.put("status", "success");
                JSONArray loadedArray = new JSONArray();

                JSONObject fileObj = new JSONObject();
                fileObj.put(errors.getFileName(), errors.getUploadCount());

                loadedArray.add(fileObj);

                obj.put("num-record-loaded", loadedArray);

                writer.println(obj);
            } else {
                obj.put("status", "error");

                JSONArray loadedArray = new JSONArray();

                JSONObject fileObj = new JSONObject();
                fileObj.put(errors.getFileName(), errors.getUploadCount());
                loadedArray.add(fileObj);

                obj.put("num-record-loaded", loadedArray);

                ArrayList<UploadErrorMessage> errMsg = errors.getErrorList();

                JSONArray errorArray = new JSONArray();

                for (int i = 0; i < errMsg.size(); i++) {
                    UploadErrorMessage currMsg = errMsg.get(i);
                    ArrayList<String> listOfErrors = currMsg.getErrorMessages();

                    JSONObject tempObj = new JSONObject();

                    tempObj.put("file", errors.getFileName());
                    tempObj.put("line", currMsg.getLineNumber());

                    JSONArray errorList = new JSONArray();

                    for (int j = 0; j < listOfErrors.size(); j++) {
                        errorList.add(listOfErrors.get(j));
                    }
                    tempObj.put("message", errorList);
                    errorArray.add(tempObj);
                }

                obj.put("error", errorArray);

                writer.println(obj);
            }

        } else if (FileValidationDAO.checkValidBootstrap(fileName)) {

            UploadResult locationError = null;
            UploadResult demographicsError = null;

            OutputStream out = null;
            InputStream filecontent = null;

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
                LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                        new Object[]{fne.getMessage()});
            } finally {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
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

            UploadDAO upload = new UploadDAO();

            try {
                demographicsError = upload.uploadFileToDatabase("demographics.csv", unzipPath, true);
            } catch (Exception ex) {
                Logger.getLogger(uploadServlet.class.getName()).log(Level.SEVERE, "servlet", ex);
            }

            try {
                locationError = upload.uploadFileToDatabase("location.csv", unzipPath, true);
            } catch (Exception ex) {
                Logger.getLogger(uploadServlet.class.getName()).log(Level.SEVERE, "servlet", ex);
            }

            if (locationError.getStatus().equals("success") && demographicsError.getStatus().equals("success")) {
                obj.put("status", "success");

                JSONArray loadedArray = new JSONArray();

                JSONObject demographObj = new JSONObject();
                demographObj.put(demographicsError.getFileName(), demographicsError.getUploadCount());
                loadedArray.add(demographObj);

                JSONObject locObj = new JSONObject();
                locObj.put(locationError.getFileName(), locationError.getUploadCount());
                loadedArray.add(locObj);

                obj.put("num-record-loaded", loadedArray);

                writer.println(obj);
            } else {
                obj.put("status", "error");

                JSONArray loadedArray = new JSONArray();

                JSONObject demographObj = new JSONObject();
                demographObj.put(demographicsError.getFileName(), demographicsError.getUploadCount());
                loadedArray.add(demographObj);

                JSONObject locObj = new JSONObject();
                locObj.put(locationError.getFileName(), locationError.getUploadCount());
                loadedArray.add(locObj);

                obj.put("num-record-loaded", loadedArray);

                ArrayList<UploadErrorMessage> demographMsg = demographicsError.getErrorList();
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

                writer.println(obj);
            }

        } else {
            obj.put("status", "error");
            obj.put("message", "not a valid file");

            writer.println(obj);
        }

    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        String fileName = null;
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                fileName = content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return fileName;
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

}
