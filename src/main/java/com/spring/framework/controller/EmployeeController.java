package com.spring.framework.controller;

import com.spring.framework.model.Employee;
import com.spring.framework.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * This controller handles all web requests related to Employee operations.
 * It supports:
 *   - Loading employee registration form (JSP)
 *   - Saving employee data using a web form (HTML/JSP)
 *   - Saving employee data using Postman JSON request
 *
 * This is a Spring MVC Controller → it handles HTTP requests and returns views (JSP).
 */
@Controller
// @Controller → Marks this class as a Spring MVC Controller.
// It tells Spring to scan this class and allow it to handle web requests.

@RequestMapping("/employee")
// @RequestMapping("/employee") → All URLs inside this class will start with "/employee".
// Example:
//   "/employee/register"
//   "/employee/add"
public class EmployeeController {

    @Autowired
    // @Autowired → Automatically injects EmployeeRepository bean.
    // Dependency Injection → Spring will create object and give it to us.
    private EmployeeRepository repo;

    /**
     * Loads the employee registration form page.
     *
     * @GetMapping("/register")
     * → This method handles GET requests for "/employee/register".
     * Example:
     *    http://localhost:8080/employee/register
     *
     * Model:
     *  - Used to pass data from controller to JSP page.
     */
    @GetMapping("/register")
    public String loadRegistrationForm(Model model) {

        // Add an empty Employee object for the JSP form binding
        model.addAttribute("employee", new Employee());

        // Return the name of the JSP file to render (EmployeeRegistration.jsp)
        return "EmployeeRegistration";
    }

    /**
     * Handles form submission (HTML/JSP form) AND Postman form-data.
     *
     * @PostMapping("/add")
     * → Handles POST requests for "/employee/add"
     *
     * @ModelAttribute("employee")
     * → Automatically binds form input fields to the Employee object.
     *   Works with JSP forms (Spring MVC form binding).
     *
     * Model:
     * → Used to send success message and employee details to success page.
     */
    @PostMapping("/add")
    public String addEmployee(
            @ModelAttribute("employee") Employee employee,
            // @ModelAttribute → Maps form input names to Employee fields.
            // Example:
            // <input name="employeeName"> → employee.setEmployeeName()
            Model model
    ) {

        // Save the employee in database using repository
        Employee saved = repo.save(employee);

        // Send data to JSP page using model
        model.addAttribute("employeeName", saved.getEmployeeName());
        model.addAttribute("employeeId", saved.getId());
        model.addAttribute("message", "Employee Added Successfully");

        // Return success JSP page
        return "EmployeeSuccess";
    }

    /**
     * Handles JSON request (Postman or API client).
     *
     * This method also maps to "/employee/add" but only when:
     *   - Content-Type = application/json
     *
     * consumes = "application/json"
     * → Tells Spring to use this method only for JSON input
     *
     * produces = "application/json"
     * → Response will be sent back as JSON
     *
     * @ResponseBody
     * → Tells Spring NOT to return a JSP view.
     *   Instead, return raw JSON text directly as API output.
     *
     * @RequestBody
     * → Converts JSON from request body into Employee object.
     */
    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String addEmployeeAPI(@RequestBody Employee employee) {

        // Save employee data from JSON request
        Employee saved = repo.save(employee);

        // Respond with JSON string
        return "{ \"message\": \"Employee Added Successfully\", \"employeeId\": \"" + saved.getId() + "\" }";
    }
}
