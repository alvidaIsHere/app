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
        <link href="chosen/chosen.css" rel="stylesheet">

        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="bootstrap/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/datetimepicker/js/moment.js"></script> 
        <script src="bootstrap/datetimepicker/js/bootstrap-datetimepicker.js"></script>  
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>
        <script src="chosen/chosen.jquery.js"></script>

    </head>
    <body>
        <% if (session.getAttribute("errMess") == null) {%>
        <%@include file="navBar.jsp" %>                           
        <div class='container-fluid' style='align: center;'>
            <div class='row'>
                <div class='col-md-12' align='center'>
                    <h2 align='center'>SLOCA System</h2>
                    <h4 align='center'>{ View Heat Map }</h4>      
                    <br />
                    <div class="well">                         
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-6">
                                    <div id="datetimepicker" class="input-group">
                                        <input style="height: 30px" name="datetime" id="datetime" class="form-control" required data-format="yyyy-mm-dd HH:MM:ss" type="datetime" />
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar" style="font-size: 12px;"></span></span>
                                    </div>
                                </div>
                                <div class="col-md-6">                              
                                    <select id="location" class="chosen-select" name="location" data-placeholder="Select Location">
                                        <option value="0">SIS Basement 1</option>
                                        <option value="1">SIS Level 1</option>
                                        <option value="2">SIS Level 2</option>
                                        <option value="3">SIS Level 3</option>
                                        <option value="4">SIS Level 4</option>
                                        <option value="5">SIS Level 5</option>
                                    </select>
                                </div>
                            </div>                     
                            <br />
                            <div class='row'>
                                <div class="col-md-12">
                                    <div align='center'>
                                        <input type='button' id='genHeatMapBtn' onclick='generateHeatMap()' value='Generate Report' class='btn btn-success' />&nbsp;                                
                                    </div> 
                                </div>
                            </div>
                            <%
                                String error = request.getParameter("error");
                                if (error != null) {
                                    out.print("* Invalid Date Entered!");
                                }
                                error = null;
                            %>    
                        </div>                                
                    </div>                    
                </div> 
            </div>
        </div>                            
        <div id='heatmapResults'>

        </div>
    </body>
    <%@include file="footer.jsp" %>    
    <%}%>
    <% if (session.getAttribute("errMess") != null) {
            out.println(session.getAttribute("errMess"));
            session.setAttribute("errMess", null);
            }%>
</html>
<script type="text/javascript">
    $(function() {
        $('#datetimepicker').datetimepicker({
            language: 'pt-BR',
            format: 'YYYY-MM-DD HH:mm:ss',
            defaultDate: '2014-03-24 15:30:00'
        });
        $(".chosen-select").chosen({
            width: "100%",
            allow_single_deselect: true
        });
    });

    // submit button click event (jquery)
    function generateHeatMap() {
        // obtain params via jquery
        var datetime = $('#datetime').val();
        var location = $('#location').val();
        // ajax 
        var xmlhttp;
        if (window.XMLHttpRequest)
        {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        }
        else
        {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function()
        {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
            {
                document.getElementById("heatmapResults").innerHTML = "";
                var newNode = document.createElement('div');
                newNode.innerHTML = xmlhttp.responseText;
                $("#heatmapResults").append(newNode);
                $("#heatmapResults").show();
            }
        }
        xmlhttp.open("GET", "toggleHeatMap.do?datetime=" + datetime + "&location=" + location, true);
        xmlhttp.send();
    }
    ;

</script>