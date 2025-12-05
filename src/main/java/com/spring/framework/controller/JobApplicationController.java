package com.spring.framework.controller;

import com.spring.framework.model.JobPostData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JobApplicationController {

    /*
    @GetMapping handles HTTP GET requests, used to fetch or read data from the server.
    @PostMapping handles HTTP POST requests, used to send or create data on the server.
    @RequestMapping is a generic mapping that can handle any HTTP method (GET, POST, PUT, DELETE) when specified. It provides broader control, while GetMapping and PostMapping are method-specific shortcuts.
    */

    @GetMapping({"/homePage", "/job-portal"})
    public String homePage(){
        return "home";
    }

    @GetMapping("/addJobPage")
    public String addJobPage(){
        return "AddJob";
    }

    @PostMapping("handleForm")
    public String handleForm(JobPostData jobPostData){
        return "JobSuccess";
    }
}
