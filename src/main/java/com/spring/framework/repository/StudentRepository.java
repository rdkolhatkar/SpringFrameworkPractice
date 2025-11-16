package com.spring.framework.repository;

import com.spring.framework.model.StudentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// This class is responsible for saving data into the database using JDBC Template.
@Repository
public class StudentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Insert data into MySQL table and return generated ID
    public int saveStudent(StudentData student) {

        String sql = "INSERT INTO StudentDetails(first_name, last_name, dob, address, email, country, state, postal_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                student.getFirstName(),
                student.getLastName(),
                new java.sql.Date(student.getDob().getTime()),   // IMPORTANT FIX
                student.getAddress(),
                student.getEmail(),
                student.getCountry(),
                student.getState(),
                student.getPostalCode()
        );

        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

}
