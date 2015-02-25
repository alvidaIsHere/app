<%-- 
    Document   : topkreport
    Created on : Sep 13, 2014, 3:46:54 AM
    Author     : baoyi.soh.2011
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<%
    ArrayList<ArrayList<String>> result = (ArrayList<ArrayList<String>>) request.getAttribute("displayResults");
    int selectedK = Integer.parseInt(request.getParameter("KValue"));
%>
<div class="container-fluid">
    <% if (result != null && result.size() > 0) {%>
    <div id="viewReport">
        <form id="TopkReport" method="POST" action="#">
            <div border='5px'>
            </div>
        </form>
    </div>

    <table class='table'>
        <tr>
            <th style='text-align: center'>&nbsp Rank (for most popular places) &nbsp</th>
            <th style='text-align: center'>&nbsp Entries &nbsp</th>
        </tr>

        <%
            for (int i = 0; i < result.size(); i++) {
                ArrayList<String> curr = result.get(i);
        %>

        <tr >
            <td style='text-align: center'><%=curr.get(0)%></td>
            <td style='text-align: center'>
                <%
                    for (int j = 1; j < curr.size(); j++) {
                        if (j == 1) {
                %>

                <%=curr.get(j)%>

                <%
                } else {
                %>

                <%=", " + curr.get(j)%>

                <%
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
        if (selectedK > result.size()) {
    %>
    Only top <%=result.size()%> results are found. 
    <%
        }
    %>

    <%
    } else {

    %>

    There are no records that fit the parameters.

    <%        }
    %>
</div>
