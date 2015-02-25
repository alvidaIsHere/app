<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
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
        <h2 align="center">View Basic Location Reports</h2>
        <div class='container-fluid' style="margin-top: 3%">            
            <div class='row' align="center">
                <div class='col-md-3'>
                    <div class="thumbnail" style="width: 250px; height: 250px;">
                        <a href="viewTopKCompanions.jsp">
                            <img src="images/top_k_companion.png"  alt="topkcompanions"/>
                        </a>                        
                        <div class="caption">
                            <h4>Top k Companions</h4>
                            <p>...</p>
                        </div>
                    </div>                 
                </div> 
                <div class='col-md-3'>
                    <div class="thumbnail" style="width: 250px; height: 250px">
                        <a href="viewTopKPlaces.jsp">
                            <img src="images/top_k_pop_places.png" alt="topknextplaces"/>
                        </a> 
                        <div class="caption">
                            <h4>Top k Popular Places</h4>
                            <p>...</p>
                        </div>
                    </div>                                  
                </div> 
                <div class='col-md-3'>
                    <div class="thumbnail" style="width: 250px; height: 250px">
                        <a href="viewTopKNextPopularPlaces.jsp">
                            <img src="images/top_k_next_place.png" alt="topkpopplaces"/>
                        </a>                         
                        <div class="caption">
                            <h4>Top k Next Places</h4>
                            <p>...</p>
                        </div>
                    </div>                 
                </div> 
                <div class='col-md-3'>
                    <div class="thumbnail" style="width: 250px; height: 250px">
                        <a href="viewDemographics.jsp">
                            <img src="images/view_demo_breakdown.png" alt="demobreakdown"/>
                        </a>                        
                        <div class="caption">
                            <h4>Demographics Breakdown</h4>
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
