<%-- 
    Document   : GroupTopKResult
    Created on : Oct 22, 2014, 13:20:11 PM
    Author     : Elvin, Jeremy
--%>

<%@page import="com.sloca.model.GroupPopularPlace"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container-fluid">
    <table class="table">
        <tr>
            <th style='text-align: center'>&nbsp Rank (for most popular places) &nbsp</th>
            <th style='text-align: center'>&nbsp Entries &nbsp</th>
        </tr>
        <%
        ArrayList<GroupPopularPlace> result = (ArrayList<GroupPopularPlace>)request.getAttribute("result");
        %>
        
        <%
        
        for(int i = 0; i < result.size(); i++){
            GroupPopularPlace g = result.get(i); 
            %>
            <tr>
                <td style='text-align: center'> <%=g.getRank()%> </td>
                <td style='text-align: center'> <%=g.getSemanticPlace()%></td>
            </tr>
            <%
        }
        
        %>
    </table>
</div>
