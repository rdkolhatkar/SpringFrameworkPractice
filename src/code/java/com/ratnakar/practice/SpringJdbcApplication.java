package com.ratnakar.practice;

import com.ratnakar.practice.entity.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringJdbcApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringJdbcApplication.class, args);
        Student student = context.getBean(Student.class);
        student.setRollNo(101);
        student.setName("Navin");
        student.setMarks(78);

    }
}
