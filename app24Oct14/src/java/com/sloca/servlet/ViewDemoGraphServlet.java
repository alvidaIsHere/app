/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.servlet;

import com.sloca.dao.Utility;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alice
 */
public class ViewDemoGraphServlet extends HttpServlet {

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
        String userChoice = request.getParameter("filter");
        // validate the date
        String dateTime = request.getParameter("datetime");
        // debug
        System.out.println("Choice : " + userChoice);
        System.out.println("Datetime : " + dateTime);
        try {            
            // try to parse
            String endDate = Utility.getEndDate(dateTime, "yyyy-MM-dd HH:mm:ss", -4320);
            if(userChoice.equals("year"))    {
                RequestDispatcher rd = request.getRequestDispatcher("YearResult.jsp");
                request.setAttribute("datetime", dateTime);    
                rd.forward(request, response);
            } else if(userChoice.equals("yeargender")) {
                RequestDispatcher rd = request.getRequestDispatcher("YearGenderResult.jsp");
                request.setAttribute("datetime", dateTime);    
                rd.forward(request, response);
            } else if(userChoice.equals("yeargenderschool"))   {
                RequestDispatcher rd = request.getRequestDispatcher("YearGenderSchoolResult.jsp");
                request.setAttribute("datetime", dateTime);    
                rd.forward(request, response);
            } else     {
                // invalid input
                RequestDispatcher rd = request.getRequestDispatcher("reportError.jsp");
                request.setAttribute("error", "You've entered an invalid input. Please select a filter.");    
                rd.forward(request, response);
            }            
        } catch (ParseException e)  {
            e.printStackTrace();
            // invalid date
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
