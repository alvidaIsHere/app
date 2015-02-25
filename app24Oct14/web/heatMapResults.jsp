<%-- 
    Document   : heatMapResults
    Created on : Oct 2, 2014, 2:20:45 PM
    Author     : alice
--%>

<%@page import="com.sloca.model.HeatMap"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>                    
<div id='container-fluid'>
    <table class='table' id='heatmap_results' align='center'>
        <tbody>
            <th style='text-align: center'>Location</th>
            <th style='text-align: center'>Students</th>
            <th style='text-align: center'>Density</th>
        </tbody>
        <% 
            // loop results
            ArrayList<HeatMap> results = (ArrayList<HeatMap>)request.getAttribute("heatmaps");
            for(HeatMap map : results)   {
                out.print("<tr>");
                out.print("<td style='text-align: center'>" + map.getSemantic_place() + "</td>");
                out.print("<td style='text-align: center'>" + map.getNum_people() + "</td>");
                out.print("<td style='text-align: center'>" + map.getCrowd_density() + "</td>");
                out.print("</tr>");
            }
        %>
    </table>
</div>
