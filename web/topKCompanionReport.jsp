<%@page import="com.sloca.model.CompanionTimeSpent"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

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
    response.setHeader("Refresh", "2;url=welcomeStudent.jsp");

} else if (!error.equals("")) {
%>
<p align="center" style="color: red"><%=error%></p>
<%

} else {
    ArrayList<CompanionTimeSpent> resultList = (ArrayList<CompanionTimeSpent>) request.getAttribute("displayResults");
    int k = Integer.parseInt((String) request.getAttribute("KValue"));

    int rankCount = 0;
    // HashMap for Rank to TimeSpent
    HashMap<Integer, Integer> timeSpentMap = new HashMap<Integer, Integer>();

    for (int i = 0; i < resultList.size(); i++) {
        CompanionTimeSpent cts = resultList.get(i);
        String email = cts.getEmail();
        String mac = cts.getMac_Address();
        int timeSpent = cts.getTimeSpent();
        int rank = cts.getRank();

        if (timeSpentMap.get(rank) == null) {
            rankCount++;
            timeSpentMap.put(rankCount, timeSpent);
        }
    }
%>
<div class="field_container">

    <div class="col-md-12">
        <% if (resultList != null && resultList.size() > 0) {%>
        <div id="viewReport">
            <form id="TopkCompanionReport" method="POST" action="#">
                <div border="5px">
                </div>
            </form>
        </div>

        <table class="table">
            <tr>
                <th style='text-align: center'>&nbsp Rank &nbsp</th>
                <th style='text-align: center'>&nbsp Mac-Address &nbsp</th>
                <th style='text-align: center'>&nbsp Companion &nbsp</th>
                <th style='text-align: center'>&nbsp Time Together &nbsp</th>
            </tr>

            <%
                int prevRank = 0;

                for (int i = 0; i < resultList.size(); i++) {
                    CompanionTimeSpent companion = resultList.get(i);
                    int rank = companion.getRank();
                    int timeSpent = companion.getTimeSpent();
                    String mac = companion.getMac_Address();
                    String email = companion.getEmail();
//                    ArrayList<String> listOfMac = macMap.get(i);
//                    ArrayList<String> listOfEmail = emailMap.get(i);
//                    int timeSpent = timeSpentMap.get(i);

            %>

            <tr>
                <%                    if (rank > prevRank) {
                        prevRank = rank;
                %>
                <td style='text-align: center'><%=rank%></td>
                <td style='text-align: center'><%=mac%></td>
                <td style='text-align: center'><%=email%></td>
                <td style='text-align: center'><%=timeSpent%></td>
                <%
                } else {
                %>
                <td style='text-align: center'>&nbsp</td>
                <td style='text-align: center'><%=mac%></td>
                <td style='text-align: center'><%=email%></td>
                <td style='text-align: center'>&nbsp</td>
                <%
                    }
                %>
            </tr>

            <%
                }

            %>
        </table>
        <%            if (k > rankCount) {
        %>
        <p>Only top <%=rankCount%> results are found.</p> 
        <%
                    }

                } else {
                    out.println("<p style='text-align: center'>There are no records that fit the parameters.</p>");

                }
            }
        %>

    </div>
</div>