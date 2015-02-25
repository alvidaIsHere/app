package com.sloca.dao;

import com.sloca.db.ConnectionFactory;
import com.sloca.model.GenderContainer;
import com.sloca.model.SchoolContainer;
import com.sloca.model.Student;
import com.sloca.model.YearContainer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

public class BreakdownDAO {

    private ArrayList<Object> breakdown = new ArrayList<>();
    private static transient ArrayList<Student> student_list;

    /**
     * Returns all students found in database within the previous 15 minutes
     * query window of the given time
     *
     * @param datetime both date and time input by user
     * @return ArrayList<Student> that are in school for the last 15 minutes
     * starting from the input datetime
     */
    public static ArrayList<Student> getStudentsWithinPeriod(String datetime) {

        ArrayList<Student> results = new ArrayList<>();

        // connection params
        Connection connection = null;
        ConnectionFactory cf = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT SUBSTRING(email, INSTR(email, '@') - 4, 4) AS Year, s.gender AS Gender, SUBSTRING(email, INSTR(email, '@') + 1, 20) AS School FROM `student` s, `location` l "
                + "WHERE s.mac_address = l.student_mac_address AND time < ? AND time >= ?";

        try {

            // first, subtract 15 minutes from datetime
            String startTime = DateUtilityDAO.getEndDate(datetime, "yyyy-MM-dd HH:mm:ss", -15);

            // connection params and set query attributes
            cf = new ConnectionFactory();
            connection = cf.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, datetime);
            stmt.setString(2, startTime);
            rs = stmt.executeQuery();

            // obtain results
            while (rs.next()) {
                String year = rs.getString("Year");
                String gender = rs.getString("Gender");
                String school = rs.getString("School");

                // only obtain first portion of sis.smu.edu.sg --> split to array and obtain first value (sis), hence [0]
                school = school.split("\\.")[0];

                // create new student object and add to array list
                results.add(new Student(year, gender, school));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } finally {

            // close connection and return results
            ConnectionFactory.close(connection, stmt);

            // set student_list
            student_list = results;

            return results;
        }
    }

    /**
     * Returns all students found in database within the previous 15 minutes
     * query window of the given time and process the breakdown based on their
     * year, gender, and school
     *
     * @param datetime both date and time input by user
     * @param order by which the user want to sort the breakdown by
     * @return ArrayList<Object> that contains the breakdown of students either
     * by year, gender or school
     */
    public static ArrayList<Object> processBreakdown(String datetime, ArrayList<String> order) {

        ArrayList<Object> breakdown = new ArrayList<>();
        if (order != null && !order.isEmpty()) {
            // populate student-list
            student_list = getStudentsWithinPeriod(datetime);

            // check order, get first element
            String first_element = order.get(0);

            // execute sort based on first element
            switch (first_element) {
                case "year":
                    // execute year to be processed first in line
                    breakdown = breakdownByYear(order, student_list);
                    break;
                case "gender":
                    // execute gender to be processed first in line
                    breakdown = breakdownByGender(order, student_list);
                    break;
                case "school":
                    // execute school to be processed first in line
                    breakdown = breakdownBySchool(order, student_list);
                    break;
            }
        }
        return breakdown;
    }

    /**
     * Returns breakdown of student list by their year. Only students with the
     * admission year from 2010 to 2014 will be returned
     *
     * @param order by which the user want to sort the breakdown by
     * @param student_list contains a list of student within 15 minutes before
     * the datetime
     * @return ArrayList<Object> that contains the breakdown of students by year
     */
    public static ArrayList<Object> breakdownByYear(ArrayList<String> order, ArrayList<Student> student_list) {
        ArrayList<Object> breakdown = new ArrayList<>();
        // create new list of year container
        ArrayList<YearContainer> year_breakdown = new ArrayList<>();

        // add the number of years from 2010 up till date
        year_breakdown.add(new YearContainer("2010"));
        year_breakdown.add(new YearContainer("2011"));
        year_breakdown.add(new YearContainer("2012"));
        year_breakdown.add(new YearContainer("2013"));
        year_breakdown.add(new YearContainer("2014"));

        // loop through student_list to sieve out students for each particular year and input into respective year containers
        for (YearContainer year_instance : year_breakdown) {
            for (Student s : student_list) {
                // verify if the student fits in this particular year
                String student_year = s.getYear();
                String container_year = year_instance.getYear();

                // add to container only if years tally
                if (student_year.equals(container_year)) {
                    year_instance.add(s);
                }
            }
        }

        // sorting by year is done, thus remove it from order and process the rest in line
        order.remove("year");

        // new sort order
        ArrayList<String> new_order = (ArrayList<String>) order.clone();

        // add year instances to main breakdown list
        for (YearContainer year_instance : year_breakdown) {
            breakdown.add(year_instance);
            // tabulate percentage
            year_instance.calcPercentage(student_list.size());
            // process further breakdown
            year_instance.processBreakdown(new_order);
            new_order = (ArrayList<String>) order.clone();
        }

        return breakdown;
    }

