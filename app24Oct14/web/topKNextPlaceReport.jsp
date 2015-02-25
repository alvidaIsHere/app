<%@page import="com.sloca.model.TopKNextPlaceResult"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<!DOCTYPE html>
<div id='nextpopzplace_result'>


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
        if (!error.equals("")) {
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

        if (result.size() == 0) {
    %>
    <p align="center" style="color: red">No result found on <b><%=atTime%></b> at <b><%=selectedPlace%></b></p>
    <%
    } else {
    %>


    <table class="table">
        <tr>
            <th style='text-align: center'>&nbsp Rank &nbsp</th>
            <th style='text-align: center'>&nbsp Semantic Place &nbsp</th>
            <th style='text-align: center'>&nbsp User Visit Count &nbsp</th>
            <th style='text-align: center'>&nbsp Percentage of User Visit &nbsp</th>
        </tr>

        <%            for (Integer rank : result.keySet()) {
                if (rank <= selectedK) {
                    ArrayList<TopKNextPlaceResult> results = result.get(rank);

        %>


        <%            for (TopKNextPlaceResult r : results) {
                double percentage = ((double) r.getCount() / (double) allUsersVisitedOtherPlace) * 100;

        %>
        <tr>
            <td style='text-align: center' ><%=rank%></td>
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
        <%}
                }
            }%>



    </table>
    <p>total number of users in the semantic place being queried = <%=totalUsersQueried%></p>
    <p>number of users who visited another place (excluding the users who have stayed at the same place or had left the place but have not visited another place) = <%=allUsersVisitedOtherPlace%></p>

    <%
        if (selectedK > result.size()) {
    %>
    Only top <%=result.size()%> results are found. 
    <%
    } else {

    %>

    There are no records that fit the parameters.

    <%            }
            }
        }
    %>

</div>
