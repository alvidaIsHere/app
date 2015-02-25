<%-- 
    Document   : welcome
    Created on : Sep 2, 2014, 2:03:08 PM
    Author     : Chris, Elvin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "admin");
    %>
    <%@include file="protect.jsp"%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>
    </head>
    <body>

        <!-- Fixed NAV Bar -->
        <% if (session.getAttribute("errMess") == null) {%>
        <%@include file="adminNavBar.jsp"%>
        <%}%>


        <% if (session.getAttribute("errMess") != null) {
                out.println(session.getAttribute("errMess"));
                session.setAttribute("errMess", null);
            }%>

        <div class="container-fluid">
            <h2 align='center'>SLOCA Admin System</h2>
            <h4 align='center'>{ Welcome Page }</h4> 
            <p>Functions under admin are bootstrap and upload additional data</p>
            <p>To use location reports, please logout and login using student's credential</p>
        </div>

    </body>
</html>
