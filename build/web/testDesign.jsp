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
        <style>
            .thumbnail {
                position:relative;
                overflow:hidden;
            }

            .caption {
                position:absolute;
                top:0;
                right:0;
                background:rgba(30, 20, 30, 0.10);
                width:100%;
                height:100%;
                padding:2%;
                display: none;
                text-align:center;
                color:black !important;
                z-index:2;
            }
            div { border: 1px solid; border-radius: 1px; }
          
        </style>
        <%@include file="navBar.jsp" %>  
        <h2 align="center">View Basic Location Reports</h2>
        <div class='container-fluid' style="margin-top: 3%" align="center">
            <div class="row">
                <div class="col-md-4" >
                </div>
                <a href="#">
                <div class="col-md-2" >            
                    <div class="thumbnail" style="width: 100px; height: 100px;">
                        <div class="caption">
                            <p>short thumbnail description</p>
                        </div>
                        <img style="width: 100px; height: 100px;" src="images/top_k_companion.png" alt="...">
                    </div>
                    <h4>Top K Companion</h4>
                </div>
                </a>
                <a href="#">
                <div class="col-md-2">            
                    <div class="thumbnail" style="width: 100px; height: 100px;">
                        <div class="caption">
                            <p>short thumbnail description</p>
                        </div>
                        <img style="width: 100px; height: 100px;" src="images/top_k_companion.png" alt="...">
                    </div>
                    <h4>Top K Companion</h4>
                </div>
                </a>
            </div>
            <div class="row">
                <div class="col-md-4" >
                </div>
                <a href="#">
                <div class="col-md-2" >            
                    <div class="thumbnail" style="width: 100px; height: 100px;">
                        <div class="caption">
                            <p>short thumbnail description</p>
                        </div>
                        <img style="width: 100px; height: 100px;" src="images/top_k_companion.png" alt="...">
                    </div>
                    <h4>Top K Companion</h4>
                </div>
                </a>
                <a href="#">
                <div class="col-md-2">            
                    <div class="thumbnail" style="width: 100px; height: 100px;">
                        <div class="caption">
                            <p>short thumbnail description</p>
                        </div>
                        <img style="width: 100px; height: 100px;" src="images/top_k_companion.png" alt="...">
                    </div>
                    <h4>Top K Companion</h4>
                </div>
                </a>
            </div>
            
        </div>

        <!--        <div class='container-fluid' style="margin-top: 3%">            
                    <div class='row'>
                        <div class='col-md-3'>
                            <div class="thumbnail" >
                                <div class="caption">
        
        
                                    <h4>Top k Companions</h4>
                                    <p>...</p>
                                    <p><a href="" class="label label-danger" rel="tooltip" title="Zoom">Zoom</a>
                                        <a href="" class="label label-default" rel="tooltip" title="Download now">Download</a></p>
        
                                    <a href="#">
                                        <img style="width: 100px; height: 100px;" src="images/top_k_companion.png"  alt="topkcompanions"/>
                                    </a> 
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
                
                    </div>-->
    </body>
    <%@include file="footer.jsp" %>
</html>
<script>
    $(document).ready(function() {
        $("[rel='tooltip']").tooltip();

        $('.thumbnail').hover(
//                $(this).animate({height:300,width:300},"fast");
//                function() {
//                    $(this).find('.caption').slideDown(250); //.fadeIn(250)
//                },
//                function() {
//                    $(this).find('.caption').slideUp(250); //.fadeOut(205)
//                }

                );
    });
</script>