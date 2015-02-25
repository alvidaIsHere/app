<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "student");
    %>
    <%@include file="protect.jsp"%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="bootstrap/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/datetimepicker/js/moment.js"></script> 
        <script src="bootstrap/datetimepicker/js/bootstrap-datetimepicker.js"></script>  
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>        
    </head>
    <body>

        <%@include file="navBar.jsp" %>  
        <br />
        <h2 align='center'>SLOCA System</h2>
        <h4 align='center'>{ Basic Location Reports }</h4>    
        <div class='container-fluid' style="margin-top: 3%">
            <div align="center">
                <div class='row'>
                    <div class='col-md-3'>
                        <div class="thumbnail" style="width: 200px; height: 200px;">
                            <a href="viewTopKCompanions.jsp">
                                <img src="images/top_k_companion.png"  alt="topkcompanions"/>
                            </a>                        
                            <div class="caption">
                                <p>Top k Companions</p>
                            </div>
                        </div>

                        </br>
                    </div> 
                    <div class='col-md-3'>
                        <div class="thumbnail" style="width: 200px; height: 200px;">
                            <a href="viewTopKPlaces.jsp">
                                <img src="images/top_k_pop_places.png" alt="topknextplaces"/>
                            </a> 
                            <div class="caption">
                                <p>Top k Popular Places</p>
                            </div>
                        </div>  
                        </br>
                    </div>
                    <div class='col-md-3'>
                        <div class="thumbnail" style="width: 200px; height: 200px;">
                            <a href="viewTopKNextPopularPlaces.jsp">
                                <img src="images/top_k_next_place.png" alt="topkpopplaces"/>
                            </a>                         
                            <div class="caption">
                                <p>Top k Next Places</p>
                            </div>
                        </div> 
                        </br>
                    </div> 
                    <div class='col-md-3'>
                        <div class="thumbnail" style="width: 200px; height: 200px;">
                            <a href="viewDemographics.jsp">
                                <img src="images/view_demo_breakdown.png" alt="demobreakdown"/>
                            </a>                        
                            <div class="caption">
                                <p>Demographics Breakdown</p>
                            </div>
                        </div>
                    </div> 
                </div>

            </div>

    </body>
    <br />
    <%@include file="footer.jsp" %>
</html>
