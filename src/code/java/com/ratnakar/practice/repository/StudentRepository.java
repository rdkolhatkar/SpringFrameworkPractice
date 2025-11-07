package com.ratnakar.practice.repository;

import com.ratnakar.practice.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Student student){
        String sqlQuery = "insert into student (rollNo, name, marks) values (?,?,?)";
        int rows = jdbcTemplate.update(sqlQuery, student.getRollNo(), student.getName(), student.getMarks());
        System.out.println(rows+" Student Added Successfully .......");
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        return students;
    }
}
