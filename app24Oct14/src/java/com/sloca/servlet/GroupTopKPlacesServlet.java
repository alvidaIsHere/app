/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.servlet;

import com.sloca.dao.BasicReportingDAO;
import com.sloca.dao.Utility;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Elvin
 */
@WebServlet(name = "GroupTopKPlaces", urlPatterns = {"/GroupTopKPlaces.do"})
public class GroupTopKPlacesServlet extends HttpServlet{
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

        // getting top K value
        String kVal = request.getParameter("KValue");
        
        // getting selected time
        String datetime = request.getParameter("datetime");
        
        String message = "";
        
        try {
            //get time for 15 min interval
            //get all groups at that interval
            //for all group in groups, get the last location (semantic place)
            //from <<last locations>> do location count for each location and order based on count
            

        } catch (NullPointerException ex) {
            ex.printStackTrace();
            message += "NullPointerException O.O! Please inform system admin ";
        }
        request.setAttribute("errorMessage", message);
//        request.setAttribute("totalUsersQueried", totalUsersQueried);
//        request.setAttribute("allUsersVisitedOtherPlace", allUsersVisitedOtherPlace);
//        request.setAttribute("result", topKNextPopPlacesResult);
        RequestDispatcher dispatcher = request.getRequestDispatcher("topKNextPlaceReport.jsp");
        dispatcher.forward(request, response);
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
