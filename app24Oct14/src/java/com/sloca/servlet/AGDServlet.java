/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sloca.dao.GroupsDAO;
import com.sloca.dao.Utility;
import com.sloca.model.AGDstatus;
import com.sloca.model.GroupLocations;
import com.sloca.model.UserLocIntervals;
import com.sloca.model.UsersInLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alice
 */
public class AGDServlet extends HttpServlet {

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
        // get parameter inputs
        String dateTime = request.getParameter("datetime");        
        // parse to subtract 15 minutes
        try {
            String endDate = Utility.getEndDate(dateTime, "yyyy-MM-dd HH:mm:ss", -15);
            // invoke DAO to retrieve students in location
            //ArrayList<GroupLocations> users_in_location = GroupsDAO.getUsersInLocation(dateTime, endDate);
            
            AGDstatus results = GroupsDAO.getUsersInLocation(dateTime, endDate);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(results);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            
            
            // convert into user location intervals
            //ArrayList<UserLocIntervals> user_intervals = GroupsDAO.getUserLocationIntervals(users_in_location);
            
            // print in table form
            /*for(UserLocIntervals user : user_intervals)   {
                out.println("<table border = 1>");
                out.println("<tr><td>" + user.getMac_address() + "</td><td><table>" + user + "</table></td></tr>");
                out.println("</table>");
            }*/
            
            // test groupings
            //ArrayList<GroupLocations> group_locs = GroupsDAO.getGroups(user_intervals);
            // print grouploc size
            //System.out.println("GLOC: " + group_locs.size());
            
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
