<%-- 
    Document   : GroupTopKResult
    Created on : Oct 22, 2014, 13:20:11 PM
    Author     : Elvin, Jeremy
--%>

<%@page import="java.util.HashMap"%>
<%@page import="com.sloca.model.GroupPopularPlace"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="container-fluid">
    <table class="table" align='center'>
        <tr>
            <th style='text-align: center'>&nbsp Rank (for most popular places) &nbsp</th>
            <th style='text-align: center'>&nbsp Entries &nbsp</th>
            <th style='text-align: center'>&nbsp Total Number of Groups &nbsp</th>
            <th style='text-align: center'>&nbsp Total Number of People &nbsp</th>
        </tr>
        
        <%
            ArrayList<GroupPopularPlace> result = (ArrayList<GroupPopularPlace>) request.getAttribute("result");
            Integer k = Integer.parseInt(request.getParameter("k"));

            HashMap<Integer, ArrayList<String>> popularMap = new HashMap<Integer, ArrayList<String>>();
            HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();

            // Key = Rank, Value = Number of People in the rank
            HashMap<Integer, Integer> userCountMap = new HashMap<Integer, Integer>();
            
            // Key = Rank, Value = Group Count
            HashMap<Integer, Integer> groupCountMap = new HashMap<Integer, Integer>();
            
            int count = 0;

            for (GroupPopularPlace gpl : result) {
                if (scoreMap.get(gpl.getCount()) == null) {
                    count++;
                    ArrayList<String> semanticList = new ArrayList<String>();
                    semanticList.add(gpl.getSemanticPlace());
                    scoreMap.put(gpl.getCount(), semanticList);
                    popularMap.put(count, semanticList);
                    userCountMap.put(count, gpl.getGroupMemberCount());
                    groupCountMap.put(count, gpl.getCount());
                } else {
                    ArrayList<String> semanticList = scoreMap.get(gpl.getCount());
                    semanticList.add(gpl.getSemanticPlace());
                    userCountMap.put(count, (userCountMap.get(count) + gpl.getGroupMemberCount()));
                    groupCountMap.put(count, groupCountMap.get(count) + gpl.getCount());
                }
            }

        %>

        <%  if (result != null && result.size() > 0) {
                for (int i = 0; i < count; i++) {   
                    ArrayList<String> list = popularMap.get(i + 1);
                    int numOfPeopleInRank = userCountMap.get(i + 1);
                    int numOfGroupInRank = groupCountMap.get(i + 1);
        %>
        <tr>
            <td style='text-align: center'> <%=i + 1%> </td>
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
            <td style='text-align: center'>
                <%=numOfGroupInRank%>
            </td>
            <td style='text-align: center'>
                <%=numOfPeopleInRank%>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        if (k > count) {
    %>
    Only top <%=count%> results are found. 
    <%
        }

    %>
    <%} else {
    %>

    There are no records that fit the parameters.

    <%
        }

    %>
</div>
