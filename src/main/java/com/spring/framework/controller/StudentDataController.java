package com.spring.framework.controller;
import com.spring.framework.model.StudentData;
import com.spring.framework.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

// Controller handles form submit, connects to DB and returns JSP views.
@Controller
public class StudentDataController {

    @Autowired
    private StudentRepository repository;

    // Load the empty form
    @GetMapping("/student-form")
    public String loadForm(Model model) {

        // Adding empty StudentData object so Spring can bind form fields
        model.addAttribute("student", new StudentData());

        return "StudentLoginForm";
    }

    // Handle form submission
    @PostMapping("/submit-student")
    public String submitStudentData(@ModelAttribute("student") StudentData student, Model model) {

        System.out.println("Received Student: " + student.getFirstName());

        int id = repository.saveStudent(student);
        System.out.println("Generated ID = " + id);

        model.addAttribute("studentName", student.getFirstName() + " " + student.getLastName());
        model.addAttribute("studentId", id);
        model.addAttribute("message", "Student Added Successfully");

        return "StudentSuccess";
    }

}
