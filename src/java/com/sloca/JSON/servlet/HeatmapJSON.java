/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import org.json.simple.*;
import com.sloca.dao.HeatMapDAO;
import com.sloca.dao.Utility;
import com.sloca.model.HeatMap;
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
 * @author alice
 */
public class HeatmapJSON extends HttpServlet {

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

        // get user inputs
        String token = request.getParameter("token");
        String floor = request.getParameter("floor");
        String date = request.getParameter("date");

        if (date != null) {
            date = date.replace('T', ' ');
        }

        JSONObject obj = new JSONObject();

        boolean hasError = false;
        boolean continueChecking = true;

        ArrayList<String> errList = new ArrayList<String>();

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

        if (floor == null) {
            hasError = true;
            continueChecking = false;
            errList.add("missing floor");
        } else if (floor.isEmpty()) {
            hasError = true;
            continueChecking = false;
            errList.add("blank floor");
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
            try {
                if (Integer.parseInt(floor) < 0 || Integer.parseInt(floor) > 5) {
                    hasError = true;
                    errList.add("invalid floor");
                }
            } catch (NumberFormatException nfe) {
                hasError = true;
                errList.add("invalid floor");
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

            obj.put("status", "success");

            JSONArray heatmapArray = new JSONArray();

            try {
                String endDate = Utility.getEndDate(date, "yyyy-MM-dd HH:mm:ss", -15);

                ArrayList<HeatMap> results = HeatMapDAO.retrieveHeatMapForLocation(floor, date, endDate);

                for (HeatMap result : results) {
                    JSONObject tempObj = new JSONObject();
                    tempObj.put("semantic-place", result.getSemantic_place());
                    tempObj.put("num-people", result.getNum_people());
                    tempObj.put("crowd-density", result.getCrowd_density());
                    heatmapArray.add(tempObj);
                }
                obj.put("heatmap", heatmapArray);
            } catch (ParseException ex) {
                Logger.getLogger(HeatmapJSON.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println(obj);
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
