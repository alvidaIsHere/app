<%-- 
    Document   : topkreport
    Created on : Sep 13, 2014, 3:46:54 AM
    Author     : baoyi.soh.2011
--%>

<%@page import="com.sloca.model.PopularPlace"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<div id="topkplaces_result">
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "student");
    %>
    <%@include file="protect.jsp"%>
    <%
        ArrayList<PopularPlace> result = (ArrayList<PopularPlace>) request.getAttribute("displayResults");
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

        int selectedK = Integer.parseInt(request.getParameter("KValue"));

        HashMap<Integer, ArrayList<String>> popularMap = new HashMap<Integer, ArrayList<String>>();
        HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();

        HashMap<Integer, Integer> countMap = new HashMap<Integer, Integer>();//rank, count

        int count = 0;

        for (PopularPlace p : result) {
            if (scoreMap.get(p.getCount()) == null) {
                count++;
                ArrayList<String> semanticList = new ArrayList<String>();
                semanticList.add(p.getSemanticPlace());
                scoreMap.put(p.getCount(), semanticList);
                popularMap.put(count, semanticList);
                countMap.put(count, p.getCount());
            } else {
                ArrayList<String> semanticList = scoreMap.get(p.getCount());
                semanticList.add(p.getSemanticPlace());
            }
        }

    %>

    <div class="container-fluid">
        <% if (result != null && result.size() > 0) {%>


        <table class='table'>
            <tr>
                <th style='text-align: center'>&nbsp Rank (for most popular places) &nbsp</th>
                <th style='text-align: center'>&nbsp Entries &nbsp</th>
                <th style='text-align: center'>&nbsp Count &nbsp</th>
            </tr>

            <%
                for (int i = 0; i < count; i++) {
                    ArrayList<String> list = popularMap.get(i + 1);
            %>

            <tr >
                <td style='text-align: center'><%=i + 1%></td>
                <td style='text-align: center'>
                    <%
                        for (int j = 0; j < list.size(); j++) {
                            if (j == 0) {
                    %>

                    <%=list.get(j)%>

                    <%
                    } else {
                    %>

                    <%=", " + list.get(j)%>

                    <%
                            }
                        }
                    %>
                </td>
                <td>
                    <%=countMap.get(i + 1)%>
                </td>
            </tr>

            <%
                }
            %>

        </table>

        <%
            if (selectedK > count) {
        %>
        Only top <%=count%> results are found. 
        <%
            }
        %>

        <%
        } else {

        %>

        <p align="center" style="color: red">No result found</p>

        <%                    }
            }
        %>
    </div>
</div>