<%-- 
    Document   : logout
    Created on : Sep 9, 2014, 2:53:47 PM
    Author     : baoyi.soh.2011, Elvin
--%>

<%@page import="java.util.*" %>

<html>
    <header>
        <meta http-equiv="refresh" content="2;URL='login.jsp'" />


    </header>
    <body>
        <p>Logged out. You will be redirect to login page in 2 seconds</p>
        <%
            session.invalidate();
        %>
    </body>
</html>
