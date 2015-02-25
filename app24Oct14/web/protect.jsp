<%-- 
    Document   : protect.jsp
    Created on : Sep 11, 2014, 1:44:02 PM
    Author     : Elvin
--%>

<%
    String user = (String) session.getAttribute("user");
    String admin = (String) session.getAttribute("admin");
    String access = (String) session.getAttribute("access");
    String errMess = null;

    if (access.equalsIgnoreCase("others")) {

        if (user != null) {
            errMess = user + " is logged in. Please logout to access this page <p>Redirecting to home page in 2 seconds.";
            response.setHeader("Refresh", "2;url=welcomeStudent.jsp");
        } else if (admin != null) {
            errMess = admin + " is logged in.  Please logout to access this page <p>Redirecting to home page in 2 seconds.";
            response.setHeader("Refresh", "2;url=welcomeAdmin.jsp");
        }
    }
    else if (user == null && admin == null) {
        errMess = "You have no access to this page. Redirecting to login page in 2 seconds.";
        response.setHeader("Refresh", "2;url=login.jsp");
    } else if (user != null && access.equalsIgnoreCase("admin")) {
        errMess = "Invalid access from :" + user + "<p>Only admin have access to this page. <p>Redirecting to home page in 2 seconds.";
        response.setHeader("Refresh", "2;url=welcomeStudent.jsp");
    } else if (admin != null && access.equalsIgnoreCase("student")) {
        errMess = "Invalid access from :" + admin + "<p>Only student have access to this page. <p>Redirecting to home page in 2 seconds.";
        response.setHeader("Refresh", "2;url=welcomeAdmin.jsp");
    } else if (admin != null && user != null) {
        errMess = "Different login session detected from :" + admin + " and " + user + "! Please relogin :) <p>Redirecting to login page in 2 seconds.";
        session.invalidate();
        response.setHeader("Refresh", "2;url=login.jsp");
    }
    session.setAttribute("errMess", errMess);
%>
