package com.ratnakar.practice.repository;

import com.ratnakar.practice.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sqlInsertQuery = "insert into student (rollNo, name, marks) values (?,?,?)";
        int rows = jdbcTemplate.update(sqlInsertQuery, student.getRollNo(), student.getName(), student.getMarks());
        System.out.println(rows+" Student Added Successfully .......");
    }
    /*
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        return students;
    }
    */

    public List<Student> findAll() {
        String sqlGetQuery = "select * from student";

        /*
        RowMapper<Student> mapper = new RowMapper<Student>() {
            @Override
            public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
                Student student = new Student();
                student.setRollNo(rs.getInt("rollNo"));
                student.setName(rs.getString("name"));
                student.setMarks(rs.getInt("marks"));
                return student;
            }
        };
        */

        // By using lambda expression
        RowMapper<Student> mapperObject = (rs, rowNum) -> {
                Student student = new Student();
                student.setRollNo(rs.getInt("rollNo"));
                student.setName(rs.getString("name"));
                student.setMarks(rs.getInt("marks"));
                return student;
            };

        // return jdbcTemplate.query(sqlGetQuery, mapper);
        return jdbcTemplate.query(sqlGetQuery, mapperObject);

    }
}
