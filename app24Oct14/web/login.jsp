<%-- 
    Document   : login
    Created on : Sep 2, 2014, 7:03:47 PM
    Author     : Elvin, Christina
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%//access refers to the one with access to this page, change the second parameter for "student" or "admin"
        session.setAttribute("access", "others");%>
    <%@include file="protect.jsp" %>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>SLOCA Login</title>
        <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <script src="bootstrap/js/jquery-2.1.1.min.js"></script>
        <script src="bootstrap/js/bootstrap.min.js"></script>
        <script src="bootstrap/js/collapse.js"></script>
        <script src="bootstrap/js/transition.js"></script>

    </head>
    <body>
        <style>
            button {
                margin: 0;
                padding: 0;
                border: 0;
                font-weight: normal;
                font-style: normal;
                font-size: 100%;
                line-height: 1;
                font-family: inherit;
            }
            .clearfix:before,
            .clearfix:after {
                display: table;
                content: "";
            }
            .clearfix:after { clear: both }
            html { background-color: white }
            body {
                color: black;
                font-family: "Helvetica Neue",Helvetica,Arial,Verdana,sans-serif;
                font-size: 13px;
                text-align: center;
            }
            input[type=text]:not(.basic),
            input[type=tel]:not(.basic),
            input[type=email]:not(.basic),
            input[type=password]:not(.basic),
            select:not(.basic),
            textarea:not(.basic) {
                border-radius: 3px;
                -moz-border-radius: 3px;
                -webkit-border-radius: 3px;
                border: 1px solid #ccc;
                display: inline-block;
                font-size: 17px;
                padding: 12px 16px;
                width: 210px;
            }
            input[type=text]:focus:not(.basic),
            input[type=tel]:focus:not(.basic),
            input[type=email]:focus:not(.basic),
            input[type=password]:focus:not(.basic),
            select:focus:not(.basic),
            textarea:focus:not(.basic) {
                box-shadow: inset 0 1px 3px rgba(0,0,0,0.1),0 0 8px rgba(89,222,44,0.4);
                -moz-box-shadow: inset 0 1px 3px rgba(0,0,0,0.1),0 0 8px rgba(89,222,44,0.4);
                -webkit-box-shadow: inset 0 1px 3px rgba(0,0,0,0.1),0 0 8px rgba(89,222,44,0.4);
                border-color: #59de2c;
                outline: none;
            }
            input[type=text].error:not(.basic),
            input[type=tel].error:not(.basic),
            input[type=email].error:not(.basic),
            input[type=password].error:not(.basic),
            select.error:not(.basic),
            textarea.error:not(.basic) { border-color: #f90 !important }
            input[type=text].error:focus:not(.basic),
            input[type=tel].error:focus:not(.basic),
            input[type=email].error:focus:not(.basic),
            input[type=password].error:focus:not(.basic),
            select.error:focus:not(.basic),
            textarea.error:focus:not(.basic) {
                box-shadow: inset 0 1px 3px rgba(0,0,0,0.1),0 0 8px rgba(255,153,0,0.4);
                -moz-box-shadow: inset 0 1px 3px rgba(0,0,0,0.1),0 0 8px rgba(255,153,0,0.4);
                -webkit-box-shadow: inset 0 1px 3px rgba(0,0,0,0.1),0 0 8px rgba(255,153,0,0.4);
                border-color: #f90;
            }

            body>#login {
                margin: 0 auto;
                padding: 30px 0 0;
                width: 100%;
            }
            body>#login h1 {
                border-radius: 100px;
                -moz-border-radius: 100px;
                -webkit-border-radius: 100px;
                background-color: #fff;
                display: inline-block;
                margin: 0 auto;
                position: relative;
                top: 52px;
                width: 130px;
                z-index: 1;
            }

            body>#login form {
                border-radius: 10px;
                -moz-border-radius: 10px;
                -webkit-border-radius: 10px;
                box-sizing: border-box;
                -moz-box-sizing: border-box;
                -wekbit-box-sizing: border-box;
                border: 8px solid #F5F3F0;
                display: inline-block;
                margin: 27px auto 0;
                padding: 60px 50px 45px;
                position: relative;
                z-index: 0;
            }
            body>#login form .form_errors {
                color: #f90;
                display: block;
                font-size: 15px;
                font-weight: 500;
                margin: 0 0 40px;
            }
            body>#login form .field_container {
                margin: 0 auto 12px;
                text-align: left;
                width: auto;
            }
            body>#login form .field_container:last-child { margin-bottom: 0 }
            body>#login form button {
                border-radius: 7px;
                -moz-border-radius: 7px;
                -webkit-border-radius: 7px;
                background-color: #f6f6f6;
                cursor: pointer;
                margin-left: 12px;
                height: 56px;
                padding: 5px;
                width: auto;
            }
            body>#login form button:hover { background-color: #f8f8f8 }
            body>#login form button:active { padding: 5px }
            body>#login form button:active .button_text {
                box-shadow: 0 0 8px #ccc;
                -moz-box-shadow: 0 0 8px #ccc;
                -webkit-box-shadow: 0 0 8px #ccc;
                background-color: #f6f6f6;
                background-repeat: repeat-x;
                height: 46px;
                width: auto;
            }

            body>#login form button .button_text {
                border-radius: 4px;
                -moz-border-radius: 4px;
                -webkit-border-radius: 4px;
                box-shadow: 0 0 4px rgba(0,0,0,0.2);
                -moz-box-shadow: 0 0 4px rgba(0,0,0,0.2);
                -webkit-box-shadow: 0 0 4px rgba(0,0,0,0.2);
                background-color: #f0f0f0;
                background-repeat: repeat-x;

                color: #807b6e;
                display: inline-block;
                font-size: 16px;
                font-weight: 500;
                height: 46px;
                line-height: 46px;
                padding: 0 25px;
                text-shadow: 1px 1px 0 #fff;
                width: auto;
            }
            body>#login form input[type=text],
            body>#login form input[type=email],
            body>#login form input[type=password] {
                border-color: #F5F3F0;
                border-style: solid;
                border-width: 4px;
                display: inline-block;
                position: relative;
            }
            body>#login form input[type=text]:focus,
            body>#login form input[type=email]:focus,
            body>#login form input[type=password]:focus {
                border-color: #59de2c;
                border-width: 2px;
                padding: 14px 18px;
            }
            body>#login form input[type=text].error,
            body>#login form input[type=email].error,
            body>#login form input[type=password].error {
                border-width: 2px;
                padding: 14px 18px;
            }
            body>#login form input[type=text][disabled],
            body>#login form input[type=email][disabled],
            body>#login form input[type=password][disabled] {
                background-color: #F5F3F0;
                color: #999488;
            }
            body>#login form .login_link {
                color: #807b6e;
                font-size: 13px;
                font-weight: 500;
            }
            body>#login form .login_link:hover { color: #f90 }
            body>#login form#login_form input[type=text],
            body>#login form#login_form input[type=email],
            body>#login form#login_form input[type=password] { width: 290px }
        </style>

        <!-- Fixed NAV Bar -->
        <% if (session.getAttribute("errMess") == null) { %>
        <div class='navbar navbar-default navbar-fixed-top' role='navigation'>
            <div class='container-fluid'>
                <div class='nav-header'>
                    <button type='button' class='navbar-toggle' data-toggle='collapse' data-target=".navbar-collapse">
                        <span class='icon-bar'></span>
                        <span class='icon-bar'></span>
                        <span class='icon-bar'></span>
                    </button>
                </div>
                <div class='navbar-collapse collapse'>
                    <ul class='nav navbar-nav'>                        
                        <li><a href='#' class='navbar-brand'>Home</a></li>
                        <!-- Ease of access purpose -->
                        <li><a href='welcomeAdmin.jsp' class='navbar-brand'>Admin Page</a></li>
                        <li><a href='welcomeStudent.jsp' class='navbar-brand'>Student Page</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <%}%>
        <%@include file="header.jsp" %>
        <% if (session.getAttribute("errMess") != null) {
                out.println(session.getAttribute("errMess"));
                session.setAttribute("errMess", null);
            } else {%>
        <div id="login">
            <form  name="login" id="login_form" method="POST" action="login.do" >
                <div class="field_container">
                    <input type="text" name="username" id="username" placeholder="Username" class="form-control" required>
                </div>

                <div class="field_container">
                    <input type="Password" name="password" placeholder="Password" class="form-control" required>
                    <button id="sign_in_button">
                        <span class="button_text">Sign In</span>
                    </button>
                </div>
                <div id="sign_in_options" class="field_container">        
                    <div class="clearfix"></div>
                </div>
            </form>

        </div>
        <%}%>
    </body>

</html>
