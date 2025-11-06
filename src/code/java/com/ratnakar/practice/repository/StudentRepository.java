package com.ratnakar.practice.repository;

import com.ratnakar.practice.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepository {
    public void save(Student student){
        System.out.println("Student Added Successfully .......");
    }

    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        return students;
    }
}
