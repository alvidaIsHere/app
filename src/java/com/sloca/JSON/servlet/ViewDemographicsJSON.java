/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sloca.dao.BreakdownDAO;
import com.sloca.dao.DateUtilityDAO;
import com.sloca.model.JSONMessage;
import is203.JWTException;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class ViewDemographicsJSON extends HttpServlet {

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {

            // obtain user inputs
            String date = request.getParameter("date");
            String order = request.getParameter("order");
            String token = request.getParameter("token");

            // initialise gson builder
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // variable to store messages
            ArrayList<String> messages = new ArrayList<>();

            // array list to store sort order of order objects
            ArrayList<String> sort_order = new ArrayList<>();

            // boolean checkers
            boolean hasError = false;
            boolean continueChecking = true;

            // check for empty date            
            if (date != null) {
                date = date.replace('T', ' ');
            }

            // check for valid token
            if (token == null) {
                hasError = true;
                continueChecking = false;
                messages.add(("missing token"));
            } else if (token.isEmpty()) {
                hasError = true;
                continueChecking = false;
                messages.add("blank token");
            } else {
                try {
                    if (JWTUtility.verify(token, "1234567890qwerty") == null) {
                        hasError = true;
                        continueChecking = false;
                        messages.add("invalid token");
                    } else {
                        String username = JWTUtility.verify(token, "1234567890qwerty");
                        if (!username.equals("admin")) {
                            hasError = true;
                            continueChecking = false;
                            messages.add("invalid token");
                        }
                    }
                } catch (JWTException e) {
                    e.printStackTrace();
                    hasError = true;
                    continueChecking = false;
                    messages.add("invalid token");
                }
            }

            // check for other empty params
            if (order == null) {
                hasError = true;
                continueChecking = false;
                messages.add("missing order");
            } else if (order.isEmpty()) {
                hasError = true;
                continueChecking = false;
                messages.add("blank order");
            }

            // check for empty date            
            if (date == null) {
                hasError = true;
                continueChecking = false;
                messages.add("missing date");
            } else if (date.isEmpty()) {
                hasError = true;
                continueChecking = false;
                messages.add("blank date");
            }

            // if continue checking is a 'green light'
            if (continueChecking) {

                try {
                    // try to parse date
                    DateUtilityDAO.getEndDate(date, "yyyy-MM-dd HH:mm:ss", 9);
                } catch (ParseException e) {
                    hasError = true;
                    messages.add(("invalid date"));
                    e.printStackTrace();
                }

                // if order is not empty, split it
                String[] order_list = order.split(",");

                // check for valid order param range (1 to 3)
                if (order_list.length >= 1 && order_list.length <= 3) {

                    // check for single input
                    if (order_list.length == 1) {
                        // this means that the user only entered 1 input; thus validate
                        if (order.equals("year") || order.equals("gender") || order.equals("school")) {
                            sort_order.add(order);
                        } else {
                            hasError = true;
                            messages.add("invalid order");
                        }
                    } else {

                        // check for duplicate order year,year,year e.g.
                        HashMap<String, Boolean> order_validator = new HashMap<>();

                        for (int i = 0; i < order_list.length; i++) {
                            // check for user input type of year/date/gender
                            String user_input = order_list[i];

                            // validate user input fields 
                            if (user_input.equals("year") || user_input.equals("gender") || user_input.equals("school")) {
                                // check if order is duplicated
                                if (order_validator.get(user_input) == null) {
                                    order_validator.put(user_input, true);
                                } else {
                                    hasError = true;
                                    messages.add("invalid order");
                                    break;
                                }
                                sort_order.add(order_list[i]);
                            } else {
                                hasError = true;
                                messages.add("invalid order");
                                break;
                            }
                        }
                    }
                } else {
                    hasError = true;
                    messages.add("invalid order");
                }
            }

            // sort error list
            Collections.sort(messages);

            // json message output
            JSONMessage results = null;

            // check for overall error status
            if (hasError) {
                // add error messages
                results = new JSONMessage("error", messages, null);
            } else {
                // process data
                ArrayList<Object> breakdown = BreakdownDAO.processBreakdown(date, sort_order);
                results = new JSONMessage("success", null, breakdown);
            }

            // write json output
            String json = gson.toJson(results);
            // finally output results
            response.getWriter().write(json);
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
