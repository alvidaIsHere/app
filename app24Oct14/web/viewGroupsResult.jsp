<%-- 
    Document   : viewGroupsResult
    Created on : Oct 20, 2014, 6:24:13 PM
    Author     : User
--%>
<%@page import="java.util.Arrays"%>
<%@page import="com.sloca.model.*"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container-fluid">
    <%
        Members m1 = new Members("Email1", "macAddr1");
        Members m2 = new Members(null, "macAddr2");
        Members m3 = new Members("Email3", "macAddr3");
        Members m4 = new Members(null, "macAddr4");

        Locations l1 = new Locations("LocId1", 5);
        Locations l2 = new Locations("LocId2", 5);
        Locations l3 = new Locations("LocId3", 5);

        Group g1 = new Group(new ArrayList<Members>(Arrays.asList(m1, m2)), new ArrayList<Locations>(Arrays.asList(l1, l2, l3)));
        Group g2 = new Group(new ArrayList<Members>(Arrays.asList(m3, m2, m4)), new ArrayList<Locations>(Arrays.asList(l2, l3)));

        ArrayList<Group> groups = new ArrayList<Group>(Arrays.asList(g1, g2));

    %>
    <p align="center" style="color: red">*Hardcoded result</p>

    <%        if (groups == null || groups.isEmpty()) {
    %>
    <p align="center" style="color:red"> No result found.</p>
    <%
    } else {
    %>
    <table class="table table-hover">
        <tr class="row">
            <th>No. </th>
            <th>Locations</th>
            <th>Members</th>
        </tr>
        <%
            int counter = 1;
            for (Group g : groups) {
                ArrayList<Members> memberList = g.getMemberList();
                ArrayList<Locations> locationList = g.getLocationList();

        %>
        <tr class="row">
            <td><%=counter%></td>
            <td>
                <ol>
                    <% for (Locations l : locationList) {%>
                    <li><%=l.getLocationID()%> : <%=l.getTimeSpent()%> min</li>
                        <%}%>
                </ol>
            </td>
            <td>
                <ol>
                    <%for (Members m : memberList) {
                            String email = m.getEmail();
                            if (m.getEmail() == null) {
                                email = "";
                            }
                    %>
                    <li><%=m.getMac_address()%> : <%=email%></li>
                        <%}%>
                </ol>
            </td>
        </tr>

        <% counter++;
            }%>
    </table>
    <p>Total number of users in SIS at (insertTimeHere) : 100 users</p>
    <p>Total number of groups discovered : <%=groups.size()%></p>
    <%
        }
    %>
    
</div>
