/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.*;
import com.sloca.model.CompanionTimeSpent;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
        String message = "";
        //hashmap for companion and time spent
        //HashMap<String, Integer> Companion = new HashMap<String, Integer>();
        String macAddr = request.getParameter("MAC");
        String kVal = request.getParameter("KValue");
        if (kVal == null || kVal.isEmpty()) {
            kVal = "3";
        }
        int k = Integer.parseInt(kVal);

        String dateTime = request.getParameter("startTime");
        String endDateTime = "";
        ArrayList<CompanionTimeSpent> topKResult = null;

        try {
            endDateTime = Utility.getEndDate(dateTime, "yyyy-MM-dd HH:mm:ss", -15);
            topKResult = BasicReportingDAO.retrieveTopKCompanions(k, dateTime, endDateTime, macAddr);

        } catch (java.text.ParseException e) {
            message += "Date error. Please select the date from the date icon on the right :)";
            Logger.getLogger(ViewTopCompanionServlet.class.getName()).log(Level.SEVERE, null, e);

        }

        request.setAttribute("displayResults", topKResult);
        request.setAttribute("KValue", kVal);
        request.setAttribute("errorMessage", message);

        RequestDispatcher dispatcher = request.getRequestDispatcher("topKCompanionReport.jsp");
        dispatcher.forward(request, response);

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
