<%-- 
    Document   : viewTopKPlaces
    Created on : Sep 13, 2014, 1:11:57 AM
    Author     : baoyi.soh.2011
--%>
@input-height-large: (floor(18 * 1.33) + (10 * 2) + 2);
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>View Top K Popular Places</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="chosen/chosen.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>
        <script src="bootstrap/datetimepicker/js/moment.js"></script>
        <script src="bootstrap/datetimepicker/js/bootstrap-datetimepicker.js"></script>
        <link href="bootstrap/datetimepicker/css/bootstrap-datetimepicker.css" rel="stylesheet">
        <script src="chosen/chosen.jquery.js"></script>
    </head>
    <body>
        <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
            session.setAttribute("access", "student");
        %>
        <%@include file="protect.jsp"%>
        <%@include file="navBar.jsp" %>
        <div class='container-fluid' style='align: center;'>
            <div class='row' align="center">                
                <h2 align='center'>SLOCA System</h2>
                <h4 align='center'>{ View Top k Popular Places }</h4>  
                <br />
                <div class="form-group">
                    <div class="well">
                        <form method="POST" action="ViewTopKPlacesServlet.do">
                            <div class="col-md-6">
                                <div id="datetimepicker" class="input-group">
                                    <input type="text" style="height: 25px" required name="datetime" id="datetime" class="form-control" data-format="yyyy-mm-dd HH:MM:ss"  placeholder="Date/Time"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar" style="font-size: 12px"></span></span>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <select id='KValue' name="KValue" class="chosen-select" data-placeholder="Top k">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3" selected>3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                </select>
                            </div>
                            <br />
                            <br />
                            <div class='row'>
                                <div class="col-md-12">
                                    <div align='center'>
                                        <input type='button' id="genReportBtn" onclick="generateReport()" value='Generate Report' class='btn btn-success' />                               
                                    </div> 
                                </div>
                            </div>
                        </form>   
                    </div>
                </div>
                <!--- Display results from topkreport.jsp here (triggered by ajax) --->
                <div id="topkplaces_result">

                </div>
            </div> 
        </div>        
    </body>
    <%@include file="footer.jsp" %>
</html>
<script type="text/javascript">
    $(function() {
        $('#datetimepicker').datetimepicker({
            language: 'pt-BR',
            format: 'YYYY-MM-DD HH:mm:ss',
            defaultDate: '2014-03-31 17:00:00'
        });
        $(".chosen-select").chosen({
            width: "100%",
            allow_single_deselect: true
        });
    });

    // submit button click event (jquery)
    function generateReport() {
        // obtain params via jquery
        var datetime = $('#datetime').val();
        var KValue = $('#KValue').val();
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
                document.getElementById("topkplaces_result").innerHTML = "";
                var newNode = document.createElement('div');
                newNode.innerHTML = xmlhttp.responseText;
                $("#topkplaces_result").append(newNode);
                $("#topkplaces_result").show();
            }
        }
        xmlhttp.open("GET", "ViewTopKPlacesServlet.do?datetime=" + datetime + "&KValue=" + KValue, true);
        xmlhttp.send();
    }
    ;
</script>
