/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sloca.dao.GroupsDAO;
import com.sloca.dao.Utility;
import com.sloca.model.AGDstatus;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author jeremy.kuah.2013
 */
public class AutomaticGroupDetectionJSON extends HttpServlet {

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
        String date = request.getParameter("date");

        if (date != null) {
            date = date.replace('T', ' ');
        }

        JSONObject obj = new JSONObject();

        boolean hasError = false;
        boolean continueChecking = true;

        ArrayList<String> errList = new ArrayList<>();

        if (token == null) {
            hasError = true;
            continueChecking = false;
            errList.add("missing token");
        } else if (token.isEmpty()) {
            hasError = true;
            continueChecking = false;
            errList.add("blank token");
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

        if (date == null) {
            hasError = true;
            continueChecking = false;
            errList.add("missing date");
        } else if (date.isEmpty()) {
            hasError = true;
            continueChecking = false;
            errList.add("blank date");
        }

        if (continueChecking) {
            try {
                String endDate = Utility.getEndDate(date, "yyyy-MM-dd HH:mm:ss", -15);
            } catch (ParseException e) {
                hasError = true;
                errList.add("invalid date");
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

            String endDate = "";

            try {
                endDate = Utility.getEndDate(date, "yyyy-MM-dd HH:mm:ss", -15);
            } catch (ParseException ex) {
                Logger.getLogger(AutomaticGroupDetectionJSON.class.getName()).log(Level.SEVERE, null, ex);
            }

            // invoke DAO to retrieve students in location
            //ArrayList<GroupLocations> users_in_location = GroupsDAO.getUsersInLocation(dateTime, endDate);
            AGDstatus results = GroupsDAO.getUsersInLocation(date, endDate);
            results.sort();
            //System.out.println(results.getGroups().get(0).getLocations().get(0).getStart_time());
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            String json = gson.toJson(results);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

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
