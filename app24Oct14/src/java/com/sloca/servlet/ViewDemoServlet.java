/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sloca.servlet;

import com.google.gson.Gson;
import com.sloca.dao.FilterDAO;
import com.sloca.dao.Utility;
import com.sloca.model.YearBreakdown;
import com.sloca.model.YearFilter;
import com.sloca.model.YearGenderBreakdown;
import com.sloca.model.YearGenderFilter;
import java.io.IOException;
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
        String dateTime = request.getParameter("datetime");
        
        
        // change UI to use radio buttons will be better instaed (during UI iteration)
        String userChoice = request.getParameter("filter");
        // check datetiem format and input fields
        System.out.println("Date Time format: " + dateTime);
        System.out.println("User input" + userChoice);

        // convert datetime to -4320 minutes (past 3 days)
        String endDate = "";
        try {
            endDate = Utility.getEndDate(dateTime, "yyyy-MM-dd HH:mm:ss", -4320);
            // check result
            System.out.println("After subtracting 3 days: " + endDate);
            // success
        } catch (ParseException e)  {
            e.printStackTrace();
        }
        
        // check user input and execute methods accordingly
        if(userChoice.equals("year"))    {
            // test first check box
            System.out.println("Year entered!! :: ");
            // test input of dynamic date data for filter year function
            ArrayList<YearFilter> yearCount = FilterDAO.filterYear(dateTime, endDate);
            YearBreakdown breakdown = new YearBreakdown("success", yearCount);
            // convert to json object
            String json = new Gson().toJson(breakdown);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            System.out.println(json);
        }
        else if(userChoice.equals("yeargender"))   {
            // test second check box
            System.out.println("Year and gender entered!! :: ");
            ArrayList<YearFilter> yearGenderCount = FilterDAO.filterYearGender(dateTime, endDate);
            YearBreakdown breakdown2 = new YearBreakdown("success", yearGenderCount);
            String json2 = new Gson().toJson(breakdown2);
            response.getWriter().write(json2);
            //System.out.println(json2);
        }
        else if(userChoice.equals("yeargenderschool"))   {
            // test final check box
            System.out.println("Year, gender and school entered!! :: ");
            ArrayList<YearGenderFilter> yearGenderSchoolCount = FilterDAO.filterYearGenderSchool(dateTime, endDate);
            YearGenderBreakdown breakdown3 = new YearGenderBreakdown("success", yearGenderSchoolCount);
            String json3 = new Gson().toJson(breakdown3);
            response.getWriter().write(json3);
            //System.out.println(json3);
        }        
        
        /*
        System.out.println(dateTime);
        System.out.println("2010: " + FilterDAO.retrieveCountByYear("", "", "2010"));
        System.out.println("2011: " + FilterDAO.retrieveCountByYear("", "", "2011"));
        System.out.println("2012: " + FilterDAO.retrieveCountByYear("", "", "2012"));
        System.out.println("2013: " + FilterDAO.retrieveCountByYear("", "", "2013"));
        System.out.println("2014: " + FilterDAO.retrieveCountByYear("", "", "2014"));
        
        System.out.println("2010 Males: " + FilterDAO.retrieveCountByYearAndGender("", "", "2010", "m"));
        System.out.println("2010 Females: " + FilterDAO.retrieveCountByYearAndGender("", "", "2010", "f"));
        System.out.println("2011 Males: " + FilterDAO.retrieveCountByYearAndGender("", "", "2011", "m"));
        System.out.println("2011 Females: " + FilterDAO.retrieveCountByYearAndGender("", "", "2011", "f"));
        System.out.println("2012 Males: " + FilterDAO.retrieveCountByYearAndGender("", "", "2012", "m"));
        System.out.println("2012 Females: " + FilterDAO.retrieveCountByYearAndGender("", "", "2012", "f"));
        System.out.println("2013 Males: " + FilterDAO.retrieveCountByYearAndGender("", "", "2013", "m"));
        System.out.println("2013 Females: " + FilterDAO.retrieveCountByYearAndGender("", "", "2013", "f"));
        System.out.println("2014 Males: " + FilterDAO.retrieveCountByYearAndGender("", "", "2014", "m"));
        System.out.println("2014 Females: " + FilterDAO.retrieveCountByYearAndGender("", "", "2014", "f"));
        
        System.out.println("Males in 2010 Business: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "m", "business"));
        System.out.println("Females in 2010 Business: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "f", "business"));
        System.out.println("Males in 2010 SIS: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "m", "sis"));
        System.out.println("Females in 2010 SIS: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "f", "sis"));
        System.out.println("Males in 2010 Accountancy: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "m", "accountancy"));
        System.out.println("Females in 2010 Accountancy: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "f", "accountancy"));
        System.out.println("Males in 2010 Socsc: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "m", "socsc"));
        System.out.println("Females in 2010 Socsc: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "f", "socsc"));
        System.out.println("Males in 2010 Law: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "m", "law"));
        System.out.println("Females in 2010 Law: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "f", "law"));
        System.out.println("Males in 2010 Economics: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "m", "economics"));
        System.out.println("Females in 2010 Economics: " + FilterDAO.retrieveCountByYearGenderAndSchool("", "", "2010", "f", "economics"));
        */
                
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
