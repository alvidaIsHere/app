<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--- Fix the calendar icon's height for bootstrap --->
@input-height-large: (floor(18 * 1.33) + (10 * 2) + 2);
<!DOCTYPE html>
<html>
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "student");
    %>
    <%@include file="protect.jsp"%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Demographics Breakdown</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="chosen/chosen.css" rel="stylesheet">
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
        <%@include file="navBar.jsp" %>           
        <div class='container-fluid' style='align: center;'>
            <div class='row'>
                <div class='col-md-12' align='center'>
                    <h2 align='center'>SLOCA System</h2>
                    <h4 align='center'>{ View Demographics Breakdown }</h4>                     
                    <div class="well">
                        <form method='GET' action='#'>
                            <div class="form-group">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div id="datetimepicker" class="input-group">
                                            <input name="datetime" style='height: 25px' id="datetimepicker1" class="form-control" required data-date-format="yyyy-mm-dd HH:MM:ss" type="text" />
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar" style="font-size: 12px;"></span></span>
                                        </div>
                                    </div>
                                    <div class="col-md-6">                              
                                        <select id="filter" class="chosen-select" name="filter" data-placeholder="Select Filter">

                                            <option value="0"></option>
                                            <option value="year">Year</option>
                                            <option value="gender">Gender</option>
                                            <option value="school">School</option>

                                            <option value="year,gender">Year & Gender</option>
                                            <option value="year,school">Year & School</option>
                                            <option value="gender,year">Gender & Year</option>
                                            <option value="gender,school">Gender & School</option>
                                            <option value="school,year">School & Year</option>
                                            <option value="school,gender">School & Gender</option>  

                                            <option value="year,gender,school">Year, Gender & School</option>
                                            <option value="year,school,gender">Year, School & Gender</option>                                            
                                            <option value="gender,year,school">Gender, Year & School</option>
                                            <option value="gender,school,year">Gender, School & Year</option>
                                            <option value="school,year,gender">School, Year & Gender</option>
                                            <option value="school,gender,year">School, Gender & Year</option>

                                        </select>
                                    </div>                                    
                                </div>                              
                            </div>
                            <div align='center'>
                                <input type='button' id="genReportBtn" onclick="generateReport()" value='Generate Report' class='btn btn-success' />&nbsp;                                
                            </div>                
                        </form>
                    </div>                    
                </div> 
            </div>
        </div>
        <!--- Results here: --->
        <div id='result'>

        </div>
        <%
            // cater for invalid input
            // obtain error param 
            String errorMsg = (String) request.getAttribute("error");
            if (errorMsg != null) {
                if (errorMsg.equals("invalidInput")) {
                    out.println("You've entered an invalid input.");
                } else if (errorMsg.equals("invalidDate")) {
                    out.println("You've entered an invalid date.");
                }
            }
        %>
    </body>
    <%@include file="footer.jsp" %>  
</html>
<script type="text/javascript">
    $(function() {
        $('#datetimepicker').datetimepicker({
            language: 'pt-BR',
            format: 'YYYY-MM-DD HH:mm:ss',
            defaultDate: '2014-03-31 16:15:00'
        });
        $(".chosen-select").chosen({
            width: "100%",
            allow_single_deselect: true
        });
    });

    // submit button click event (jquery)
    function generateReport() {
        // obtain params via jquery
        var datetime = $('#datetimepicker1').val();
        var filter = $('#filter').val();
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
                document.getElementById("result").innerHTML = "";
                var newNode = document.createElement('div');
                newNode.innerHTML = xmlhttp.responseText;
                $("#result").append(newNode);
                $("#result").show();
            }
        }
        xmlhttp.open("POST", "ViewDemo.do?order=" + filter + "&date=" + datetime, true);
        xmlhttp.send();
    }
    ;
</script>
