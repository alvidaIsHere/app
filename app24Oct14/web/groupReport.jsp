<%-- 
    Document   : groupReport
    Created on : Oct 20, 2014, 3:05:46 PM
    Author     : Elvin, Jeremy
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Group Report</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script> 

    </head>
    <body>
        <%@include file="navBar.jsp" %>  
        <h2 align="center">View Group Location Reports</h2>
        <div class='container-fluid' align="center">            
            <div class='row' style="margin-top: 3%; 
                 margin-left: auto;
                 margin-right: auto; 
                 display: inline-block;">
                <div class='col-md-3'  style="
                     width: 50%;">
                    <div class="thumbnail" style="width: 250px; height: 250px">
                        <a href="groupTopKForm.jsp">
                            <img src="images/top_k_pop_places.png" alt="topkpopplaces"/>
                        </a>                         
                        <div class="caption">
                            <h4>Top k Places</h4>
                            <p>...</p>
                        </div>
                    </div>                 
                </div> 
                <div class='col-md-3' style="
                     width: 50%;">
                    <div class="thumbnail" style="width: 250px; height: 250px">
                        <a href="groupTopKNextPlaceForm.jsp">
                            <img src="images/top_k_next_place.png" alt="Top K Next Places"/>
                        </a>                        
                        <div class="caption">
                            <h4>Top K Next Places</h4>
                            <p>...</p>
                        </div>
                    </div>                 
                </div> 
            </div>

        </div>
    </body>
    <br />
    <%@include file="footer.jsp" %>
</html>
