<%@page import="com.sloca.model.SchoolContainer"%>
<%@page import="com.sloca.model.GenderContainer"%>
<%@page import="com.sloca.model.YearContainer"%>
<%@page import="com.sloca.model.HeatMap"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>              
<style>
    .table tbody>tr>td{
        vertical-align: middle;
        text-align: center;
    }

    th {
        text-align: center;
        vertical-align: middle;
    }
</style>
<div id='container-fluid'>
    <%
        String error = (String) request.getAttribute("errorMessage");

        if (error == null) {
    %>
    <p align="center" style="color: red">Invalid access, redirect to home page</p>
    <%
        response.setHeader("Refresh", "2;url=welcomeStudent.jsp");

    } else if (!error.equals("")) {
    %>
    <p align="center" style="color: red"><%=error%></p>
    <%

    } else {

    %>
    <table class='table table-bordered table-hover table-striped' id='heatmap_results' align='center'>
        <tbody>

            <%//breakdown data
                int yearTypes = 5;//(2010-2014)
                int genderTypes = 2;
                int schoolTypes = 6;
// loop results
                ArrayList<Object> results = (ArrayList<Object>) request.getAttribute("breakdown");
                int totalStudent = ((Integer) request.getAttribute("totalStudent"));
                String[] orderList = (String[]) request.getAttribute("orderList");
                int numFilter = orderList.length;
                String header = "<tr>";
                for (String s : orderList) {
                    header += "<th>" + s + "</th><th>No.</th><th>%</th>";
                }
                header += "</tr>";
                out.println(header);
                String row = "";

                int rowspan = 1;
                boolean firstRow = true;

                for (Object s : results) {

                    if (s instanceof YearContainer) {
                        // if it's a yearcontainer object, cast it
                        YearContainer year_obj = (YearContainer) s;
                        year_obj.calcPercentage(totalStudent);
                        if (numFilter == 3) {
                            rowspan = schoolTypes * genderTypes;
                        } else if (numFilter == 2) {
                            if (orderList[1].equals("gender")) {
                                rowspan = genderTypes;
                            } else {
                                rowspan = schoolTypes;
                            }
                        }
                        row += "<tr><td rowspan='" + rowspan + "'>" + year_obj.getYear() + "</td>"
                                + "<td rowspan='" + rowspan + "'>" + year_obj.getCount() + "</td>"
                                + "<td rowspan='" + rowspan + "'>" + year_obj.getPercentage() + "</td>";

                        // check if breakdown has values
                        if (year_obj.getBreakdown() != null && !year_obj.getBreakdown().isEmpty()) {

                            // if it's not empty, iterate through and print results
                            ArrayList<Object> breakdown = year_obj.getBreakdown();
                            firstRow = true;
                            for (Object o : breakdown) {
                                rowspan = 1;
                                if (!firstRow) {
                                    row += "<tr>";
                                }
                                if (o instanceof GenderContainer) {
                                    //year gender
                                    // if it's a gendercontainer object, cast it
                                    GenderContainer gender_obj_1 = (GenderContainer) o;
                                    gender_obj_1.calcPercentage(totalStudent);
                                    if (numFilter == 3) {
                                        rowspan = schoolTypes;
                                    }

                                    row += "<td rowspan='" + rowspan + "'>" + gender_obj_1.getGender() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + gender_obj_1.getCount() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + gender_obj_1.getPercentage() + "</td>";
                                    // check if breakdown has values
                                    if (gender_obj_1.getBreakdown() != null && !gender_obj_1.getBreakdown().isEmpty()) {
                                        //1. year gender school
                                        ArrayList<Object> breakdown_1 = gender_obj_1.getBreakdown();
                                        // if it's not empty, iterate through and print results
                                        boolean firstItem = true;
                                        for (Object o_1 : breakdown_1) {

                                            SchoolContainer s_obj_1 = (SchoolContainer) o_1;
                                            s_obj_1.calcPercentage(totalStudent);
                                            if (!firstItem) {
                                                row += "<tr>";

                                            }
                                            row += "<td>" + s_obj_1.getSchool() + "</td>"
                                                    + "<td>" + s_obj_1.getCount() + "</td>"
                                                    + "<td>" + s_obj_1.getPercentage() + "</td></tr>";
                                            out.println(row);
                                            row = "";
                                            firstItem = false;
                                        }

                                    } else {
                                        //2. year and gender
                                        row += "</tr>";
                                        out.println(row);
                                        row = "";
                                    }
                                }
                                if (o instanceof SchoolContainer) {
                                    // if it's a yearcontainer object, cast it
                                    SchoolContainer sch_obj_1 = (SchoolContainer) o;
                                    sch_obj_1.calcPercentage(totalStudent);
                                    if (numFilter == 3) {
                                        rowspan = genderTypes;
                                    }
                                    row += "<td rowspan='" + rowspan + "'>" + sch_obj_1.getSchool() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + sch_obj_1.getCount() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + sch_obj_1.getPercentage() + "</td>";

                                    // check if breakdown has values
                                    if (sch_obj_1.getBreakdown() != null && !sch_obj_1.getBreakdown().isEmpty()) {
                                        //3. year school gender
                                        // if it's not empty, iterate through and print results
                                        ArrayList<Object> breakdown_1 = sch_obj_1.getBreakdown();
                                        boolean firstItem = true;
                                        for (Object o_1 : breakdown_1) {
                                            GenderContainer s_obj_1 = (GenderContainer) o_1;
                                            s_obj_1.calcPercentage(totalStudent);
                                            if (!firstItem) {
                                                row += "<tr>";

                                            }
                                            row += "<td>" + s_obj_1.getGender() + "</td>"
                                                    + "<td>" + s_obj_1.getCount() + "</td>"
                                                    + "<td>" + s_obj_1.getPercentage() + "</td></tr>";
                                            out.println(row);
                                            row = "";
                                            firstItem = false;
                                        }
                                    } else {
                                        //4. year and school
                                        row += "</tr>";
                                        out.println(row);
                                        row = "";
                                    }
                                }
                                firstRow = false;
                            }

                        } else {
                            //year only
                            row += "</tr>";
                            out.println(row);
                            row = "";
                        }

                    } else if (s instanceof GenderContainer) {
                        // if it's a yearcontainer object, cast it
                        GenderContainer gender_obj = (GenderContainer) s;
                        gender_obj.calcPercentage(totalStudent);
                        if (numFilter == 3) {
                            rowspan = schoolTypes * yearTypes;
                        } else if (numFilter == 2) {
                            if (orderList[1].equals("year")) {
                                rowspan = yearTypes;
                            } else {
                                rowspan = schoolTypes;
                            }
                        }
                        row += "<tr><td rowspan='" + rowspan + "'>" + gender_obj.getGender() + "</td>"
                                + "<td rowspan='" + rowspan + "'>" + gender_obj.getCount() + "</td>"
                                + "<td rowspan='" + rowspan + "'>" + gender_obj.getPercentage() + "</td>";

                        // check if breakdown has values
                        if (gender_obj.getBreakdown() != null && !gender_obj.getBreakdown().isEmpty()) {
                            // if it's not empty, iterate through and print results
                            ArrayList<Object> breakdown = gender_obj.getBreakdown();
                            firstRow = true;
                            for (Object o : breakdown) {
                                rowspan = 1;
                                if (!firstRow) {
                                    row += "<tr>";
                                }
                                if (o instanceof SchoolContainer) {
                                    // if it's a yearcontainer object, cast it
                                    SchoolContainer sch_obj_1 = (SchoolContainer) o;
                                    sch_obj_1.calcPercentage(totalStudent);
// check if breakdown has values
                                    if (numFilter == 3) {
                                        rowspan = yearTypes;
                                    }
                                    row += "<td rowspan='" + rowspan + "'>" + sch_obj_1.getSchool() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + sch_obj_1.getCount() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + sch_obj_1.getPercentage() + "</td>";

                                    if (sch_obj_1.getBreakdown() != null && !sch_obj_1.getBreakdown().isEmpty()) {
                                        //6. gender school year
                                        // if it's not empty, iterate through and print results
                                        ArrayList<Object> breakdown_1 = sch_obj_1.getBreakdown();
                                        boolean firstItem = true;
                                        for (Object o_1 : breakdown_1) {
                                            YearContainer y_o_1 = (YearContainer) o_1;
                                            y_o_1.calcPercentage(totalStudent);
                                            if (!firstItem) {
                                                row += "<tr>";

                                            }
                                            row += "<td>" + y_o_1.getYear() + "</td>"
                                                    + "<td>" + y_o_1.getCount() + "</td>"
                                                    + "<td>" + y_o_1.getPercentage() + "</td></tr>";
                                            out.println(row);
                                            row = "";
                                            firstItem = false;
                                        }
                                    } else {
                                        //gender school
                                        row += "</tr>";
                                        out.println(row);
                                        row = "";
                                    }
                                }
                                if (o instanceof YearContainer) {
                                    // if it's a yearcontainer object, cast it
                                    YearContainer year_obj_1 = (YearContainer) o;
                                    year_obj_1.calcPercentage(totalStudent);
                                    if (numFilter == 3) {
                                        rowspan = schoolTypes;
                                    }
                                    row += "<td rowspan='" + rowspan + "'>" + year_obj_1.getYear() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + year_obj_1.getCount() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + year_obj_1.getPercentage() + "</td>";

                                    // check if breakdown has values
                                    if (year_obj_1.getBreakdown() != null && !year_obj_1.getBreakdown().isEmpty()) {
                                        //8. gender year school
                                        // if it's not empty, iterate through and print results
                                        ArrayList<Object> breakdown_1 = year_obj_1.getBreakdown();
                                        boolean firstItem = true;

                                        for (Object o_1 : breakdown_1) {
                                            SchoolContainer s_obj_1 = (SchoolContainer) o_1;
                                            s_obj_1.calcPercentage(totalStudent);
                                            if (!firstItem) {
                                                row += "<tr>";

                                            }
                                            row += "<td>" + s_obj_1.getSchool() + "</td>"
                                                    + "<td>" + s_obj_1.getCount() + "</td>"
                                                    + "<td>" + s_obj_1.getPercentage() + "</td></tr>";
                                            out.println(row);
                                            row = "";
                                            firstItem = false;
                                        }
                                    } else {
                                        //gender year
                                        row += "</tr>";
                                        out.println(row);
                                        row = "";
                                    }
                                }
                                firstRow = false;
                            }
                        } else {
                            //10 gender only
                            row += "</tr>";
                            out.println(row);
                            row = "";
                        }
                    } else if (s instanceof SchoolContainer) {
                        // if it's a yearcontainer object, cast it
                        SchoolContainer sch_obj = (SchoolContainer) s;
                        sch_obj.calcPercentage(totalStudent);
                        if (numFilter == 3) {
                            rowspan = yearTypes * genderTypes;
                        } else if (numFilter == 2) {
                            if (orderList[1].equals("gender")) {
                                rowspan = genderTypes;
                            } else {
                                rowspan = yearTypes;
                            }
                        }
                        row += "<tr><td rowspan='" + rowspan + "'>" + sch_obj.getSchool() + "</td>"
                                + "<td rowspan='" + rowspan + "'>" + sch_obj.getCount() + "</td>"
                                + "<td rowspan='" + rowspan + "'>" + sch_obj.getPercentage() + "</td>";

                        // check if breakdown has values
                        if (sch_obj.getBreakdown() != null && !sch_obj.getBreakdown().isEmpty()) {
                            // if it's not empty, iterate through and print results
                            ArrayList<Object> breakdown = sch_obj.getBreakdown();
                            firstRow = true;
                            for (Object o : breakdown) {
                                rowspan = 1;
                                if (!firstRow) {
                                    row += "<tr>";
                                }
                                if (o instanceof GenderContainer) {

                                    // if it's a gender container object, cast it
                                    GenderContainer gender_obj_1 = (GenderContainer) o;
                                    gender_obj_1.calcPercentage(totalStudent);
                                    if (numFilter == 3) {
                                        rowspan = yearTypes;
                                    }

                                    row += "<td rowspan='" + rowspan + "'>" + gender_obj_1.getGender() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + gender_obj_1.getCount() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + gender_obj_1.getPercentage() + "</td>";
                                    // check if breakdown has values
                                    if (gender_obj_1.getBreakdown() != null && !gender_obj_1.getBreakdown().isEmpty()) {
                                        //11 school gender year
                                        // if it's not empty, iterate through and print results
                                        ArrayList<Object> breakdown_1 = gender_obj_1.getBreakdown();
                                        boolean firstItem = true;
                                        for (Object o_1 : breakdown_1) {
                                            YearContainer y_o_1 = (YearContainer) o_1;
                                            y_o_1.calcPercentage(totalStudent);
                                            if (!firstItem) {
                                                row += "<tr>";
                                            }
                                            row += "<td>" + y_o_1.getYear() + "</td>"
                                                    + "<td>" + y_o_1.getCount() + "</td>"
                                                    + "<td>" + y_o_1.getPercentage() + "</td></tr>";
                                            out.println(row);
                                            row = "";
                                            firstItem = false;
                                        }
                                    } else {
                                        //12 school gender
                                        row += "</tr>";
                                        out.println(row);
                                        row = "";
                                    }
                                }
                                if (o instanceof YearContainer) {
                                    // if it's a yearcontainer object, cast it
                                    YearContainer year_obj_1 = (YearContainer) o;
                                    year_obj_1.calcPercentage(totalStudent);
                                    if (numFilter == 3) {
                                        rowspan = genderTypes;
                                    }
                                    row += "<td rowspan='" + rowspan + "'>" + year_obj_1.getYear() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + year_obj_1.getCount() + "</td>"
                                            + "<td rowspan='" + rowspan + "'>" + year_obj_1.getPercentage() + "</td>";
                                    // check if breakdown has values
                                    if (year_obj_1.getBreakdown() != null && !year_obj_1.getBreakdown().isEmpty()) {
                                        //13 school year gender
                                        // if it's not empty, iterate through and print results
                                        ArrayList<Object> breakdown_1 = year_obj_1.getBreakdown();
                                        boolean firstItem = true;
                                        for (Object o_1 : breakdown_1) {
                                            GenderContainer s_obj_1 = (GenderContainer) o_1;
                                            s_obj_1.calcPercentage(totalStudent);
                                            if (!firstItem) {
                                                row += "<tr>";

                                            }
                                            row += "<td>" + s_obj_1.getGender() + "</td>"
                                                    + "<td>" + s_obj_1.getCount() + "</td>"
                                                    + "<td>" + s_obj_1.getPercentage() + "</td></tr>";
                                            out.println(row);
                                            row = "";
                                            firstItem = false;
                                        }
                                    } else {
                                        //14. school year
                                        row += "</tr>";
                                        out.println(row);
                                        row = "";
                                    }
                                }
                                firstRow = false;
                            }
                        } else {
                            //15 school only
                            row += "</tr>";
                            out.println(row);
                            row = "";
                        }
                    }
                }
            %>
        </tbody>
    </table>
    <%}%>
</div>
