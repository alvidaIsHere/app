/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.servlet;

import com.google.gson.Gson;
import com.sloca.dao.HeatMapDAO;
import com.sloca.dao.Utility;
import com.sloca.model.HeatMap;
import com.sloca.model.HeatMapBreakdown;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alice
 */

public class ViewHeatMapServlet extends HttpServlet {

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
        // get user inputs
        String dateTime = request.getParameter("datetime");
        String location = request.getParameter("location");
        
        // check for proper date input
        try {
            // try to parse
            String endDate = Utility.getEndDate(dateTime, "yyyy-MM-dd HH:mm:ss", -4320);
            // invoke method to query heat map
            ArrayList<HeatMap> results = HeatMapDAO.retrieveHeatMapForLocation(location, dateTime, endDate);
            // debug print results
            for(HeatMap m : results)   {
                System.out.println("OIE! " + m);
            }
            
            // package for json output
            HeatMapBreakdown breakdown = new HeatMapBreakdown("success", results);
            // convert to json object
            String json = new Gson().toJson(breakdown);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);    
        } catch (ParseException e)  {
            e.printStackTrace();
            // if it's not parsable, direct back to view heat map page
            response.sendRedirect("viewHeatMap.jsp?error=1");
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
