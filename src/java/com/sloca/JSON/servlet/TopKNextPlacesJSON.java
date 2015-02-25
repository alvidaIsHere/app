/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.sloca.dao.BasicReportingDAO;
import com.sloca.dao.GroupReportingDAO;
import com.sloca.dao.Utility;
import com.sloca.model.GroupPopularPlace;
import com.sloca.model.TopKNextPlaceResult;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Elvin, Jeremy
 */
public class TopKNextPlacesJSON extends HttpServlet {

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
        /* TODO output your page here. You may use following sample code. */
        /*sample
         http://localhost:8080/sloca_app/json/top-k-next-places?token=[tokenValue]&k=2&date=2014-03-24T15:30:00&origin=SMUSISL1LOBBY

         */
        // get user inputs
        String origin = request.getParameter("origin");
        String k = request.getParameter("k");
        String date = request.getParameter("date");
        String token = request.getParameter("token");

        //validation for kValue
        int kValue = 0;
        if (k == null || k.isEmpty()) {
            k = "3";
        }

        if (date != null) {
            date = date.replace('T', ' ');
        }

        JSONObject obj = new JSONObject();

        boolean hasError = false;
        boolean continueChecking = true;

        ArrayList<String> errList = new ArrayList<>();

        //validation for token
        if (token == null) {
            hasError = true;
            continueChecking = false;
            errList.add("missing token");
        } else if (token.isEmpty()) {
            hasError = true;
            continueChecking = false;
            errList.add("token is blank");
        } else {
            try {
                if (JWTUtility.verify(token, "1234567890qwerty") == null) {
                    hasError = true;
                    continueChecking = false;
                    errList.add("invalid token");
                } else {
                    String username = JWTUtility.verify(token, "1234567890qwerty");
                    if (!username.equals("admin")) {
                        hasError = true;
                        continueChecking = false;
                        errList.add("invalid token");
                    }
                }
            } catch (JWTException ex) {
                hasError = true;
                continueChecking = false;
                errList.add("invalid token");
            }
        }

        //validation for origin
        ArrayList<String> semanticPlaces = BasicReportingDAO.getAllLocationNames();
        if (origin == null) {
            hasError = true;
            continueChecking = false;
            errList.add("missing origin");
        } else if (origin.isEmpty()) {
            hasError = true;
            continueChecking = false;
            errList.add("origin is blank");
        } else if (!semanticPlaces.contains(origin)) {
            hasError = true;
            continueChecking = false;
            errList.add("invalid origin");
        }

        //validation for date
        if (date == null) {
            hasError = true;
            continueChecking = false;
            errList.add("missing date");
        } else if (date.trim().isEmpty()) {
            hasError = true;
            continueChecking = false;
            errList.add("date is blank");
        }

        //check for kValue
        if (continueChecking) {

            try {
                Utility.getEndDate(date, "yyyy-MM-dd HH:mm:ss", -15);
            } catch (ParseException e) {
                hasError = true;
                continueChecking = false;
                errList.add("invalid date");
            }

            try {
                kValue = Integer.parseInt(k);

                if (kValue < 1 || kValue > 10) {
                    hasError = true;
                    errList.add("invalid k");
                }
            } catch (NumberFormatException nfe) {
                hasError = true;
                errList.add("invalid k");
            }
        }
        Collections.sort(errList);

        if (hasError) {
            obj.put("status", "error");
            JSONArray errorArray = new JSONArray();

            for (String err : errList) {
                errorArray.add(err);
            }

            obj.put("messages", errorArray);
            out.println(obj);

        } else {

            try {
                obj.put("status", "success");

                ArrayList<String> macAddresses = BasicReportingDAO.getUsersLocatedAt(origin, date);
                //total number of users in the semantic place being queried
                int totalUsersQueried = macAddresses.size();
                obj.put("total-users", totalUsersQueried);
                HashMap<String, String> nextPlaces = BasicReportingDAO.getLastVisitForUsers(macAddresses, origin, date); //HashMap<userMac, lastVisitSemanticLocation>

                //number of users who visited another place (excluding the users who have stayed at the same place or had left the place but have not visited another place)
                int allUsersVisitedOtherPlace = nextPlaces.size();
                obj.put("total-next-place-users", allUsersVisitedOtherPlace);

                HashMap<Integer, ArrayList<TopKNextPlaceResult>> topKNextPlacesResult = BasicReportingDAO.getTopKNextPlaces(nextPlaces, kValue);
                ArrayList<GroupPopularPlace> groupPopularPlaces = GroupReportingDAO.convertGroupNextPlaceResult(topKNextPlacesResult);

                //create json array
                JSONArray topKPopularArray = new JSONArray();
                for (GroupPopularPlace result : groupPopularPlaces) {
                    JSONObject tempObj = new JSONObject();
                    tempObj.put("rank", result.getRank());
                    tempObj.put("semantic-place", result.getSemanticPlace());
                    tempObj.put("count", result.getCount());
                    topKPopularArray.add(tempObj);
                }

                obj.put("results", topKPopularArray);
                out.println(obj);
            } catch (ParseException | NullPointerException | SQLException ex) {
                Logger.getLogger(TopKNextPlacesJSON.class.getName()).log(Level.SEVERE, null, ex);
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
