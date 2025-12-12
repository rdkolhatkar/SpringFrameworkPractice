package com.ratnakar.practice.service;

import com.spring.framework.model.JobPostData;
import com.spring.framework.repository.JobAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {
    @Autowired
    JobAppRepository jobAppRepository;
    public void addJob(JobPostData jobPostData){
        jobAppRepository.addJob(jobPostData);
    }
    public List<JobPostData> getAllJobs(){
        return jobAppRepository.getAllJobs();
    }
}