    /**
     * Returns breakdown of student list by their gender ( i.e.:M or F)
     *
     * @param order by which the user want to sort the breakdown by
     * @param student_list contains a list of student within 15 minutes before
     * the datetime
     * @return ArrayList<Object> that contains the breakdown of students by
     * gender
     */
    public static ArrayList<Object> breakdownByGender(ArrayList<String> order, ArrayList<Student> student_list) {
        ArrayList<Object> breakdown = new ArrayList<>();

        // initialise gender container
        ArrayList<GenderContainer> gender_breakdown = new ArrayList<>();
        gender_breakdown.add(new GenderContainer("M"));
        gender_breakdown.add(new GenderContainer("F"));

        // iterate through student's list to filter males and females
        for (GenderContainer gender_instance : gender_breakdown) {
            for (Student s : student_list) {
                // if student is of a particular gender, add to the gender container
                if (s.getGender().equals(gender_instance.getGender())) {
                    gender_instance.add(s);
                }
            }
        }
        // gender is done processing, remove from order
        order.remove("gender");

        // further processing, refreshing order list after removal
        ArrayList<String> new_order = (ArrayList<String>) order.clone();

        // add the instances into breakdown and process further breakdown
        for (GenderContainer gender_instance : gender_breakdown) {
            breakdown.add(gender_instance);
            // tabulate percentage
            gender_instance.calcPercentage(student_list.size());
            // further process breakdown
            gender_instance.processBreakdown(new_order);
            new_order = (ArrayList<String>) order.clone();
        }
        return breakdown;
    }

    /**
     * Returns breakdown of student list by their school (i.e.: business,
     * economics, law, sis, accountancy, or socsc)
     *
     * @param order by which the user want to sort the breakdown by
     * @param student_list contains a list of student within 15 minutes before
     * the datetime
     * @return ArrayList<Object> that contains the breakdown of students by
     * school
     */
    public static ArrayList<Object> breakdownBySchool(ArrayList<String> order, ArrayList<Student> student_list) {
        ArrayList<Object> breakdown = new ArrayList<>();

        // initialise school container
        ArrayList<SchoolContainer> school_breakdown = new ArrayList<>();

        // add schools
        school_breakdown.add(new SchoolContainer("business"));
        school_breakdown.add(new SchoolContainer("economics"));
        school_breakdown.add(new SchoolContainer("law"));
        school_breakdown.add(new SchoolContainer("sis"));
        school_breakdown.add(new SchoolContainer("accountancy"));
        school_breakdown.add(new SchoolContainer("socsc"));

        // loop through students_list to filter students for each schools
        for (SchoolContainer school_instance : school_breakdown) {
            for (Student s : student_list) {
                if (s.getSchool().equals(school_instance.getSchool())) {
                    school_instance.add(s);
                }
            }
        }

        // remove the "school" option in the order, moving onto next in line
        order.remove("school");

        // flush/refresh the list
        ArrayList<String> new_order = (ArrayList<String>) order.clone();

        // store list into schoolcontainer objects for sorting first
        ArrayList<SchoolContainer> list_to_sort = new ArrayList<>();

        // iterate through and add school objects into the breakdown
        for (SchoolContainer school_instance : school_breakdown) {
            list_to_sort.add(school_instance);
            // tabulate percentage
            school_instance.calcPercentage(student_list.size());
            // further processing of the rest 'in-line'
            school_instance.processBreakdown(new_order);
            // refresh order list after
            new_order = (ArrayList<String>) order.clone();

        }
        // sort in alphabetical order
        Collections.sort(list_to_sort, Collections.reverseOrder());
        breakdown.addAll(list_to_sort);
        return breakdown;
    }
}
