/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.GroupReportingDAO;
import com.sloca.model.GroupsFound;
import com.sloca.model.TopKNextPlaceResult;
import java.io.IOException;
import java.sql.SQLException;
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
 * @author Elvin, Chris
 */
@WebServlet(name = "GroupTopKNextPlaces", urlPatterns = {"/GroupTopKNextPlaces.do"})
public class GroupTopKNextPlacesServlet extends HttpServlet {

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
        HashMap<Integer, ArrayList<TopKNextPlaceResult>> topKNextPlacesResult = new HashMap<>();
        HashMap<GroupsFound, String> nextPlaces = new HashMap<>();
        // getting top K value
        int kValue = Integer.parseInt(request.getParameter("kValue"));
//        System.out.println(kValue);
        // getting selected time
        String atTime = request.getParameter("atTime");
//        System.out.println(atTime);
        // getting selected place
        String semanticPlace = request.getParameter("semanticPlace");
//        System.out.println(semanticPlace);
        String message = "";

        int totalGroupsQueried = 0;
        int allGroupsVisitedOtherPlaces = 0;

        try {

            ArrayList<GroupsFound> groups = GroupReportingDAO.getGroupsLocatedAt(semanticPlace, atTime);
            //total number of groups in the semantic place being queried
            totalGroupsQueried = groups.size();

            nextPlaces = GroupReportingDAO.getLastVisitForGroups(groups, semanticPlace, atTime); //HashMap<GroupsFound, lastVisitSemanticLocation>

            //number of groups who visited another place (excluding the group who have stayed at the same place or had left the place but have not visited another place)
            allGroupsVisitedOtherPlaces = nextPlaces.size();

            topKNextPlacesResult = GroupReportingDAO.getTopKNextPlaces(nextPlaces.values(), kValue);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
            message += "Date error. Please select the date from the date icon on the right :)";
        } catch (SQLException ex) {
            ex.printStackTrace();
            message += "Connection to database error! ";
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            message += "NullPointerException O.O! Please inform system admin ";
        }
        request.setAttribute("errorMessage", message);
        request.setAttribute("totalGroupsQueried", totalGroupsQueried);
        request.setAttribute("allGroupsVisitedOtherPlace", allGroupsVisitedOtherPlaces);
        request.setAttribute("result", topKNextPlacesResult);
        RequestDispatcher dispatcher = request.getRequestDispatcher("groupTopKNextPlaceResult.jsp");
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
        return "View Top K Next Places Servlet";
    }// </editor-fold>

}
