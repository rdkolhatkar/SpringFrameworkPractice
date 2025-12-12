package com.ratnakar.practice.controller;

import com.spring.framework.model.JobPostData;
import com.spring.framework.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobRestController {
    @Autowired
    JobApplicationService jobApplicationService;

    @GetMapping("jobPost")
    @ResponseBody
    public List<JobPostData> getAllJobs(){
        return jobApplicationService.getAllJobs();
    }
}
