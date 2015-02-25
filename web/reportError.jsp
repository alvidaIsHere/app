<%-- 
    Document   : viewDemoError
    Created on : Oct 11, 2014, 11:21:47 PM
    Author     : alice
--%>
<% session.setAttribute("access", "student"); %>
<%@include file="protect.jsp"%>

<%
    //access refers to the one with access to this page, change the second parameter for "student" or "admin"
    
    // print error
    String error = (String)request.getAttribute("error");
    out.println("<p align='center'>" + error + "</p>");
%>
