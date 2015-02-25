/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.servlet;

import com.sloca.dao.BreakdownDAO;
import com.sloca.dao.DateUtilityDAO;
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
 * @author alice
 */
public class ViewDemoServlet extends HttpServlet {

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
        String date = request.getParameter("date");
        String order = request.getParameter("order");
        String[] order_list = order.split(",");
        String message = "";
        ArrayList<Object> breakdown = null;
        int totalStudent = 0;

        if (date == null || order == null) {
            message += "Invalid input";
        } else {
            if (order.equals("0")) {
                message += "Filter error. Please select a filter from the dropdown. \n";
            }
            try {
                DateUtilityDAO.getEndDate(date, "yyyy-MM-dd HH:mm:ss", 9);
            } catch (ParseException e) {
                message += "Date error. Please select the date from the date icon on the right :)\n";

                e.printStackTrace();
            }

        }
        if (message.equals("")) {

            // determine order of user selection
            ArrayList<String> sort_order = new ArrayList<>();
            if (order_list != null) {
                // check for user input
                for (int i = 0; i < order_list.length; i++) {
                    String user_input = order_list[i];

                    // additional kiasu validation
                    if (user_input.equalsIgnoreCase("year") || user_input.equalsIgnoreCase("gender") || user_input.equalsIgnoreCase("school")) {
                        sort_order.add(order_list[i]);
                    } else {

                        break;
                    }
                }
            } else {
                // the user only selected a single input
                // additional kiasu validation
                String user_input = order;
                if (user_input.equalsIgnoreCase("year") || user_input.equalsIgnoreCase("gender") || user_input.equalsIgnoreCase("school")) {
                    sort_order.add(user_input);
                }
            }

            // process data
            breakdown = BreakdownDAO.processBreakdown(date, sort_order);
            totalStudent = BreakdownDAO.getStudentsWithinPeriod(date).size();
        }
        // push results to a result page
        RequestDispatcher rd = request.getRequestDispatcher("BreakdownResults.jsp");
        request.setAttribute("breakdown", breakdown);
        request.setAttribute("totalStudent", totalStudent);
        request.setAttribute("errorMessage", message);
        request.setAttribute("orderList", order_list);
        rd.forward(request, response);
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
