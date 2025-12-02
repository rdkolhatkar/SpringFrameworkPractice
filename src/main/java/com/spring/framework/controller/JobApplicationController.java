package com.spring.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobApplicationController {

    @GetMapping({"/homePage", "/job-portal"})
    public String homePage(){
        return "home";
    }

    @GetMapping("/addJobPage")
    public String addJobPage(){
        return "AddJob";
    }
}
