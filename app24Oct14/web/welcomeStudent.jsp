<%-- 
    Document   : welcomeStudent
    Created on : Sep 2, 2014, 2:03:08 PM
    Author     : Chris, Elvin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
            session.setAttribute("access", "student");
            String userName = (String) session.getAttribute("user");
        %>
        <%@include file="protect.jsp"%>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>        
    </head>
    <body>
        <% if (session.getAttribute("errMess") == null) {%>

        <!-- Fixed NAV Bar -->
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
                        <li><a href='welcomeStudent.jsp' class='navbar-brand'>Home</a></li>
                        <li><a href='viewHeatMap.jsp' class='navbar-brand'>Heat Map</a></li>
                        <li><a href='basicReport.jsp' class='navbar-brand'>Location Reports</a></li>
                        <li><a href='viewGroups.jsp' class='navbar-brand'>View Groups</a></li>
                        <li><a href='groupReport.jsp' class='navbar-brand'>Group Reports</a></li>
                        <li><a href='logout.jsp' class='navbar-brand'>Logout</a></li>
                    </ul>
                </div>
                <p style="float:right;margin-top:-50px"class='navbar-brand'>Student: <%=userName%></p>
            </div>
        </div>
        <%}%>
        <header class='masthead' style='background-color: #428bca; margin-top: 50px'>
            <div class='container'>
                <div class='row'>
                    <div class='col-md-12' style='color: white'>
                        <h1 align='center'>SLOCA System</h1>
                        <p class='lead' align='center'>{Singapore Management University}</p>
                    </div> 
                </div>
            </div>
        </header>
        Welcome to the student's home page.
        <% if (session.getAttribute("errMess") != null) {
                out.println(session.getAttribute("errMess"));
                session.setAttribute("errMess", null);
            }%>
    </body>
</html>
