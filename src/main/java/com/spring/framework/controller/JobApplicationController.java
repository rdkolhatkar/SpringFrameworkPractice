package com.spring.framework.controller;

import com.spring.framework.model.JobPostData;
import com.spring.framework.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class JobApplicationController {

    /*
    @GetMapping handles HTTP GET requests, used to fetch or read data from the server.
    @PostMapping handles HTTP POST requests, used to send or create data on the server.
    @RequestMapping is a generic mapping that can handle any HTTP method (GET, POST, PUT, DELETE) when specified. It provides broader control, while GetMapping and PostMapping are method-specific shortcuts.
    */

    @Autowired
    private JobApplicationService jobApplicationService;

    @GetMapping({"/home", "/job-portal"})
    public String homePage(){
        return "home";
    }

    @GetMapping("/addJobPage")
    public String addJobPage(){
        return "AddJob";
    }

    @PostMapping("/handleForm")
    public String handleForm(JobPostData jobPostData){
        jobApplicationService.addJob(jobPostData);
        return "JobSuccess";
    }

    @GetMapping("/jobs")
    public String showJobs(Model model) {
        List<JobPostData> jobList = jobApplicationService.getAllJobs(); // fetch jobs from DB or service
        model.addAttribute("jobs", jobList);
        return "JobSuccess"; // JobSuccess.jsp
    }

    @GetMapping("viewAllJobs")
    public String viewJobs(Model model){
        List<JobPostData> jobs = jobApplicationService.getAllJobs();
        model.addAttribute("jobPosts", jobs);
       return "ViewAllJobs";
    }


}
