package com.ratnakar.practice;

import com.ratnakar.practice.entity.Student;
import com.ratnakar.practice.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class SpringJdbcApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringJdbcApplication.class, args);
        // Calling the bean for student entity
        Student student = context.getBean(Student.class);
        student.setRollNo(101);
        student.setName("Navin");
        student.setMarks(78);
        // Calling the bean for student service
        StudentService studentService = context.getBean(StudentService.class);
        studentService.addStudent(student);
        List<Student> students = studentService.getStudents();
    }
}
