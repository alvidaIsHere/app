/* 
 Document   : loginServlet.java
 Created on : Sep 2, 2014, 7:00:00 PM
 Author     : Elvin, Christina
 */
package com.sloca.servlet;

import com.sloca.dao.LoginDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@WebServlet(name = "loginServlet", urlPatterns = {"/login.do"})
public class loginServlet extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        //set the parameters here
        String username = request.getParameter("username");
        String password = request.getParameter("password");
   
        HttpSession session = request.getSession();
        session = request.getSession(true);
        
        if (username == null || username.equals("") || password == null || password.equals("")) {
            //get the form page here
            getServletContext().getRequestDispatcher("/login.jsp").include(request,
                    response);
            out.println("<center><p style=\"color:red\">Error In Login</p></center>");

            //authenticate admin first
        } else if (LoginDAO.authenticate(username, password, "admin")) {
            session.setAttribute("admin",username);
            response.sendRedirect("admin");
            //authenticate student if not admin
        } else if (LoginDAO.authenticate(username, password, "student")) {
            session.setAttribute("user",username);
            response.sendRedirect("welcomeStudent.jsp");
            //cannot login
        } else {

            getServletContext().getRequestDispatcher("/login.jsp").include(request,
                    response);
            out.println("<center><p style=\"color:red\">User or password mismatch</p></center>");
            //print out input username and password in clear text for debugging purpose
//            out.println("Username = " + username);
//            out.println("Pass = " + password);

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
