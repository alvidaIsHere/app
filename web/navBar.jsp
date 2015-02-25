<style>
    .navbar-custom-sloca  .nav > li > a {
        //color: black;
        float: none;
        padding: 15px 22px;
        font-size: 18px;

    }

    /*    .container-fluid{
                width:1000px;
               }*/

    .navbar-inverse { background-color: #090524}
    .navbar-inverse .navbar-nav>.active>a:hover,.navbar-inverse .navbar-nav>li>a:hover, .navbar-inverse .navbar-nav>li>a:focus { background-color: #2E96FF}
    .navbar-inverse .navbar-nav>.active>a,.navbar-inverse .navbar-nav>.open>a,.navbar-inverse .navbar-nav>.open>a, .navbar-inverse .navbar-nav>.open>a:hover,.navbar-inverse .navbar-nav>.open>a, .navbar-inverse .navbar-nav>.open>a:hover, .navbar-inverse .navbar-nav>.open>a:focus { background-color: #080808}
    .dropdown-menu { background-color: #FFFFFF}
    .dropdown-menu>li>a:hover, .dropdown-menu>li>a:focus { background-color: #428BCA}
    .navbar-inverse { background-image: none; }
    .dropdown-menu>li>a:hover, .dropdown-menu>li>a:focus { background-image: none; }
    .navbar-inverse { border-color: #000000}
    .navbar-inverse .navbar-brand { color: #DEDEDE}
    .navbar-inverse .navbar-brand:hover { color: #FFFFFF}
    .navbar-inverse .navbar-nav>li>a { color: #E8E8E8}
    .navbar-inverse .navbar-nav>li>a:hover, .navbar-inverse .navbar-nav>li>a:focus { color: #FFFFFF}
    .navbar-inverse .navbar-nav>.active>a,.navbar-inverse .navbar-nav>.open>a, .navbar-inverse .navbar-nav>.open>a:hover, .navbar-inverse .navbar-nav>.open>a:focus { color: #FFFFFF}
    .navbar-inverse .navbar-nav>.active>a:hover, .navbar-inverse .navbar-nav>.active>a:focus { color: #FFFFFF}
    .dropdown-menu>li>a { color: #333333}
    .dropdown-menu>li>a:hover, .dropdown-menu>li>a:focus { color: #FFFFFF}
    .navbar-inverse .navbar-nav>.dropdown>a .caret { border-top-color: #999999}
    .navbar-inverse .navbar-nav>.dropdown>a:hover .caret { border-top-color: #FFFFFF}
    .navbar-inverse .navbar-nav>.dropdown>a .caret { border-bottom-color: #999999}
    .navbar-inverse .navbar-nav>.dropdown>a:hover .caret { border-bottom-color: #FFFFFF}


</style>
<!-- Fixed NAV Bar for student-->

<div class='navbar navbar-inverse navbar-fixed-top navbar-custom-sloca' role='navigation'>
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
                <li><a href='welcomeStudent.jsp' class='navbar-brand'>Home</a></li>
                <li><a href='viewHeatMap.jsp' class='navbar-brand'>Heat Map</a></li>
                <li><a href='basicReport.jsp' class='navbar-brand'>Location Reports</a></li>
                <li><a href='viewGroups.jsp' class='navbar-brand'>View Groups</a></li>
                <li><a href='groupReport.jsp' class='navbar-brand'>Group Reports</a></li>
                <li><a href='logout.jsp' class='navbar-brand'>Logout</a></li>
            </ul>
        </div>
        <%
            String userName = (String) session.getAttribute("user");
            if (userName != null) {
        %>
        <p style="float:right;margin-top:-50px" class='nav navbar-brand'>User: <%=userName%></p>
        <%}%>
    </div>
</div>
</br>
<br />
<p></p>
<%--<%@include file="header.jsp" %>--%>  

