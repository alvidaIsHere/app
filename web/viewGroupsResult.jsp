<%@page import="java.util.Arrays"%>
<%@page import="com.sloca.model.*"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
    session.setAttribute("access", "student");
%>
<%@include file="protect.jsp"%>

<%
    String error = (String) request.getAttribute("errorMessage");
    if (error == null) {
%>
<p align="center" style="color: red">Invalid access, redirect to home page</p>
<%
    response.setHeader("Refresh", "0;url=welcomeStudent.jsp");

} else if (!error.equals("")) {
%>
<p align="center" style="color: red"><%=error%></p>
<%

} else {
// obtain results
    ArrayList<GroupLocations> groups_in_locations = (ArrayList<GroupLocations>) request.getAttribute("groups");
    // variable to store results
    int total_num_students = (Integer) request.getAttribute("total_users");
    int total_num_groups = groups_in_locations.size();
%>
<div class="container-fluid">
    <div class ='row'>
        <div class='col-md-6'>
            <h4>Total Number of Groups : <% out.println(total_num_groups); %></h4>
        </div>
        <div class='col-md-6'>
            <h4>Total Number of People : <% out.println(total_num_students); %></h4>
        </div>
    </div>
    <%if (groups_in_locations == null || groups_in_locations.size() == 0) {
    %>
    <p align="center" style="color: red">No group found</p>

    <%
    } else {
    %>
    <table class='table' id='heatmap_results' align='center'>
        <tbody>
        <th style='text-align: center'>Members</th>
        <th style='text-align: center'>Email</th>
        <th style='text-align: center'>Locations Visited</th>
        <th style='text-align: center'>Time Spent</th>
        </tbody>
        <%
            // loop and expand results
            for (GroupLocations groups : groups_in_locations) {
                out.println("<tr>");

                // get all members addresses
                String user_list = "";
                String email_list = "";
                ArrayList<UserLocationInterval> users = groups.getUsers();
                for (UserLocationInterval u : users) {
                    user_list += u.getMac_address() + "</br>";
                    email_list += u.getEmail() + "</br>";
                }
                out.println("<td style='text-align: center'>" + user_list + "</td>");
                out.println("<td style='text-align: center'>" + email_list + "</td>");

                // get all locations
                ArrayList<LocationTimeSpent> location_time = groups.getLocations();
                String locations_visited = "";
                String time_spent = "";
                for (LocationTimeSpent location : location_time) {
                    locations_visited += location.getLocation() + "<br />";
                    time_spent += location.getTime_spent() + "<br />";
                }
                out.print("<td style='text-align: center'>" + locations_visited + "</td>");
                out.print("<td style='text-align: center'>" + time_spent + "</td>");

                out.println("</tr>");
            }

        %>
    </table>  
    <%}
        }%>

</div>

