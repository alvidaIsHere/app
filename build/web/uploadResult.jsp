<%-- 
    Document   : uploadResult
    Created on : Sep 14, 2014, 5:25:49 PM
    Author     : Jimmy
--%>

<%@page import="com.sloca.model.UploadResult"%>
<%@page import="com.sloca.model.UploadErrorMessage"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "admin");
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

        <div class='container-fluid'>
            <div class='row'>
                <div class='col-md-12' align='center'>

                    <%
                        ArrayList<UploadResult> uploadResult = new ArrayList<UploadResult>();

                        uploadResult = (ArrayList<UploadResult>) request.getAttribute("uploadInfo");

                        if (uploadResult == null) {
                    %>
                    <p align="center" style="color: red">Invalid access, redirect to home page</p>
                    <%
                        response.setHeader("Refresh", "2;url=welcomeAdmin.jsp");

                    } else {
                    %>
                    <h3>File Upload Result</h3>


                    <table border="1">
                        <tr>
                            <th>File</th>
                            <th>Records Loaded</th>
                        </tr>

                        <%
                            for (int i = 0; i < uploadResult.size(); i++) {
                                UploadResult result = uploadResult.get(i);
                        %>
                        <tr>
                            <td><%=result.getFileName()%></td>
                            <td align="center"><%=result.getUploadCount()%></td>
                        </tr>
                        <%
                            }
                        %>

                    </table>

                    <%
                        boolean noError = true;

                        for (int i = 0; i < uploadResult.size(); i++) {
                            UploadResult result = uploadResult.get(i);

                            ArrayList<UploadErrorMessage> errMessages = result.getErrorList();

                            if (errMessages.size() > 0) {
                                noError = false;
                    %>
                    <br>
                    <table border="1">
                        <tr>
                            <td colspan="2"><%=result.getFileName()%> Error Message</td>
                        </tr>
                        <tr>
                            <th>Line Number</th>
                            <th>Error Message</th>
                        </tr>
                        <%
                            for (int j = 0; j < errMessages.size(); j++) {
                                UploadErrorMessage lineMessage = errMessages.get(j);
                                ArrayList<String> lineErrMessages = lineMessage.getErrorMessages();
                        %>
                        <tr>
                            <td><%=lineMessage.getLineNumber()%></td>
                            <td>
                                <%
                                    for (int k = 0; k < lineErrMessages.size(); k++) {
                                        if (k == 0) {
                                            out.print(lineErrMessages.get(k));
                                        } else {
                                            out.print(", " + lineErrMessages.get(k));
                                        }
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
                            }

                            if (noError) {
                                out.println("Upload success");
                            }
                        }
                    %>    

                </div>
            </div>
        </div>  

    </body>
</html>