<%@page import="com.sloca.dao.BasicReportingDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<!--- Fix the calendar icon's height for bootstrap --->
@input-height-large: (floor(18 * 1.33) + (10 * 2) + 2);
<!DOCTYPE html>
<html>

    <head>

        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Group Top K Next Popular Places</title>
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
        <script>
            function validateForm() {
                var x = document.forms["nextPlaceForm"]["atTime"].value;
                if (x == null || x == "") {
                    alert("date is required!");
                    return false;
                }
            }
        </script>

    </head>
    <body>
        <%@include file="navBar.jsp" %>
        <div class='container-fluid' style='align: center;'>
            <h2 align='center'>View Top k Next Popular Places For Group</h2>
            <div class='well'>
                <div class='row' align='center'>
                    <div class='form-group'>
                        <form method='POST' action='ViewTopkNextPlaces.do' name="nextPlaceForm" onsubmit="validateForm()">
                            <div class='col-md-4'>
                                <h4><em>Top</em></h4>
                                <select name="kValue" id='kVal' class='chosen-select' data-placeholder="Select Number">
                                    <option value="3"></option>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">6</option>
                                    <option value="7">7</option>
                                    <option value="8">8</option>
                                    <option value="9">9</option>
                                    <option value="10">10</option>
                                </select>                                
                            </div>
                            <div class='col-md-4'>
                                <h4><em>Next Places From</em></h4>
                                <select name="semanticPlace" id='location' data-placeholder="Select Location">

                                    <%
                                        ArrayList<String> places = BasicReportingDAO.getAllLocationNames();
                                        Iterator iter = places.iterator();
                                        while (iter.hasNext()) {
                                            String location = (String) iter.next();
                                    %>
                                    <option value="<%=location%>"><%=location%></option>
                                    <%
                                            iter.remove();
                                        }
                                    %>
                                </select>                                
                            </div>
                            <div class='col-md-4'>
                                <h4><em>During Period</em></h4>
                                <div id="datetimepicker" class="input-group">
                                    <input type="text" style='height: 25px' required name="atTime" id="period" class="form-control" data-format="yyyy-mm-dd HH:MM:ss"  placeholder="Date/Time"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar" style="font-size: 12px;"></span></span>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <br />
                <div class='row'>
                    <div class="col-md-12">
                        <div align='center'>
                            <input type='button' id='genReportBtn' onclick='generateReport()' value='Generate Report' class='btn btn-success' />                            
                        </div> 
                    </div>
                </div>
            </div>
            <div id='nextpopzplace_result'>

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
            defaultDate: '2014-03-23 00:40:00'
        });
        $(".chosen-select1").chosen({
            width: "100%",
            allow_single_deselect: true
        });
        $(".chosen-select").chosen({
            width: "100%",
            allow_single_deselect: true
        });
    });

    // submit button click event (jquery)
    function generateReport() {
        // obtain params via jquery
        var location = $('#location').val();
        var kVal = $('#kVal').val();
        var period = $('#period').val();
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
                document.getElementById("nextpopzplace_result").innerHTML = "";
                var newNode = document.createElement('div');
                newNode.innerHTML = xmlhttp.responseText;
                $("#nextpopzplace_result").append(newNode);
                $("#nextpopzplace_result").show();
            }
        }
        xmlhttp.open("POST", "GroupTopKNextPlaces.do?kValue=" + kVal + "&atTime=" + period + "&semanticPlace=" + location, true);
        xmlhttp.send();
        //http://localhost:8080/sloca_app/ViewTopkNextPlaces.do?kValue=3&atTime=2014-03-23%2000:40:00&semanticPlace=SMUSISL1LOBBY
        //http://localhost:8080/sloca_app/ToggleTopKNextPlaces.do?kValue=3&atTime=2014-03-23%2000:40:00&semanticPlace=SMUSISL1LOBBY
    }
    ;
</script>