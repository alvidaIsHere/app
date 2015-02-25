<%@page import="com.sloca.model.CompanionTimeSpent"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

        <%
            ArrayList<CompanionTimeSpent> resultList = (ArrayList<CompanionTimeSpent>) request.getAttribute("displayResults");
            int k = Integer.parseInt(request.getParameter("KValue"));

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
                        <th style='text-align: center'>&nbsp Companion &nbsp</th>
                        <th style='text-align: center'>&nbsp Mac-Address &nbsp</th>
                        <th style='text-align: center'>&nbsp Time Together &nbsp</th>
                    </tr>

                    <%
                        int count = 1;
                        for (int i = 0; i < resultList.size(); i++) {
                            CompanionTimeSpent c = resultList.get(i);
                    %>

                    <tr>
                        <%
                            if (i > 0) {
                                if (c.getTimeSpent() == resultList.get(i - 1).getTimeSpent()) {
                                    out.println("<td style='text-align: center'>");
                                    out.println(count);
                                    out.println("</td>");
                                } else {
                                    count++;
                                    out.println("<td style='text-align: center'>");
                                    out.println(count);
                                    out.println("</td>");
                                }
                            }

                        %>
                        <td><%out.println(c.getEmail());%></td>
                        <td><%out.println(c.getMac_Address());%></td>
                        <td><%out.println(c.getTimeSpent());%></td>
                    </tr>

                    <%
                        }
                    %>

                </table>

                <%
                    if (k > resultList.size()) {
                %>
                Only top <%=resultList.size()%> results are found. 
                <%
                    }
                %>

                <%
                } else {
                    out.println("<p style='text-align: center'>There are no records that fit the parameters.</p>");

                %>



                <%                    }
                %>

            </div>
        </div>