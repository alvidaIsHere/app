/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sloca.dao.GroupReportingDAO;
import com.sloca.dao.GroupsDAO;
import com.sloca.dao.Utility;
import com.sloca.model.GroupLocations;
import com.sloca.model.GroupPopularPlace;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Langxin
 */
public class GroupTopKPopularPlacesJSON extends HttpServlet {

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

        PrintWriter out = response.getWriter();

        String token = request.getParameter("token");
        String k = request.getParameter("k");
        String date = request.getParameter("date");

        if (date != null) {
            date = date.replace('T', ' ');
        }

        if (k == null || k.isEmpty()) {
            k = "3";
        }

        ArrayList<String> errorList = new ArrayList<String>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonOutput = new JsonObject();

        if (token == null) {
            errorList.add("missing token");
        } else if (token.isEmpty()) {
            errorList.add("blank token");
        } else {
            try {
                if (JWTUtility.verify(token, "1234567890qwerty") == null) {
                    errorList.add("invalid token");
                } else {
                    String username = JWTUtility.verify(token, "1234567890qwerty");
                    if (!username.equals("admin")) {
                        errorList.add("invalid token");
                    }
                }
            } catch (JWTException ex) {
                errorList.add("invalid token");
            }
        }

        if (date == null) {
            errorList.add("missing date");
        } else if (date.isEmpty()) {
            errorList.add("blank date");
        }

        if (errorList.isEmpty()) {

            if (k.isEmpty()) {
                errorList.add("blank k");
            } else {
                try {
                    int kValue = Integer.parseInt(k);

                    if (kValue < 1 || kValue > 10) {
                        errorList.add("invalid k");
                    }
                } catch (NumberFormatException nfe) {
                    errorList.add("invalid k");
                }
            }

            try {
                date = Utility.getEndDate(date, "yyyy-MM-dd HH:mm:ss", 0);
            } catch (ParseException ex) {
                errorList.add("invalid date");
                Logger.getLogger(GroupTopKPopularPlacesJSON.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        Collections.sort(errorList);

        if (errorList.size() > 0) {
            jsonOutput.addProperty("status", "error");

            JsonArray errorArr = new JsonArray();

            for (int i = 0; i < errorList.size(); i++) {
                JsonPrimitive e = new JsonPrimitive(errorList.get(i));
                errorArr.add(e);
            }

            jsonOutput.add("messages", errorArr);

            out.println(gson.toJson(jsonOutput));
        } else {

            String endDate;

            try {
                endDate = Utility.getEndDate(date, "yyyy-MM-dd HH:mm:ss", -15);
                ArrayList<GroupLocations> rawGroupList = GroupsDAO.getGroups(date, endDate);
                ArrayList<GroupPopularPlace> popularGroupList = GroupReportingDAO.retrieveGroupTopKPopularPlaces(rawGroupList, Integer.parseInt(k));

                JsonArray jArray = new JsonArray();

                for (int i = 0; i < popularGroupList.size(); i++) {
                    GroupPopularPlace place = popularGroupList.get(i);
                    JsonObject obj = new JsonObject();
                    obj.addProperty("rank", place.getRank());
                    obj.addProperty("semantic-place", place.getSemanticPlace());
                    obj.addProperty("count", place.getCount());

                    jArray.add(obj);
                }

                jsonOutput.addProperty("status", "success");
                jsonOutput.add("results", jArray);

                out.println(gson.toJson(jsonOutput));

            } catch (ParseException ex) {
                Logger.getLogger(GroupTopKPopularPlacesJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
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

}
