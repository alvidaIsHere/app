<%-- 
    Document   : adminNavBar
    Created on : Sep 14, 2014, 7:17:47 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class='navbar navbar-default navbar-fixed-top' role='navigation'>
            <div class='container-fluid'>
                <div class='nav-header'>
                    <button type='button' class='navbar-toggle' data-toggle='collapse' data-target=".navbar-collapse">
                        <span class='icon-bar'></span>
                        <span class='icon-bar'></span>
                        <span class='icon-bar'></span>
                    </button>
                </div>
                <div class='navbar-collapse collapse'>
                    <ul class='nav navbar-nav'>                        
                        <li><a href='welcomeAdmin.jsp' class='navbar-brand'>Home</a></li>
                        <li><a href='fileUpload.jsp' class='navbar-brand'>Upload File</a></li>
                        <li><a href='bootstrap.jsp' class='navbar-brand'>Bootstrap System</a></li>
                        <li><a href='logout.jsp' class='navbar-brand'>Logout</a></li>
                    </ul>
                </div>

                <p style="float:right;margin-top:-50px"class='navbar-brand'>Admin: <%=userName%></p>

            </div>
        </div>