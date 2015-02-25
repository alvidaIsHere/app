/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.servlet;

import com.sloca.dao.HeatMapDAO;
import com.sloca.dao.Utility;
import com.sloca.model.HeatMap;
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
 * @author alice
 */
public class ToggleHeatMapServlet extends HttpServlet {

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
        // this servlet will forward the request params to the view heat map result page
        String dateTime = request.getParameter("datetime");
        String location = request.getParameter("location");                
        
        try {
            // try to parse
            String endDate = Utility.getEndDate(dateTime, "yyyy-MM-dd HH:mm:ss", -15);
            // check for invalid inputs
            if(location == null)    {
                // forward params
                RequestDispatcher rd = request.getRequestDispatcher("reportError.jsp");
                request.setAttribute("error", "You've entered an invalid input. Please enter a location.");
                rd.forward(request, response);
            }
            else    {
                // forward params
                RequestDispatcher rd = request.getRequestDispatcher("heatMapResults.jsp");
                // invoke method to query heat map
                ArrayList<HeatMap> results = HeatMapDAO.retrieveHeatMapForLocation(location, dateTime, endDate);
                request.setAttribute("heatmaps", results);
                request.setAttribute("datetime", dateTime);
                request.setAttribute("location", location);
                rd.forward(request, response);
            } 
        } catch(ParseException e)   {
            e.printStackTrace();
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
