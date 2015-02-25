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
    <br />
    <body>
        <% if (session.getAttribute("errMess") == null) {%>
        <%@include file="navBar.jsp" %> 
        <%}%>

        <% if (session.getAttribute("errMess") != null) {
                out.println(session.getAttribute("errMess"));
                session.setAttribute("errMess", null);
            }%>
        <h2 align='center'>SLOCA System</h2>
        <h4 align='center'>{ Overview }</h4>
        <ol>
            <li><strong>Heatmap</strong> <br> 

                <ul>
                    <li>
                        The crowd density of any floor of the SIS building on any given day and time.<br><br>
                    </li>
                </ul>
            </li>
            <li><strong>Location Reports</strong>
                <br>
                <ul>
                    <li>
                        The breakdown statistics of Year 2010/2011/2012/2013/2014 students, genders, and schools.
                    </li>
                    <li>
                        The advance breakdown statistics of students by a combination of your desired attributes and their order.<br>(E.g School and Gender only)
                    </li>
                    <li>
                        The top K popular places in SIS (k ranges from 1 to 10)
                    </li>
                    <li>
                        The top K people who have been in the same location as a specified person (K ranges from 1 to 10) 
                    </li>
                    <li>
                        The top K next places that users visit after visiting a particular place 
                        <br><br>
                    </li>
                </ul>
            </li>
            <li><strong>Automatic Group Detection</strong>
                <ul>
                    <li>
                        The list of groups (location of group and composition) at a particular timing.<br><br>
                    </li>       
                </ul>
            </li>
            <li><strong>Group-aware Location Reports</strong></li>
            <ul>
                <li>
                    The top K popular places for groups (of size >= 2)
                </li>
                <li>
                    The top K next places that groups (of size >= 2) visit next after visiting a particular place. 
                </li>
            </ul>
        </ol>

    </body>
</html>
