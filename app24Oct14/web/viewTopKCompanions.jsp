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
        <script src="chosen/chosen.jquery.js"></script>
        <script src="bootstrap/js/transition.js"></script>        
    </head>
    <body>
        <%@page import="com.sloca.dao.*, java.util.*"%>
        <%@include file="navBar.jsp" %>           
        <div class='container-fluid' style='align: center;'>
            <h2 align="center">View Top k Companions</h2>
            <div class="well">
                <div class='row' align="center">
                    <div class="form-group">
                            <div class="col-md-3">
                                <h4><em>Top</em></h4>
                                <select id="kVal" name="kVal" class="chosen-select" data-placeholder="Select Number">
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
                            <div class="col-md-3">
                                <h4><em>Companions For</em></h4>
                                <select id="student_name" name="student_name" class="chosen-select" data-placeholder="Student">
                                    <%
                                        //display all names
                                        ArrayList<String> studentList = BasicReportingDAO.retrieveAllStudents();

                                        out.println("<option value='0'></option>");

                                        for (int i = 0; i < studentList.size(); i++) {
                                            out.print("<option value=\"" + studentList.get(i) + "\">");
                                            out.print(studentList.get(i));
                                            out.print("</option>");
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <h4><em>From</em></h4>
                                <div id="from_datetime" class="input-group">
                                    <input type="text" style='height: 25px' required name="from_datetime" id="from_datetime_val" class="form-control" data-format="yyyy-mm-dd HH:MM:ss"  placeholder="From Date"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar" style="font-size: 12px;"></span></span>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <h4><em>To</em></h4>
                                <div id="to_datetime" class="input-group">
                                    <input type="text" style='height: 25px' required name="to_datetime" id="to_datetime_val" class="form-control" data-format="yyyy-mm-dd HH:MM:ss"  placeholder="To Date"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar" style="font-size: 12px;"></span></span>
                                </div>
                            </div>
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
            <!--- Display results from topkcompanionresult.jsp --->
            <div id="topkcompanions_result">
                
            </div>
        </div>
    </body>
    <%@include file="footer.jsp" %>
</html>

<script type="text/javascript">
    
    $(function() {
        $('#from_datetime').datetimepicker({
            language: 'pt-BR',
            format: 'YYYY-MM-DD HH:mm:ss',
            defaultDate: '2014-03-24 15:30:00'
        });
        $('#to_datetime').datetimepicker({
            language: 'pt-BR',
            format: 'YYYY-MM-DD HH:mm:ss',
            defaultDate: '2014-03-24 17:30:00'
        });
        $(".chosen-select").chosen({
            width: "100%",
            allow_single_deselect: true
        });
    });
    
    // submit button click event (jquery)
    function generateReport()  {
       // obtain params via jquery
       var kVal = $('#kVal').val();
       var student = $('#student_name').val();
       var from_date = $('#from_datetime_val').val();
       var to_date = $('#to_datetime_val').val();
       
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
                document.getElementById("topkcompanions_result").innerHTML = "";
                var newNode = document.createElement('div');
                newNode.innerHTML = xmlhttp.responseText;
                $("#topkcompanions_result").append(newNode);
                $("#topkcompanions_result").show();
            }
        }
        // set params to pass to servlet
        xmlhttp.open("GET", "ViewTopCompanionServlet.do?startTime=" + from_date + "&endTime=" + to_date + "&KValue=" + kVal + "&name=" + student, true);
        xmlhttp.send(); 
    };
</script>