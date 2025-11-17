package com.spring.framework.controller;

import com.spring.framework.model.Employee;
import com.spring.framework.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo;

    // Load Registration Page
    @GetMapping("/register")
    public String loadRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "EmployeeRegistration";
    }

    // Handle Web + Postman together
    @PostMapping("/add")
    public String addEmployee(
            @ModelAttribute("employee") Employee employee,
            Model model
    ) {
        Employee saved = repo.save(employee);

        model.addAttribute("employeeName", saved.getEmployeeName());
        model.addAttribute("employeeId", saved.getId());
        model.addAttribute("message", "Employee Added Successfully");

        return "EmployeeSuccess";
    }

    // Postman JSON API
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String addEmployeeAPI(@RequestBody Employee employee) {

        Employee saved = repo.save(employee);

        return "{ \"message\": \"Employee Added Successfully\", \"employeeId\": \"" + saved.getId() + "\" }";
    }
}