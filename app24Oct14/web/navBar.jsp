<style>
    .navbar-custom-sloca  .nav > li > a {
        //color: black;
        float: none;
        padding: 15px 22px;
        font-size: 18px;
    }
</style>
<!-- Fixed NAV Bar for student-->
<div class='navbar navbar-default navbar-fixed-top navbar-custom-sloca' role='navigation'>
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
    </div>
</div>

<%@include file="header.jsp" %>  

