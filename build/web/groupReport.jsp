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

        <script type="text/javascript">
            function changeText() {
                document.getElementById("message").innerHTML = "This function has been dropped";
            }
            ;
        </script>

    </head>
    <body>
        <%@include file="navBar.jsp" %>  
        <br />
        <div class='container-fluid' align="center">   
            <h2 align='center'>SLOCA System</h2>
            <h4 align='center'>{ View Group Reports }</h4>   
            <div class='row' style="margin-top: 3%; 
                 margin-left: auto;
                 margin-right: auto; 
                 display: inline-block;">
                <div class='col-md-3' style="
                     width: 50%;">
                    <div class="thumbnail" style="width: 200px; height: 200px;">
                        <a href="groupTopKForm.jsp">
                            <img src="images/top_k_pop_places.png" alt="topkpopplaces"/>
                        </a>                         
                        <div class="caption">
                            <p>Top k Places</p>
                            <p></p>
                        </div>
                    </div>                 
                </div> 
                <div class='col-md-3' style="
                     width: 50%;">
                    <div class="thumbnail" style="width: 200px; height: 200px;">
                        <a href="#" onclick="changeText()">
                            <img src="images/top_k_next_place.png" alt="Top K Next Places"/>
                        </a>                        
                        <div class="caption">
                            <p>Top K Next Places</p>
                            <h4 id="message" style="color: red"></h4>
                        </div>
                    </div>                 
                </div> 
            </div>
        </div>
    </body>

    <%@include file="footer.jsp" %>
</html>
