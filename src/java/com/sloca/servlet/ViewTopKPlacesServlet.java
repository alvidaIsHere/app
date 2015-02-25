/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.BasicReportingDAO;
import com.sloca.dao.DateUtilityDAO;
import com.sloca.model.PopularPlace;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class ViewTopKPlacesServlet extends HttpServlet {

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
        String message = "";
        // getting selected time
        String datetime = request.getParameter("datetime");
//        System.out.println("here");
        // check for invalid date
        try {
            // try to parse
            String endDate = DateUtilityDAO.getEndDate(datetime, "yyyy-MM-dd HH:mm:ss", -15);
        } catch (ParseException e) {
//            e.printStackTrace();
//            response.sendRedirect("viewTopKPlaces.jsp");
            e.printStackTrace();
            message += "Date error. Please select the date from the date icon on the right :)";
        }

        ArrayList<PopularPlace> topKResult = BasicReportingDAO.retrieveTopKPopularPlaces(Integer.parseInt(kVal), datetime);
        // set the resultset in ArrayList<String> format
        request.setAttribute("displayResults", topKResult);
        request.setAttribute("errorMessage", message);

        RequestDispatcher dispatcher = request.getRequestDispatcher("topkreport.jsp");
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
