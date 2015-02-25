<%-- 
    Document   : bootstrapResult
    Created on : Sep 14, 2014, 10:10:52 PM
    Author     : Elvin
--%>

<%@page import="com.sloca.model.UploadErrorMessage"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% ArrayList<ArrayList<String>> errorMessages = (ArrayList<ArrayList<String>>) request.getAttribute("errorMsgs");%>
<html lang="en">
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "admin");
        String userName = (String) session.getAttribute("admin");
    %>
    <%@include file="protect.jsp"%>
    <head>
        <title>Bootstrap Result</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <div class='container-fluid'>
            <div class='row'>
                <div class='col-md-12' align='center'>
                    <h3>BootStrap Result</h3>
                    
                    <%

                        if (request.getAttribute("demographicsError") == null) {
                                out.println("demographics.csv uploaded without error");
                        } else {
                            ArrayList<UploadErrorMessage> errList = (ArrayList<UploadErrorMessage>) 
                                    request.getAttribute("demographicsError");

                    %>

                    <table border="1">
                        <tr>
                            <th> Line Number </th>
                            <th> Error </th>
                        </tr>

                        <%                 for (int i = 0; i < errList.size(); i++) {
                                UploadErrorMessage err = errList.get(i);
                                ArrayList<String> messages = err.getErrorMessages();
                        %>
                        <tr>
                            <td><%=err.getLineNumber()%></td>
                            <td>
                                <%
                                    for (int j = 0; j < messages.size(); j++) {
                                        out.print(messages.get(j) + " ");
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                            }
                        %>

                    </table>

                    <%
                        }
                    %>
                    
                    
                    
                    
                    
                    <%

                        if (request.getAttribute("locationError") == null) {
                                out.println("location.csv uploaded without error");
                        } else {
                            ArrayList<UploadErrorMessage> errList = (ArrayList<UploadErrorMessage>) 
                                    request.getAttribute("locationError");

                    %>

                    <table border="1">
                        <tr>
                            <th> Line Number </th>
                            <th> Error </th>
                        </tr>

                        <%                 for (int i = 0; i < errList.size(); i++) {
                                UploadErrorMessage err = errList.get(i);
                                ArrayList<String> messages = err.getErrorMessages();
                        %>
                        <tr>
                            <td><%=err.getLineNumber()%></td>
                            <td>
                                <%
                                    for (int j = 0; j < messages.size(); j++) {
                                        out.print(messages.get(j) + " ");
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                            }
                        %>

                    </table>

                    <%
                        }
                    %>
                    
                    
                    
                    
                    
                    
                    <%

                        if (request.getAttribute("locationLookupError") == null) {
                                out.println("location-lookup.csv uploaded without error");
                        } else {
                            ArrayList<UploadErrorMessage> errList = (ArrayList<UploadErrorMessage>) 
                                    request.getAttribute("locationLookupError");

                    %>

                    <table border="1">
                        <tr>
                            <th> Line Number </th>
                            <th> Error </th>
                        </tr>

                        <%                 for (int i = 0; i < errList.size(); i++) {
                                UploadErrorMessage err = errList.get(i);
                                ArrayList<String> messages = err.getErrorMessages();
                        %>
                        <tr>
                            <td><%=err.getLineNumber()%></td>
                            <td>
                                <%
                                    for (int j = 0; j < messages.size(); j++) {
                                        out.print(messages.get(j) + " ");
                                    }
                                %>
                            </td>
                        </tr>
                        <%
                            }
                        %>

                    </table>

                    <%
                        }
                    %>
                    

                </div>
            </div>
        </div>  
        
    </body>
</html>