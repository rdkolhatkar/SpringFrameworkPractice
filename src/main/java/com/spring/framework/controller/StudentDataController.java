package com.spring.framework.controller;

// Importing our Model/Entity class which holds student form data
import com.spring.framework.model.StudentData;

// Importing Repository class which contains database logic (save, update, etc.)
import com.spring.framework.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;     // For Dependency Injection
import org.springframework.stereotype.Controller;             // Makes this class a Spring MVC Controller
import org.springframework.web.bind.annotation.*;             // Contains MVC annotations like @GetMapping, @PostMapping, @ModelAttribute
import org.springframework.ui.Model;                          // Used to pass data from Controller to JSP view

/**
 * This class works as a Controller in Spring MVC.
 * Controller = Takes user inputs, sends to service/repository, returns a view page.
 *
 * It handles:
 * 1. Showing the student form (GET)
 * 2. Receiving submitted form data (POST)
 * 3. Storing data in database using StudentRepository
 * 4. Returning JSP pages as output
 */
@Controller   // ⬅️ Tells Spring that this class is a Controller in MVC framework
public class StudentDataController {

    /**
     * @Autowired:
     * Spring will automatically create an object of StudentRepository
     * and inject it here.
     *
     * This is called Dependency Injection (DI).
     * Without @Autowired, we would have to write:
     * StudentRepository repo = new StudentRepository();
     * which is not recommended.
     */
    @Autowired
    private StudentRepository repository;

    /**
     * This method runs when the user opens:
     * http://localhost:8080/student-form   (GET request)
     *
     * @GetMapping:
     * - Handles HTTP GET requests.
     * - Used to show pages/forms.
     *
     * Model model:
     * - Used to send data from Controller → JSP page.
     * - Works like a data map (key → value).
     */
    @GetMapping("/student-form")
    public String loadForm(Model model) {

        /**
         * We add an empty StudentData object to the model.
         * Why?
         * - JSP form fields need an object to bind to.
         * - Spring will automatically fill this object during form submission.
         *
         * Example:
         * <input name="firstName">
         * will go inside student.setFirstName(...)
         */
        model.addAttribute("student", new StudentData());

        /**
         * Returning the name of the JSP page.
         * Spring searches:
         * /WEB-INF/views/StudentLoginForm.jsp
         */
        return "StudentLoginForm";
    }

    /**
     * This method handles the form submission when user clicks Submit button.
     * POST request to: /submit-student
     *
     * @PostMapping:
     * - Handles HTTP POST requests.
     * - Used to save data / update data / any form submission.
     *
     * @ModelAttribute("student"):
     * - Spring automatically picks values from the form fields.
     * - Fills the StudentData object with submitted data.
     *
     * Example:
     * input name="firstName" → student.setFirstName(value)
     *
     * Model model:
     * - To send success message and saved student details back to JSP page.
     */
    @PostMapping("/submit-student")
    public String submitStudentData(@ModelAttribute("student") StudentData student, Model model) {

        // Console output for debugging—verifying that firstName was received.
        System.out.println("Received Student: " + student.getFirstName());

        /**
         * Saving student data in database.
         * The repository will:
         * - run the SQL INSERT query
         * - return the generated student ID (primary key)
         */
        int id = repository.saveStudent(student);
        System.out.println("Generated ID = " + id);

        /**
         * Adding data to be displayed in the success JSP page:
         * ${studentName}
         * ${studentId}
         * ${message}
         */
        model.addAttribute("studentName", student.getFirstName() + " " + student.getLastName());
        model.addAttribute("studentId", id);
        model.addAttribute("message", "Student Added Successfully");

        /**
         * Returning success JSP page
         * /WEB-INF/views/StudentSuccess.jsp
         */
        return "StudentSuccess";
    }
    /*
    Parameters explained:
    @ModelAttribute("student") StudentData student
    What it does: tells Spring to create (or reuse) a StudentData object and bind incoming form parameters to its fields.
    Example: an input named firstName in the form will be set into student.setFirstName(...).
    The string "student" matches the model attribute used when the form was rendered (so binding works cleanly).
    It handles type conversion (strings → dates, numbers) if configured properly (e.g., @DateTimeFormat).
    Model model
    Used to pass response data to the view (success message, student id, student name).
    */

}
