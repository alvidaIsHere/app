/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.GroupsDAO;
import com.sloca.dao.Utility;
import com.sloca.model.GroupLocations;
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
public class toggleViewGroups extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            try {

                String datetime = request.getParameter("datetime");
                // try to parse
                String endDate = Utility.getEndDate(datetime, "yyyy-MM-dd HH:mm:ss", -15);

                // get total number of users
                int total_num_users = GroupsDAO.getTotalNumOfUsers(datetime, endDate);

                // obtain results
                ArrayList<GroupLocations> groups = GroupsDAO.getGroups(datetime, endDate);

                // set attribute to be forwarded to results page
                request.setAttribute("groups", groups);
                request.setAttribute("total_users", total_num_users);

                // forward
            } catch (ParseException e) {
                message += "Date error. Please select the date from the date icon on the right :)";

                e.printStackTrace();
            }
            request.setAttribute("errorMessage", message);
            RequestDispatcher rd = request.getRequestDispatcher("viewGroupsResult.jsp");
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
