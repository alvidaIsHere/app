<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--- Fix the calendar icon's height for bootstrap --->
@input-height-large: (floor(18 * 1.33) + (10 * 2) + 2);
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
                    <h2 align="center">View Demographics Breakdown</h2>                       
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
                                            <option value="yeargender">Year & Gender</option>
                                            <option value="yeargenderschool">Year, Gender & School</option>
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
            String errorMsg = (String)request.getAttribute("error");
            if(errorMsg != null)    {
                if(errorMsg.equals("invalidInput"))    {
                    out.println("You've entered an invalid input.");
                }
                else if(errorMsg.equals("invalidDate"))   {
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
            defaultDate: '2014-03-24 15:30:00'
        });
        $(".chosen-select").chosen({
            width: "100%",
            allow_single_deselect: true
        });
    });
    
    // submit button click event (jquery)
    function generateReport()  {
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
        xmlhttp.open("GET", "ViewDemoGraph.do?datetime=" + datetime + "&filter=" + filter, true);
        xmlhttp.send(); 
    };
</script>
