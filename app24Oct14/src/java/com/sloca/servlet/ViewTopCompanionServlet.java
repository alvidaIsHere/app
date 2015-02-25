/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.*;
import com.sloca.model.CompanionTimeSpent;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Langxin, Elvin
 */

public class ViewTopCompanionServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");

        //hashmap for companion and time spent
        //HashMap<String, Integer> Companion = new HashMap<String, Integer>();
        String specifiedUser = request.getParameter("name");
        String kVal = request.getParameter("KValue");
        if (kVal == null || kVal.isEmpty()) {
            kVal = "3";
        }
        int k = Integer.parseInt(kVal);
        String startDateTime = request.getParameter("startTime");
        String endDateTime = request.getParameter("endTime");
        
        System.out.println(startDateTime);
        System.out.println(endDateTime);
        
        try {
            ArrayList<CompanionTimeSpent> topKResult = BasicReportingDAO.retrieveTopKCompanions(k, startDateTime, endDateTime, specifiedUser);
            request.setAttribute("displayResults", topKResult);
            request.setAttribute("KValue", kVal);

            RequestDispatcher dispatcher = request.getRequestDispatcher("topKCompanionReport.jsp");
            dispatcher.forward(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ViewTopCompanionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}