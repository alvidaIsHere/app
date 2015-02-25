<%-- 
    Document   : viewGroups
    Created on : Oct 20, 2014, 5:42:01 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>View Groups</title>
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
        <%@include file="navBar.jsp" %>
        <div class='container-fluid' style='align: center;'>
            <div class='row' align="center">                
                <h2 align="center">View Groups</h2>
                <div class="form-group">
                    <div class="well">
                        <form method="POST" action="#">
                            <div class="col-md-6" align="center">
                                <div id="datetimepicker" class="input-group" align="center">
                                    <input type="text" style="height: 25px" required name="datetime" id="datetime" class="form-control" data-format="yyyy-mm-dd HH:MM:ss"  placeholder="Date/Time"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar" style="font-size: 12px"></span>

                                    </span>
                                </div>
                            </div>

                            <br />
                            <br />
                            <div class='row'>
                                <div class="col-md-12">
                                    <div align='center'>
                                        <input type='button' id="genReportBtn" value='Generate Report' class='btn btn-success' onclick="generateReport()" />                               
                                    </div> 
                                </div>
                            </div>
                        </form>   
                    </div>
                </div>
                <!--- Display results from topkreport.jsp here (triggered by ajax) --->
                <div id="groups_result">

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
            defaultDate: '2014-03-24 15:30:00'
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
                document.getElementById("groups_result").innerHTML = "";
                var newNode = document.createElement('div');
                newNode.innerHTML = xmlhttp.responseText;
                $("#groups_result").append(newNode);
                $("#groups_result").show();
            }
        }
        xmlhttp.open("GET", "viewGroupsResult.jsp", true);
        xmlhttp.send();
    }
    ;
</script>
