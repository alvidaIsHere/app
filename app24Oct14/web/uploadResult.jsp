<%-- 
    Document   : uploadResult
    Created on : Sep 14, 2014, 5:25:49 PM
    Author     : Jimmy
--%>

<%@page import="com.sloca.model.UploadErrorMessage"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "admin");
        String userName = (String) session.getAttribute("admin");
    %>
    <%@include file="protect.jsp"%>
    <head>
        <title>File Upload</title>
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
                    <h3>File Upload Result</h3>

                    <%

                        if (request.getAttribute("error") == null) {
                            
                            if(request.getAttribute("filename") != null){
                                String filename = (String) request.getAttribute("filename");
                                out.println(filename + " uploaded without error");
                            }
                            
                        } else {
                            ArrayList<UploadErrorMessage> errList = (ArrayList<UploadErrorMessage>) request.getAttribute("error");

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