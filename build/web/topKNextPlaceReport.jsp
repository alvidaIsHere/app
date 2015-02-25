<%@page import="com.sloca.model.TopKNextPlaceResult"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<!DOCTYPE html>
<div id='nextpopzplace_result'>
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "student");
    %>
    <%@include file="protect.jsp"%>

    <style>
        divborder {
            border: 2px solid #a1a1a1;
            padding: 10px 40px; 
            background: #dddddd;
            width: 300px;
            border-radius: 300px;
            height: 300px;
        }
    </style>


    <%
        DecimalFormat formatter = new DecimalFormat("#.00");
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

        int totalUsersQueried = (Integer) request.getAttribute("totalUsersQueried");
        int allUsersVisitedOtherPlace = (Integer) request.getAttribute("allUsersVisitedOtherPlace");

        HashMap<Integer, ArrayList<TopKNextPlaceResult>> result = (HashMap<Integer, ArrayList<TopKNextPlaceResult>>) request.getAttribute("result");

        int selectedK = Integer.parseInt(request.getParameter("kValue"));
        String selectedPlace = request.getParameter("semanticPlace");
        String atTime = request.getParameter("atTime");
    %>
    <h3 align="center">Top k next places result on <b><%=atTime%></b> at <b><%=selectedPlace%></b> </h3> </br>
    <%
        if (result.size() == 0) {
    %>
    <p align="center" style="color: red">No result found on <b><%=atTime%></b> at <b><%=selectedPlace%></b></p>
    <%
    } else {
    %>
    <table class="table table-hover table-bordered">
        <tr>
            <th style='text-align: center'>&nbsp Rank &nbsp</th>
            <th style='text-align: center'>&nbsp Semantic Place &nbsp</th>
            <th style='text-align: center'>&nbsp User Visit Count &nbsp</th>
            <th style='text-align: center'>&nbsp Percentage of User Visit &nbsp</th>
        </tr>
        <%
            for (Integer rank : result.keySet()) {
                if (rank <= selectedK) {
                    ArrayList<TopKNextPlaceResult> results = result.get(rank);
                    int rowSpan = results.size();
        %>

        <tr>
            <td style='text-align: center; vertical-align: centers' rowSpan="<%=rowSpan%>"><%=rank%></td>
            <%
                for (TopKNextPlaceResult r : results) {
                    double percentage = ((double) r.getCount() / (double) totalUsersQueried) * 100;
            %>

            <td style='text-align: center'>
                <%=r.getSemanticPlace()%>
            </td>

            <td style='text-align: center'>
                <%=r.getCount()%>
            </td>
            <td style='text-align: center'>
                <%=formatter.format(percentage)%>%
            </td>
        </tr>
        <%
                    }
                }
            }%>
    </table>

    <%
        if (selectedK > result.size()) {
    %>
    <p align="center" style="color: red">Only top <%=result.size()%> result(s) found.</p>
    <%
            }
        }
    %>
    <p>total number of users in the semantic place being queried = <%=totalUsersQueried%></p>
    <p>number of users who visited another place (exclude those left the place but have not visited another place) = <%=allUsersVisitedOtherPlace%></p>
    <%
        }
    %>

</div>
