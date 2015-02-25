/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.GroupReportingDAO;
import com.sloca.dao.GroupsDAO;
import com.sloca.dao.Utility;
import com.sloca.model.GroupLocations;
import com.sloca.model.GroupPopularPlace;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jeremy.kuah.2013
 */
public class GroupTopKPopularPlacesServlet extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */

        String k = request.getParameter("k");
        String datetime = request.getParameter("datetime");

        // parse to subtract 15 minutes
        try {
            String endDate = Utility.getEndDate(datetime, "yyyy-MM-dd HH:mm:ss", -15);
            //ArrayList<Group> rawGroupList = AutomaticGroupIdentificationDAO.identifyGroup(datetime);
            ArrayList<GroupLocations> rawGroupList = GroupsDAO.getGroups(datetime, endDate);

            ArrayList<GroupPopularPlace> popularGroupList = GroupReportingDAO.retrieveGroupTopKPopularPlaces(rawGroupList, Integer.parseInt(k));

            RequestDispatcher dispatcher = request.getRequestDispatcher("GroupTopKResults.jsp");
            request.setAttribute("result", popularGroupList);
            dispatcher.forward(request, response);
        } catch (ParseException e) {
            e.printStackTrace();

            RequestDispatcher rd = request.getRequestDispatcher("reportError.jsp");
            request.setAttribute("error", "You've entered an invalid date.");
            rd.forward(request, response);
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
