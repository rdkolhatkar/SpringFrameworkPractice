package com.ratnakar.practice.controller;

import com.ratnakar.practice.service.JobCodeApplicationService;
import com.spring.framework.model.JobPostData;
import com.spring.framework.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 @CrossOrigin allows requests from a different origin (domain/port)
 Here, we are explicitly allowing requests coming from
 the frontend running on http://localhost:3000 (Which is our React Application)
 Without this annotation, browsers will block the request
 due to Same-Origin Policy (CORS restriction).
*/
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class JobRestController {
    @Autowired
    JobCodeApplicationService jobApplicationService;

    @GetMapping("jobPosts")
    @ResponseBody
    public List<JobPostData> getAllJobs(){
        return jobApplicationService.getAllJobs();
    }
    @GetMapping("jobPost/{postId}")
    public JobPostData getJob(@PathVariable("postId") int postId){
        return jobApplicationService.getJob(postId);
    }
}
