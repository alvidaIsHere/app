/* 
 Document   : login
 Created on : Sep 2, 2014, 7:00:48 PM
 Author     : Elvin, Christina
 */
package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginDAO {

    /**
     * method to authenticate user
     *
     * @param username the username
     * @param password the password
     * @param userType the type of user (admin or student)
     * @return true if username matches with password and userType in the
     * database
     */
    public static boolean authenticate(String username, String password, String userType) {
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "";
        //to differentiate if the authentication is for admin or for student
        if (userType.equalsIgnoreCase("admin")) {
            query = "SELECT * FROM admin WHERE admin_username='" + username + "' AND password='" + password + "'";
        } else {
            query = "SELECT * FROM student WHERE email LIKE '" + username + "%' and password = '" + password + "'";
        }
        try {
            //cf = new ConnectionFactory(); //no need to initialize for static method
            connection = ConnectionFactory.getConnection();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            if (userType.equalsIgnoreCase("admin")) {
                while (rs.next()) {
                    String usr = rs.getString("admin_username");
                    String pwd = rs.getString("password");
                    if (username.equals(usr) && password.equals(pwd)) {
                        return true;
                    }
                }
            } else {
                while (rs.next()) {
                    String email = rs.getString("email");
                    String pwd = rs.getString("password");
                    String usr = email.split("@")[0];
                    if (username.equals(usr) && password.equals(pwd)) {
                        return true;
                    }
                }
            }
            rs.close();

        } catch (SQLException e) {
            Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

}
