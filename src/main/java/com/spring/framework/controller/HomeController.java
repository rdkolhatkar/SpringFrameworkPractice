package com.spring.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Controller tells Spring Boot that this class contains
 * methods (endpoints) which will handle web requests.
 *
 * These methods will return JSP pages as views.
 */
@Controller
public class HomeController {

    /**
     * @RequestMapping("/")
     * This maps the root URL (http://localhost:8080/)
     * to this method.
     *
     * When the user opens the application,
     * Spring Boot calls this method and returns index.jsp page.
     */
    @RequestMapping("/")
    public String home() {
        return "index"; // Loads index.jsp
    }

    // ======================================================
    // 🚀 ADDITION — Uses @GetMapping + @PostMapping
    // ======================================================

    /**
     * @GetMapping("/add")
     * Called when user opens the "Add" form.
     *
     * GET = Display the form
     */
    @GetMapping("/add")
    public String showAddForm() {
        return "index"; // Shows input form again
    }

    /**
     * @PostMapping("/add")
     * Called when form is submitted.
     *
     * @RequestParam is used to fetch values from form fields.
     * Spring automatically converts String → int.
     *
     * Model is used to send data from Controller → JSP page.
     */
    @PostMapping("/add")
    public String doAdd(
            @RequestParam("num1") int num1,
            @RequestParam("num2") int num2,
            Model model) {

        int result = num1 + num2;

        // Adding values to Model so JSP can display them
        model.addAttribute("operation", "Addition");
        model.addAttribute("result", result);

        return "result"; // Loads result.jsp
    }

    // ======================================================
    // ✖ MULTIPLICATION — Uses @GetMapping + @PostMapping
    // ======================================================

    /**
     * GET method for showing the multiplication form.
     */
    @GetMapping("/multiply")
    public String showMultiplyForm() {
        return "index";
    }

    /**
     * POST method to process multiplication.
     *
     * Works same as addition,
     * but performs multiplication instead.
     */
    @PostMapping("/multiply")
    public String doMultiply(
            @RequestParam("num1") int num1,
            @RequestParam("num2") int num2,
            Model model) {

        int result = num1 * num2;

        model.addAttribute("operation", "Multiplication");
        model.addAttribute("result", result);

        return "result";
    }

    // ======================================================
    // ➖ SUBTRACTION — Using RequestMapping + HttpServletRequest
    // ======================================================

    /**
     * @RequestMapping("/subtract")
     *
     * HttpServletRequest is the old way (Servlet API)
     * to fetch form values using request.getParameter()
     *
     * This method:
     * - Gets values manually
     * - Converts them manually
     * - Stores results using req.setAttribute()
     *
     * JSP can access attributes using ${result}
     */
    @RequestMapping("/subtract")
    public String subtract(HttpServletRequest req) {

        int num1 = Integer.parseInt(req.getParameter("num1")); // Get form value
        int num2 = Integer.parseInt(req.getParameter("num2"));
        int result = num1 - num2; // Perform subtraction

        req.setAttribute("operation", "Subtraction"); // Send data to JSP
        req.setAttribute("result", result);

        return "result"; // Shows result.jsp
    }

    // ======================================================
    // ➗ DIVISION — Using RequestMapping + HttpServletRequest
    // ======================================================

    /**
     * Division logic using the same old-style Servlet technique.
     *
     * NOTE:
     * - We check division by zero (num2 == 0)
     * - If error → send an error message to result.jsp
     */
    @RequestMapping("/divide")
    public String divide(HttpServletRequest req) {

        int num1 = Integer.parseInt(req.getParameter("num1"));
        int num2 = Integer.parseInt(req.getParameter("num2"));

        double result = 0;

        // Check for divide-by-zero error
        if (num2 != 0) {
            result = (double) num1 / num2;
        } else {
            req.setAttribute("operation", "Division");
            req.setAttribute("result", "Error: Cannot divide by zero");
            return "result"; // Show error on result.jsp
        }

        req.setAttribute("operation", "Division");
        req.setAttribute("result", result);

        return "result"; // JSP page to show the output
    }

    // This method will handle POST request coming to URL: /addFloat
    @PostMapping("/addFloat")
    public ModelAndView doAddFloat(
            @RequestParam("num1") double num1, // Reads first decimal number from form
            @RequestParam("num2") double num2  // Reads second decimal number from form
    ) {

        // ------------------------------------------
        // STEP 1: Perform the mathematical operation
        // ------------------------------------------
        double result = num1 + num2; // Adding the two floating numbers


        // ---------------------------------------------------------
        // STEP 2: Create a ModelAndView object
        // ---------------------------------------------------------
        // ModelAndView is used when we want to:
        // ✔ send data to JSP page (Model part)
        // ✔ decide which JSP page to open (View part)
        //
        // Think of it like a "package" that contains:
        // → the data you want to show on screen
        // → the JSP page where you want to show it
        // ---------------------------------------------------------
        ModelAndView mv = new ModelAndView();


        // ---------------------------------------------------------
        // STEP 3: Add data to Model (so JSP can display it)
        // ---------------------------------------------------------
        // addObject("key", value):
        // "key" → name you will use inside JSP to print the value
        // value → actual data you want to send to the JSP page
        // ---------------------------------------------------------
        mv.addObject("operation", "Addition of Floating Numbers");
        mv.addObject("result", result);


        // ---------------------------------------------------------
        // STEP 4: Set which JSP page should open
        // ---------------------------------------------------------
        // Here we are telling Spring MVC:
        // “Open the result.jsp page to show the output”
        //
        // Important: Do NOT include .jsp extension if a ViewResolver
        // is already configured. But if your setup needs it, include it.
        // ---------------------------------------------------------
        mv.setViewName("result"); // Loads result.jsp


        // ---------------------------------------------------------
        // STEP 5: Return ModelAndView to Spring
        // ---------------------------------------------------------
        // Spring MVC will do two things automatically:
        // 1️⃣ It sends 'result' and 'operation' values to result.jsp
        // 2️⃣ It opens result.jsp in the browser
        // ---------------------------------------------------------
        return mv;
    }


}
