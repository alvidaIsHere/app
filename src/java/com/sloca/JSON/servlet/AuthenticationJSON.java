/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sloca.JSON.servlet;

import com.sloca.dao.LoginDAO;
import is203.JWTUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;

public class AuthenticationJSON extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     *
     *
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */

        String sharedSecret = "1234567890qwerty";

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        JSONObject obj = new JSONObject();

        boolean hasError = false;

        ArrayList<String> errList = new ArrayList<String>();

        if (username == null) {
            hasError = true;
            errList.add("missing username");
        } else if (username.isEmpty()) {
            hasError = true;
            errList.add("blank username");
        }

        if (password == null) {
            hasError = true;
            errList.add("missing password");
        } else if (password.isEmpty()) {
            hasError = true;
            errList.add("blank password");
        }

        if (hasError) {
            obj.put("status", "error");
            JSONArray errorArray = new JSONArray();

            Collections.sort(errList);

            for (String err : errList) {
                errorArray.add(err);
            }

            obj.put("messages", errorArray);
            out.println(obj);

        } else {

            if (LoginDAO.authenticate(username, password, "admin")) {

                String token = JWTUtility.sign(sharedSecret, username);

                obj.put("status", "success");
                obj.put("token", token);

                out.println(obj);

            } else if (LoginDAO.authenticate(username, password, "student")) {

                String token = JWTUtility.sign(sharedSecret, username);

                obj.put("status", "success");
                obj.put("token", token);

                out.println(obj);

            } else {

                errList.add("invalid username/password");

                obj.put("status", "error");
                obj.put("messages", errList);

                out.println(obj);

            }
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
