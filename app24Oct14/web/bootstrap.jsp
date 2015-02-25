<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "admin");
        String userName = (String) session.getAttribute("admin");
    %>
    <%@include file="protect.jsp"%>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bootstrap Page</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>
    </head>
    <body>
        <!-- Fixed NAV Bar -->
        <% if (session.getAttribute("errMess") == null) {%>
        <%@include file="adminNavBar.jsp"%>
        <%}%>
        <header class='masthead' style='background-color: #428bca; margin-top: 50px'>
            <div class='container'>
                <div class='row'>
                    <div class='col-md-12' style='color: white'>
                        <h1 align='center'>SLOCA System</h1>
                        <p class='lead' align='center'>{Singapore Management University}</p>
                    </div> 
                </div>
            </div>
        </header>        
        <div class='container-fluid'>
            <div class='row'>
                <div class='col-md-12' align='center'>
                    <h3>Bootstrap System</h3>
                    <p>Only zip file containing "location.csv", "demographics.csv", and "location-lookup.csv" will be accepted</p>
                    <form method="POST" action="bootstrapServlet.do" enctype="multipart/form-data" >
                        File:
                        <input type="file" name="file" id="file" /> <br/>
                        <input type="submit" value="Upload" name="upload" class='btn btn-primary' id="upload" />
                    </form>
                </div>
            </div>
        </div>  
        <% if (session.getAttribute("errMess") != null) {
                out.println(session.getAttribute("errMess"));
                session.setAttribute("errMess", null);
            }%>
    </body>
</html>

